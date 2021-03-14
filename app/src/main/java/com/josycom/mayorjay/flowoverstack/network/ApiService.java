package com.josycom.mayorjay.flowoverstack.network;

import com.josycom.mayorjay.flowoverstack.model.AnswerResponse;
import com.josycom.mayorjay.flowoverstack.model.QuestionsResponse;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET(AppConstants.QUESTIONS_END_POINT)
    Call<QuestionsResponse> getQuestionsForAll(
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter,
            @Query("key") String siteKey);

    @GET(AppConstants.ANSWERS_END_POINT)
    Call<AnswerResponse> getAnswersToQuestion(
            @Path("question_id") int id,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter,
            @Query("key") String siteKey);

    @GET(AppConstants.SEARCH_END_POINT)
    Call<QuestionsResponse> getQuestionsWithTextInTitle(
            @Query("intitle") String inTitle);

}