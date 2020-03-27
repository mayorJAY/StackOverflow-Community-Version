package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.model.AnswerResponse;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.util.StringConstants;

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

    @GET("/2.2/questions/{question_id}/answers?order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wzu0T")
    Call<AnswerResponse>getAnswersToQuestion(@Path("question_id") int id);
}
