package com.swm.idle.batch.crawler

enum class CrawlerConsts(val location: String, val value: String) {
    CRAWLING_TARGET_URL_FORMAT("CRAWLING_TARGET_URL_FORMAT","https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do?basicSetupYn=&careerTo=&keywordJobCd=&occupation=&seqNo=&cloDateEndtParam=&payGbn=&templateInfo=&rot2WorkYn=&shsyWorkSecd=&srcKeywordParam=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&resultCnt=50&keywordJobCont=&cert=&moreButtonYn=Y&minPay=&codeDepth2Info=11000&currentPageNo=1&eventNo=&mode=&major=&resrDutyExcYn=&eodwYn=&sortField=DATE&staArea=&sortOrderBy=DESC&keyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&termSearchGbn=all&carrEssYns=&benefitSrchAndOr=O&disableEmpHopeGbn=&actServExcYn=&keywordStaAreaNm=&maxPay=&emailApplyYn=&codeDepth1Info=11000&keywordEtcYn=&regDateStdtParam={yesterday}&publDutyExcYn=&keywordJobCdSeqNo=&viewType=&exJobsCd=&templateDepthNmInfo=&region=&employGbn=&empTpGbcd=&computerPreferential=&infaYn=&cloDateStdtParam=&siteClcd=WORK&searchMode=Y&birthFromYY=&indArea=&careerTypes=&subEmpHopeYn=&tlmgYn=&academicGbn=&templateDepthNoInfo=&foriegn=&entryRoute=&mealOfferClcd=&basicSetupYnChk=&station=&holidayGbn=&srcKeyword=%EC%9A%94%EC%96%91%EB%B3%B4%ED%98%B8%EC%82%AC&academicGbnoEdu=noEdu&enterPriseGbn=all&cloTermSearchGbn=all&birthToYY=&keywordWantedTitle=&stationNm=&benefitGbn=&notSrcKeywordParam=&keywordFlag=&notSrcKeyword=&essCertChk=&depth2SelCode=&keywordBusiNm=&preferentialGbn=&rot3WorkYn=&regDateEndtParam={yesterday}&pfMatterPreferential=&pageIndex={pageIndex}&termContractMmcnt=&careerFrom=&laborHrShortYn=#scrollLoc"),
    JOB_POSTING_COUNT_PER_PAGE("JOB_POSTING_COUNT_PER_PAGE","50"),
    JOB_POSTING_COUNT("JOB_POSTING_COUNT","//*[@id=\"mForm\"]/div[2]/div/div[1]/div[1]/span/span"),

    //공고 정보
    TITLE("TITLE", "//*[@id=\"contents\"]/div/div/div/div[1]/div[3]/div[1]/div[1]/strong"),
    CONTENT("CONTENT", "//*[@id=\"tab-panel01\"]/div[1]/div"),

    //근무 정보
    PAY_INFO("PAY_INFO", "//*[@id=\"tab-panel02\"]/div/table/tbody/tr[1]/td[2]"),
    WORK_TIME("WORK_TIME","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[2]/td"),
    WORK_SCHEDULE("WORK_SCHEDULE","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[3]/td[2]"),

    //모집 정보
    RECRUITMENT_PROCESS("RECRUITMENT_PROCESS","//*[@id=\"tab-panel05\"]/div[2]/div/div[2]/p[1]"),
    REQUIRED_DOCUMENT("REQUIRED_DOCUMENT","//*[@id=\"tab-panel05\"]/div[2]/div/div[2]/p[2]"),
    APPLY_METHOD("APPLY_METHOD","//*[@id=\"tab-panel05\"]/div[2]/div/div[2]/p[1]"),
    APPLY_DEADLINE("APPLY_DEADLINE","//*[@id=\"tab-panel05\"]/div[2]/div/div[1]/div[1]/p"),
    CREATED_AT("CREATED_AT","//*[@id=\"contents\"]/div/div/div/div[1]/div[5]/div[11]/div[2]/table/tbody/tr[1]/td[1]"),

    //센터 정보
    CENTER_NAME("CENTER_NAME","//*[@id=\"contents\"]/div/div/div/div[1]/div[3]/div[1]/div[1]/p/strong"),
    CENTER_ADDRESS1("CENTER_ADDRESS1","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[5]/td/div[1]/p"),
    CENTER_ADDRESS2("CENTER_ADDRESs2","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[5]/td/div[1]/p"),
    CENTER_ADDRESS3("CENTER_ADDRESS3","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[5]/td/div[1]/p"),

    //노인 주소
    CLIENT_ADDRESS1("CLIENT_ADDRESS1","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[5]/td/div[1]/p"),
    CLIENT_ADDRESS2("CLIENT_ADDRESS2","//*[@id=\"tab-panel02\"]/div/table/tbody/tr[5]/td/div[1]/p"),

    //ChromDriver-Options
    HEADLESS("HEADLESS","--headless"),
    NO_SANDBOX("NO_SANDBOX","--no-sandbox"),
    DISABLE_DEV_SHM_USAGE("DISABLE_DEV_SHM_USAGE","--disable-dev-shm-usage"),
    DISABLE_GPU("DISABLE_GPU","--disable-gpu"),
    WINDOW_SIZE("WINDOW_SIZE","window-size=1920x1080"),
    DISABLE_SOFTWARE_RASTERIZER("DISABLE_SOFTWARE_RASTERIZER","--disable-software-rasterizer"),
    IGNORE_SSL_ERRORS("IGNORE_SSL_ERRORS","--ignore-ssl-errors=yes"),
    IGNORE_CERTIFICATE_ERRORS("IGNORE_CERTIFICATE_ERRORS","--ignore-certificate-errors");

    companion object {
        fun getChromOptions(): Array<String> {
            return arrayOf(
                HEADLESS.value,
                NO_SANDBOX.value,
                DISABLE_DEV_SHM_USAGE.value,
                DISABLE_GPU.value,
                WINDOW_SIZE.value,
                DISABLE_SOFTWARE_RASTERIZER.value,
                IGNORE_SSL_ERRORS.value,
                IGNORE_CERTIFICATE_ERRORS.value
            )
        }
    }

    fun getIntValue(): Int {
        return value.toInt()
    }
}