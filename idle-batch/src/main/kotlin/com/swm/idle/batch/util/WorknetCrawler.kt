package com.swm.idle.batch.util

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WorknetCrawler {

    private val logger = KotlinLogging.logger { }

    private const val CRAWLING_TARGET_URL_FORMAT =
        "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do?basicSetupYn=&careerTo=&keywordJobCd=&occupation=&seqNo=&cloDateEndtParam=&payGbn=&templateInfo=&rot2WorkYn=&shsyWorkSecd=&srcKeywordParam=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&resultCnt=10&keywordJobCont=&cert=&moreButtonYn=Y&minPay=&codeDepth2Info=11000&currentPageNo=1&eventNo=&mode=&major=&resrDutyExcYn=&eodwYn=&sortField=DATE&staArea=&sortOrderBy=DESC&keyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&termSearchGbn=all&carrEssYns=&benefitSrchAndOr=O&disableEmpHopeGbn=&actServExcYn=&keywordStaAreaNm=&maxPay=&emailApplyYn=&codeDepth1Info=11000&keywordEtcYn=&regDateStdtParam={today}&publDutyExcYn=&keywordJobCdSeqNo=&viewType=&exJobsCd=&templateDepthNmInfo=&region=&employGbn=&empTpGbcd=&computerPreferential=&infaYn=&cloDateStdtParam=&siteClcd=WORK&searchMode=Y&birthFromYY=&indArea=&careerTypes=&subEmpHopeYn=&tlmgYn=&academicGbn=&templateDepthNoInfo=&foriegn=&entryRoute=&mealOfferClcd=&basicSetupYnChk=&station=&holidayGbn=&srcKeyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&academicGbnoEdu=noEdu&enterPriseGbn=all&cloTermSearchGbn=all&birthToYY=&keywordWantedTitle=&stationNm=&benefitGbn=&notSrcKeywordParam=&keywordFlag=&notSrcKeyword=&essCertChk=&depth2SelCode=&keywordBusiNm=&preferentialGbn=&rot3WorkYn=&regDateEndtParam={today}&pfMatterPreferential=&pageIndex={pageIndex}&termContractMmcnt=&careerFrom=&laborHrShortYn=#scrollLoc"

    private const val JOB_POSTING_COUNT_PER_PAGE = 50

    private const val CHROMIUM_DRIVER_PATH = "/usr/bin/chromedriver"
    private const val CHROMIUM_BROWSER_PATH = "/usr/bin/chromium-browser"

    private lateinit var driver: WebDriver

    private val postings = mutableListOf<CrawledJobPostingDto>()

    private fun initializeDriver() {
        val service = ChromeDriverService.Builder()
            .usingDriverExecutable(File(CHROMIUM_DRIVER_PATH))
            .build()

        val options = ChromeOptions().apply {
            addArguments("--headless")
            addArguments("--no-sandbox")
            addArguments("--disable-dev-shm-usage")
            addArguments("--disable-gpu")
            addArguments("window-size=1920x1080")
            addArguments("--disable-software-rasterizer")
            addArguments("--ignore-ssl-errors=yes");
            addArguments("--ignore-certificate-errors");

            setBinary(CHROMIUM_BROWSER_PATH)
        }

        driver = ChromeDriver(service, options)
    }


    fun run(): List<CrawledJobPostingDto>? {
        initializeDriver()

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val today = LocalDate.now().minusDays(3).format(formatter)
        val crawlingUrl = CRAWLING_TARGET_URL_FORMAT
            .replace("{today}", today)
            .replace("{pageIndex}", "1")

        driver.get(crawlingUrl)

        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mForm\"]/div[2]/div/div[1]/div[1]/span/span")))

        val jobPostingCountText =
            driver.findElement(By.xpath("//*[@id=\"mForm\"]/div[2]/div/div[1]/div[1]/span/span")).text
        val jobPostingCount = Integer.parseInt(jobPostingCountText)

        if (jobPostingCount == 0) {
            driver.quit()
            logger.warn { "0" }
            return null
        }

        val pageCount = jobPostingCount / JOB_POSTING_COUNT_PER_PAGE

        logger.warn { "pageCount= " + pageCount }

        for (i in 1..pageCount) {
            if (i >= 2) {
                val updatedCrawlingUrl = crawlingUrl
                    .replace("{today}", today)
                    .replace(Regex("pageIndex=\\d+"), "pageIndex=${i}")
                driver.get(updatedCrawlingUrl)
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1")))

            crawlPosts(1, JOB_POSTING_COUNT_PER_PAGE, postings)
        }

        val lastPageJobPostingCount = jobPostingCount % JOB_POSTING_COUNT_PER_PAGE

        if (lastPageJobPostingCount > 0) {
            val updateCrawlingUrl = crawlingUrl
                .replace("{today}", today)
                .replace(Regex("pageIndex=\\d+"), "pageIndex=${pageCount + 1}")
            driver.get(updateCrawlingUrl)

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1")))

            crawlPosts(1, lastPageJobPostingCount, postings)
        }

        driver.quit()
        return postings
    }

    private fun handleAlertIfPresent() {
        try {
            val alert: Alert = driver.switchTo().alert()
            alert.accept()
            driver.navigate().back()
        } catch (e: NoAlertPresentException) {
        }
    }


    private fun crawlPosts(
        start: Int,
        end: Int,
        postings: MutableList<CrawledJobPostingDto>,
    ) {
        for (i in start..end) {
            try {
                val originalWindow = driver.windowHandle

                val element = driver.findElement(By.xpath("//*[@id=\"list$i\"]/td[2]/a"))
                element.click()

                handleAlertIfPresent()

                val wait = WebDriverWait(driver, Duration.ofSeconds(5))
                wait.until(ExpectedConditions.numberOfWindowsToBe(2))

                val allWindows = driver.windowHandles

                for (windowHandle in allWindows) {
                    if (windowHandle != originalWindow) {
                        driver.switchTo().window(windowHandle)
                        break
                    }
                }

                val crawledJobPostingDto = CrawledJobPostingDto(
                    title = getTitle(),
                    content = getContent(),
                    clientAddress = getClientAddress(),
                    createdAt = getCreatedAt(),
                    payInfo = getPayInfo(),
                    workTime = getWorkTime(),
                    workSchedule = getWorkSchedule(),
                    applyDeadline = getApplyDeadline(),
                    recruitmentProcess = getRecruitmentProcess(),
                    applyMethod = getApplyMethod(),
                    requiredDocument = getRequiredDocument(),
                    centerName = getCenterName(),
                    centerAddress = getCenterAddress(),
                    directUrl = driver.currentUrl,
                )

                postings.add(crawledJobPostingDto)

                driver.close()
                driver.switchTo().window(originalWindow)
            } catch (e: Exception) {
                handleAlertIfPresent()
            }
        }
    }


    private fun getClientAddress(): String {
        val xpaths = listOf(
            "//*[@id=\"contents\"]/section/div/div[3]/div[15]/div/table/tbody/tr[4]/td/p[2]",
            "//*[@id=\"contents\"]/section/div/div[3]/div[17]/div/table/tbody/tr[4]/td/p[2]",
            "//*[@id=\"contents\"]/section/div/div[3]/div[16]/div/table/tbody/tr[4]/td/p[2]"
        )

        for (xpath in xpaths) {
            try {
                val address = driver.findElement(By.xpath(xpath)).text
                return address.replace("지도보기", "").trim()
            } catch (e: Exception) {
                // Ignore and try the next XPath
            }
        }

        throw NoSuchElementException("클라이언트 주소 크롤링 에러")
    }


    private fun getRequiredDocument(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[4]")).text
    }

    private fun getApplyMethod(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[3]")).text
    }

    private fun getRecruitmentProcess(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[2]")).text
    }

    private fun getApplyDeadline(): String {
        val applyDeadline =
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[1]")).text

        return if (applyDeadline.contains("채용시까지")) {
            LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        } else {
            applyDeadline
        }
    }

    private fun getWorkSchedule(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[6]/table/tbody/tr/td[3]")).text
    }

    private fun getWorkTime(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[6]/table/tbody/tr/td[2]")).text
            .replace("(근무시간) ", "")
            .substringBefore("주 소정근로시간").trim()
    }

    private fun getPayInfo(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[2]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/span")).text
    }

    private fun getCenterName(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[2]/div[1]/div[2]/div[2]/ul/li[1]/div")).text
    }

    private fun getCreatedAt(): String {
        val xpaths = listOf(
            "//*[@id=\"contents\"]/section/div/div[3]/div[16]/table/tbody/tr/td[1]",
            "//*[@id=\"contents\"]/section/div/div[3]/div[18]/table/tbody/tr/td[1]",
            "//*[@id=\"contents\"]/section/div/div[3]/div[17]/table/tbody/tr/td[1]"
        )

        for (xpath in xpaths) {
            try {
                return driver.findElement(By.xpath(xpath)).text
            } catch (e: Exception) {
                // Ignore and try the next XPath
            }
        }

        throw NoSuchElementException("CreatedAt element not found using any of the provided XPaths")
    }


    private fun getCenterAddress(): String {
        val xpaths = listOf(
            "//*[@id=\"contents\"]/section/div/div[3]/div[15]/div/table/tbody/tr[1]/td",
            "//*[@id=\"contents\"]/section/div/div[3]/div[17]/div/table/tbody/tr[1]/td",
            "//*[@id=\"contents\"]/section/div/div[3]/div[16]/div/table/tbody/tr[4]/td/p[2]"
        )

        for (xpath in xpaths) {
            try {
                val address = driver.findElement(By.xpath(xpath)).text
                return address.replace("지도보기", "").trim().replace(Regex("\\(\\d{5}\\)"), "").trim()
            } catch (e: Exception) {
            }
        }

        throw NoSuchElementException("Center address not found using any of the provided XPaths")
    }


    private fun getContent(): String {
        return driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[3]/table/tbody/tr/td")).text
    }

    private fun getTitle(): String {
        val em = driver.findElement(By.cssSelector(".left"))
        return em.findElement(By.cssSelector(".tit-area .tit")).text
    }

}
