package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.model.AnswerResponse;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.util.StringConstants;

import java.util.List;

import io.reactivex.Observable;
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
            @Query(value = "filter", encoded = true) String filter);

    @GET(StringConstants.ANSWERS_END_POINT)
    Observable<AnswerResponse> getAnswersToQuestion(
            @Path("question_id") int id,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter);

    @GET("/2.2/search?order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wwYGT")
    Observable<List<QuestionsResponse>> getQuestionsWithTextInTitle(@Query("intitle") String inTitle);
}