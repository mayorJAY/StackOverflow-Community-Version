package com.josycom.mayorjay.flowoverstack.network;

import com.josycom.mayorjay.flowoverstack.model.AnswerResponse;
import com.josycom.mayorjay.flowoverstack.model.QuestionsResponse;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET(StringConstants.QUESTIONS_END_POINT)
    Call<QuestionsResponse> getQuestionsForAll(
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter,
            @Query("key") String siteKey);

    @GET(StringConstants.ANSWERS_END_POINT)
    Call<AnswerResponse> getAnswersToQuestion(
            @Path("question_id") int id,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter,
            @Query("key") String siteKey);

    @GET("/2.2/search?pagesize=100&order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wwYGT&key=1ZLMY6ESrAkq5*odMs3zQw((")
    Call<QuestionsResponse> getQuestionsWithTextInTitle(@Query("intitle") String inTitle);

}