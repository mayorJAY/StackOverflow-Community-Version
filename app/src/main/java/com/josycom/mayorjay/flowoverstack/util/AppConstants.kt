package com.josycom.mayorjay.flowoverstack.util

interface AppConstants {
    companion object {
        const val BASE_URL = "https://api.stackexchange.com"
        const val QUESTIONS_END_POINT = "/2.3/questions"
        const val ANSWERS_END_POINT = "/2.3/questions/{question_id}/answers"
        const val SEARCH_END_POINT = "/2.3/search?pagesize=100&order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wwYGT&key=1ZLMY6ESrAkq5*odMs3zQw(("
        const val TAGS_END_POINT = "/2.3/tags?order=desc&sort=popular&site=stackoverflow"
        const val SITE = "stackoverflow"
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 50
        const val SPLASH_TIME = 1500L
        const val ORDER_DESCENDING = "desc"
        const val QUESTION_FILTER = "!9Z(-wwYGT"
        const val ANSWER_FILTER = "!9Z(-wzu0T"
        const val API_KEY = "1ZLMY6ESrAkq5*odMs3zQw(("//YOUR_API_KEY
        const val SORT_BY_ACTIVITY = "activity"
        const val SORT_BY_VOTES = "votes"
        const val SORT_BY_CREATION = "creation"
        const val SORT_BY_HOT = "hot"
        const val EXTRA_QUESTION_TITLE = "question_title"
        const val EXTRA_AVATAR_ADDRESS = "avatar_address"
        const val EXTRA_QUESTION_DATE = "question_date"
        const val EXTRA_QUESTION_ID = "question_id"
        const val EXTRA_QUESTION_NAME = "question_name"
        const val EXTRA_QUESTION_ANSWERS_COUNT = "question_answers"
        const val EXTRA_QUESTION_FULL_TEXT = "question_full_text"
        const val EXTRA_QUESTION_OWNER_LINK = "question_owner_link"
        const val EXTRA_QUESTION_VOTES_COUNT = "question_votes"
        const val WEBVIEW_EXTRA_OBJECT = "key.EXTRA_OBJC"
        const val LOADING = "loading"
        const val LOADED = "loaded"
        const val FAILED = "failed"
        const val NO_MATCHING_RESULT = "No matching result"
        const val FRAGMENT_STATE = "fragment_state"
    }
}