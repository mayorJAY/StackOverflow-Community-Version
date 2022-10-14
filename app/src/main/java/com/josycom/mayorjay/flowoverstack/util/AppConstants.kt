package com.josycom.mayorjay.flowoverstack.util

interface AppConstants {
    companion object {
        const val BASE_URL = "https://api.stackexchange.com"
        const val QUESTIONS_END_POINT = "/2.3/questions"
        const val ANSWERS_END_POINT = "/2.3/questions/{question_id}/answers"
        const val SEARCH_END_POINT = "/2.3/search?order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wwYGT&key=1ZLMY6ESrAkq5*odMs3zQw(("
        const val TAGS_END_POINT = "/2.3/tags?order=desc&sort=popular&site=stackoverflow"
        const val PLAY_STORE_URL =
            "https://play.google.com/store/apps/details?id=com.josycom.mayorjay.flowoverstack"
        const val SITE = "stackoverflow"
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 50
        const val SEARCH_PAGE_SIZE = 100
        const val SPLASH_TIME = 1000L
        const val ORDER_DESCENDING = "desc"
        const val QUESTION_FILTER = "!9Z(-wwYGT"
        const val ANSWER_FILTER = "!9Z(-wzu0T"
        const val API_KEY = "YOUR_API_KEY"
        const val SORT_BY_ACTIVITY = "activity"
        const val SORT_BY_VOTES = "votes"
        const val SORT_BY_CREATION = "creation"
        const val SORT_BY_HOT = "hot"
        const val EXTRA_QUESTION_KEY = "key.question_extra"
        const val WEBVIEW_EXTRA_OBJECT = "key.EXTRA_OBJC"
        const val LOADING = "loading"
        const val LOADED = "loaded"
        const val FAILED = "failed"
        const val NO_MATCHING_RESULT = "No matching result"
        const val FRAGMENT_STATE = "fragment_state"
    }
}