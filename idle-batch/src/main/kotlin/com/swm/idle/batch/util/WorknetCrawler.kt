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
        "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do?basicSetupYn=&careerTo=&keywordJobCd=&occupation=&seqNo=&cloDateEndtParam=&payGbn=&templateInfo=&rot2WorkYn=&shsyWorkSecd=&srcKeywordParam=%EC%9A%94%EC%96%91+%EB%B3%B4%ED%98%B8%EC%82%AC&resultCnt=50&keywordJobCont=&cert=&moreButtonYn=Y&minPay=&codeDepth2Info=11000&currentPageNo=1&eventNo=&mode=&major=&resrDutyExcYn=&eodwYn=&sortField=DATE&staArea=&sortOrderBy=DESC&keyword=%EC%9A%94%EC%96%91+%EB%B3%B4%ED%98%B8%EC%82%AC&termSearchGbn=&carrEssYns=&benefitSrchAndOr=O&disableEmpHopeGbn=&actServExcYn=&keywordStaAreaNm=&maxPay=&emailApplyYn=&codeDepth1Info=11000&keywordEtcYn=&regDateStdtParam=20241111&publDutyExcYn=&keywordJobCdSeqNo=&viewType=&exJobsCd=&templateDepthNmInfo=&region=&employGbn=&empTpGbcd=1&computerPreferential=&infaYn=&cloDateStdtParam=&siteClcd=WORK&searchMode=Y&birthFromYY=&indArea=&careerTypes=&subEmpHopeYn=&tlmgYn=&academicGbn=&templateDepthNoInfo=&foriegn=&entryRoute=&mealOfferClcd=&basicSetupYnChk=&station=&holidayGbn=&srcKeyword=%EC%9A%94%EC%96%91+%EB%B3%B4%ED%98%B8%EC%82%AC&academicGbnoEdu=noEdu&enterPriseGbn=all&cloTermSearchGbn=&birthToYY=&keywordWantedTitle=&stationNm=&benefitGbn=&notSrcKeywordParam=&keywordFlag=&notSrcKeyword=&essCertChk=&depth2SelCode=&keywordBusiNm=&preferentialGbn=&rot3WorkYn=&regDateEndtParam=20241112&pfMatterPreferential=&pageIndex=1&termContractMmcnt=&careerFrom=&laborHrShortYn=#scrollLoc"

//        "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do?basicSetupYn=&careerTo=&keywordJobCd=&occupation=&seqNo=&cloDateEndtParam=&payGbn=&templateInfo=&rot2WorkYn=&shsyWorkSecd=&srcKeywordParam=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&resultCnt=10&keywordJobCont=&cert=&moreButtonYn=Y&minPay=&codeDepth2Info=11000&currentPageNo=1&eventNo=&mode=&major=&resrDutyExcYn=&eodwYn=&sortField=DATE&staArea=&sortOrderBy=DESC&keyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&termSearchGbn=all&carrEssYns=&benefitSrchAndOr=O&disableEmpHopeGbn=&actServExcYn=&keywordStaAreaNm=&maxPay=&emailApplyYn=&codeDepth1Info=11000&keywordEtcYn=&regDateStdtParam=20241104&publDutyExcYn=&keywordJobCdSeqNo=&viewType=&exJobsCd=&templateDepthNmInfo=&region=&employGbn=&empTpGbcd=&computerPreferential=&infaYn=&cloDateStdtParam=&siteClcd=WORK&searchMode=Y&birthFromYY=&indArea=&careerTypes=&subEmpHopeYn=&tlmgYn=&academicGbn=&templateDepthNoInfo=&foriegn=&entryRoute=&mealOfferClcd=&basicSetupYnChk=&station=&holidayGbn=&srcKeyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&academicGbnoEdu=noEdu&enterPriseGbn=all&cloTermSearchGbn=all&birthToYY=&keywordWantedTitle=&stationNm=&benefitGbn=&notSrcKeywordParam=&keywordFlag=&notSrcKeyword=&essCertChk=&depth2SelCode=&keywordBusiNm=&preferentialGbn=&rot3WorkYn=&regDateEndtParam=20241108&pfMatterPreferential=&pageIndex={pageIndex}&termContractMmcnt=&careerFrom=&laborHrShortYn=#scrollLoc"

    private const val JOB_POSTING_COUNT_PER_PAGE = 50

    private lateinit var driver: WebDriver

    private val postings = mutableListOf<CrawledJobPostingDto>()

    // 에러 카운트를 저장할 맵
    private val errorCountMap = mutableMapOf<String, Int>()

    private fun initializeDriver() {
        try {
            val service = ChromeDriverService.Builder()
                .usingDriverExecutable(File(System.getenv("CHROMEDRIVER_BIN")))
                .build()

            val options = ChromeOptions().apply {
                addArguments("--headless")
                addArguments("--no-sandbox")
                addArguments("--disable-dev-shm-usage")
                addArguments("--disable-gpu")
                addArguments("window-size=1920x1080")
                addArguments("--disable-software-rasterizer")
                addArguments("--ignore-ssl-errors=yes")
                addArguments("--ignore-certificate-errors")

                setBinary(System.getenv("CHROME_BIN"))
            }

            driver = ChromeDriver(service, options)
        } catch (e: Exception) {
            logError("initializeDriver", e)
        }
    }

    fun run(): List<CrawledJobPostingDto>? {
        return try {
            try {
                initializeDriver()
            } catch (e: Exception) {
                logger.error { e.toString() }
                logError("run", e)
            }

            logger.info { "=====초기화 완료, 크롤링 작업 시작" }

            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val yesterday = LocalDate.now().format(formatter)
            val crawlingUrl = CRAWLING_TARGET_URL_FORMAT
                .replace("{yesterday}", yesterday)
                .replace("{pageIndex}", "1")

            driver.get(crawlingUrl)

            logger.info { "=====크롤링 url: $crawlingUrl" }

            val wait = WebDriverWait(driver, Duration.ofSeconds(15))
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mForm\"]/div[2]/div/div[1]/div[1]/span/span")))

            val jobPostingCountText =
                driver.findElement(By.xpath("//*[@id=\"mForm\"]/div[2]/div/div[1]/div[1]/span/span")).text

            logger.info { "=====크롤링 대상 공고 수: $jobPostingCountText" }

            val jobPostingCount = Integer.parseInt(jobPostingCountText.replace(",", ""))

            if (jobPostingCount == 0) {
                driver.quit()
                logger.info { "=====크롤링 할 공고가 없어 미리 종료합니다." }
                return emptyList() // 데이터가 없는 경우 빈 리스트 반환
            }

            val pageCount = jobPostingCount / JOB_POSTING_COUNT_PER_PAGE
            logger.warn { "===== 크롤링 페이지 수 $pageCount" }

            for (i in 1..pageCount) {
                if (i >= 2) {
                    val updatedCrawlingUrl = crawlingUrl
                        .replace("{yesterday}", yesterday)
                        .replace(Regex("pageIndex=\\d+"), "pageIndex=${i}")
                    driver.get(updatedCrawlingUrl)
                }

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1")))
                crawlPosts(1, JOB_POSTING_COUNT_PER_PAGE, postings)
            }

            val lastPageJobPostingCount = jobPostingCount % JOB_POSTING_COUNT_PER_PAGE
            if (lastPageJobPostingCount > 0) {
                val updateCrawlingUrl = crawlingUrl
                    .replace("{yesterday}", yesterday)
                    .replace(Regex("pageIndex=\\d+"), "pageIndex=${pageCount + 1}")
                driver.get(updateCrawlingUrl)

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1")))
                crawlPosts(1, lastPageJobPostingCount, postings)
            }

            postings // 정상적으로 크롤링이 완료되었을 경우 수집한 데이터를 반환
        } catch (e: Exception) {
            logger.error { "Error occurred during crawling, returning collected data so far: ${e.message}" }
            postings // 에러가 발생해도 현재까지 수집된 데이터를 반환
        } finally {
            driver.quit()
        }
    }


    private fun logError(method: String, e: Exception) {
        logger.error(e) { "Error occurred in $method: ${e.message}" }
        errorCountMap[method] = errorCountMap.getOrDefault(method, 0) + 1
    }

    private fun crawlPosts(
        start: Int,
        end: Int,
        postings: MutableList<CrawledJobPostingDto>,
    ) {
        for (i in start..end) {
            try {
                val originalWindow = driver.windowHandle

                val element = driver.findElement(By.xpath("//*[@id=\"list$i\"]/td[1]/div/div[2]/a"))
                element.click()

                if (handleAlertIfPresent()) {
                    driver.navigate().back()
                    continue
                }

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
                logError("=== 에러 원인은..", e)
            }
        }
    }

    private fun handleAlertIfPresent(): Boolean {
        return try {
            val alert: Alert = driver.switchTo().alert()
            alert.accept()  // 알림창이 있을 경우 수락
            true // 알림창이 있었음을 표시
        } catch (e: NoAlertPresentException) {
            false // 알림창이 없었음을 표시
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
                logError("getClientAddress", e)
            }
        }

        throw NoSuchElementException("클라이언트 주소 크롤링 에러")
    }

    private fun getRequiredDocument(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[4]")).text
        } catch (e: Exception) {
            logError("getRequiredDocument", e)
            throw e
        }
    }

    private fun getApplyMethod(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[3]")).text
        } catch (e: Exception) {
            logError("getApplyMethod", e)
            throw e
        }
    }

    private fun getRecruitmentProcess(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[2]")).text
        } catch (e: Exception) {
            logError("getRecruitmentProcess", e)
            throw e
        }
    }

    private fun getApplyDeadline(): String {
        return try {
            val applyDeadline =
                driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[7]/table/tbody/tr/td[1]")).text

            if (applyDeadline.contains("채용시까지")) {
                LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            } else {
                applyDeadline
            }
        } catch (e: Exception) {
            logError("getApplyDeadline", e)
            throw e
        }
    }

    private fun getWorkSchedule(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[6]/table/tbody/tr/td[3]")).text
        } catch (e: Exception) {
            logError("getWorkSchedule", e)
            throw e
        }
    }

    private fun getWorkTime(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[6]/table/tbody/tr/td[2]")).text
                .replace("(근무시간) ", "")
                .substringBefore("주 소정근로시간").trim()
        } catch (e: Exception) {
            logError("getWorkTime", e)
            throw e
        }
    }

    private fun getPayInfo(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[2]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/span")).text
        } catch (e: Exception) {
            logError("getPayInfo", e)
            throw e
        }
    }

    private fun getCenterName(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[2]/div[1]/div[2]/div[2]/ul/li[1]/div")).text
        } catch (e: Exception) {
            logError("getCenterName", e)
            throw e
        }
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
                logError("getCreatedAt", e)
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
                logError("getCenterAddress", e)
            }
        }

        throw NoSuchElementException("Center address not found using any of the provided XPaths")
    }

    private fun getContent(): String {
        return try {
            driver.findElement(By.xpath("//*[@id=\"contents\"]/section/div/div[3]/div[3]/table/tbody/tr/td")).text
        } catch (e: Exception) {
            logError("getContent", e)
            throw e
        }
    }

    private fun getTitle(): String {
        return try {
            val em = driver.findElement(By.cssSelector(".left"))
            em.findElement(By.cssSelector(".tit-area .tit")).text
        } catch (e: Exception) {
            logError("getTitle", e)
            throw e
        }
    }

    // 에러 집계를 출력하는 메서드 추가
    fun printErrorSummary() {
        logger.error { "===== 에러 집계 =====" }
        errorCountMap.forEach { (method, count) ->
            logger.error { "$method: $count errors" }
        }
    }
}
