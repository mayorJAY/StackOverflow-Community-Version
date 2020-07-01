package com.josycom.mayorjay.flowoverstack.util;

public interface StringConstants {

    String BASE_URL = "https://api.stackexchange.com";
    String QUESTIONS_END_POINT = "/2.2/questions";
    String ANSWERS_END_POINT = "/2.2/questions/{question_id}/answers";
    String SITE = "stackoverflow";

    int FIRST_PAGE = 1;
    int PAGE_SIZE = 50;
    long SPLASH_TIME = 5000L;
    String ORDER_DESCENDING = "desc";

    String QUESTION_FILTER = "!9Z(-wwYGT";
    String ANSWER_FILTER = "!9Z(-wzu0T";
    String API_KEY = "1ZLMY6ESrAkq5*odMs3zQw((";

    String SORT_BY_ACTIVITY = "activity";
    String SORT_BY_VOTES = "votes";
    String SORT_BY_CREATION = "creation";
    String SORT_BY_HOT = "hot";

    String EXTRA_QUESTION_TITLE = "question_title";
    String EXTRA_AVATAR_ADDRESS = "avatar_address";
    String EXTRA_QUESTION_DATE = "question_date";
    String EXTRA_QUESTION_ID = "question_id";
    String EXTRA_QUESTION_NAME = "question_name";
    String EXTRA_QUESTION_ANSWERS_COUNT = "question_answers";
    String EXTRA_QUESTION_FULL_TEXT = "question_full_text";
    String EXTRA_QUESTION_OWNER_LINK = "question_owner_link";
    String EXTRA_QUESTION_VOTES_COUNT = "question_votes";
    String WEBVIEW_EXTRA_OBJECT = "key.EXTRA_OBJC";

    String LOADING = "loading";
    String LOADED = "loaded";
    String FAILED = "failed";
    String NO_MATCHING_RESULT = "No matching result";

    String FRAGMENT_STATE = "fragment_state";
}