package com.example.josycom.flowoverstack.util;

public interface StringConstants {

    String BASE_URL = "https://api.stackexchange.com";
    String QUESTIONS_END_POINT = "/2.2/questions";
    String SITE = "stackoverflow";

    int FIRST_PAGE = 1;
    int PAGE_SIZE = 50;
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String ORDER_DESCENDING = "desc";

    /**
     * Filter for getting upvote count in the response
     */
    String FILTER = "!Fc6b6oecdMxo)a9GWh_oLGugZw";

    String SORT_BY_ACTIVITY = "activity";
    String SORT_BY_VOTES = "votes";
    String SORT_BY_CREATION = "creation";
    String SORT_BY_HOT = "hot";

    String QUESTIONS_BY_ACTIVITY_API_SERVICE = "QuestionsByActivityApi";
    String QUESTIONS_BY_VOTES_API_SERVICE = "QuestionsByVoteApi";
    String QUESTIONS_BY_CREATION_API_SERVICE = "QuestionsByCreationApi";
    String QUESTIONS_BY_HOT_API_SERVICE = "QuestionsByHotApi";
}
