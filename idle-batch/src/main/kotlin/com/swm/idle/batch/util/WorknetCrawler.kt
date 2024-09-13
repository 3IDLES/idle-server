package com.swm.idle.batch.util

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WorknetCrawler {

    private const val CRAWLING_TARGET_URL_FORMAT =
        "https://www.work.go.kr/empInfo/empInfoSrch/list/dtlEmpSrchList.do?careerTo=&keywordJobCd=&occupation=550100%2C550104&templateInfo=&shsyWorkSecd=&rot2WorkYn=&payGbn=&resultCnt=50&keywordJobCont=&cert=&cloDateStdt=&moreCon=&minPay=&codeDepth2Info=11000&isChkLocCall=&sortFieldInfo=DATE&major=&resrDutyExcYn=&eodwYn=&sortField=DATE&staArea=&sortOrderBy=DESC&keyword=&termSearchGbn=D-0&carrEssYns=&benefitSrchAndOr=O&disableEmpHopeGbn=&webIsOut=&actServExcYn=&maxPay=&keywordStaAreaNm=&emailApplyYn=&listCookieInfo=DTL&pageCode=&codeDepth1Info=11000&keywordEtcYn=&publDutyExcYn=&keywordJobCdSeqNo=&exJobsCd=&templateDepthNmInfo=&computerPreferential=&regDateStdt={today}&employGbn=&empTpGbcd=&region=&infaYn=&resultCntInfo=50&siteClcd=WORK%2CP&cloDateEndt=&sortOrderByInfo=DESC&currntPageNo=1&indArea=&careerTypes=&searchOn=Y&tlmgYn=&subEmpHopeYn=&academicGbn=&templateDepthNoInfo=&foriegn=&mealOfferClcd=&station=&moerButtonYn=Y&holidayGbn=&srcKeyword=&enterPriseGbn=all&academicGbnoEdu=noEdu&cloTermSearchGbn=all&keywordWantedTitle=&stationNm=&benefitGbn=&keywordFlag=&notSrcKeyword=&essCertChk=&isEmptyHeader=&depth2SelCode=&_csrf=355cf055-ee67-497a-9695-a65cabc28829&keywordBusiNm=&preferentialGbn=&rot3WorkYn=&pfMatterPreferential=&regDateEndt={today}&staAreaLineInfo1=11000&staAreaLineInfo2=1&pageIndex={pageIndex}&termContractMmcnt=&careerFrom=&laborHrShortYn=#viewSPL"

    private const val JOB_POSTING_COUNT_PER_PAGE = 50

    private lateinit var driver: WebDriver
    private val logger = KotlinLogging.logger { }

    init {
        initializeDriver()
    }

    // Driver 초기화 메서드
    private fun initializeDriver() {
        val options = ChromeOptions()
        options.addArguments("--headless")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--disable-gpu")
        options.addArguments("window-size=1400,1500");
        driver = ChromeDriver(options)
    }

    private val postings = mutableListOf<CrawledJobPostingDto>()

    fun run(): List<CrawledJobPostingDto>? {
        initializeDriver()

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val today = LocalDate.now().format(formatter)
        val crawlingUrl = CRAWLING_TARGET_URL_FORMAT
            .replace("{today}", today)
            .replace("{pageIndex}", "1")

        logger.warn { "기본 세팅중" }
        driver.get(crawlingUrl)
        logger.warn { "초기화 완료" }

        // WebDriverWait에 대기 시간을 충분히 설정 (10초)
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"srcFrm\"]/div[3]/div[1]/p[2]/em")))

        val jobPostingCountText =
            driver.findElement(By.xpath("//*[@id=\"srcFrm\"]/div[3]/div[1]/p[2]/em")).text
        val jobPostingCount = jobPostingCountText.replace(",", "").toInt()

        if (jobPostingCount == 0) {
            driver.quit()
            return null
        }

        val pageCount = jobPostingCount / JOB_POSTING_COUNT_PER_PAGE

        for (i in 1..2) {
            if (i >= 2) {
                val updatedCrawlingUrl = crawlingUrl
                    .replace("{today}", today)
                    .replace(Regex("pageIndex=\\d+"), "pageIndex=${i}")
                driver.get(updatedCrawlingUrl)
            }

            // 대기 시간을 10초로 늘림
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

    // 알림창(Alert) 처리 - 없으면 예외를 잡고 처리
    private fun handleAlertIfPresent() {
        try {
            val alert: Alert = driver.switchTo().alert()
            logger.warn { "Alert detected: ${alert.text}" }
            alert.accept()  // Alert 창의 '확인'을 누름
        } catch (e: NoAlertPresentException) {
            // 알림창이 없으면 무시하고 넘어감
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

                val em = driver.findElement(By.id("list$i"))
                val thirdTd = em.findElements(By.tagName("td"))[2]
                val jobPostingDetail = thirdTd.findElement(By.cssSelector(".cp-info .cp-info-in a"))
                jobPostingDetail.click()

                handleAlertIfPresent()

                // 대기 시간 5초로 설정 (새 창이 뜨는 것을 기다림)
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
                logger.error { "Error crawling job posting $i: ${e.message}" }
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
                // Ignore and try the next XPath
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
