package com.josycom.mayorjay.flowoverstack.network

import com.josycom.mayorjay.flowoverstack.util.AppConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(AppConstants.QUESTIONS_END_POINT)
    suspend fun getQuestionsForAll(
            @Query("page") page: Int,
            @Query("pagesize") pageSize: Int,
            @Query("order") order: String?,
            @Query("sort") sortCondition: String?,
            @Query("site") site: String?,
            @Query("tagged") tagged: String?,
            @Query(value = "filter", encoded = true) filter: String?,
            @Query("key") siteKey: String?): QuestionsResponse

    @GET(AppConstants.ANSWERS_END_POINT)
    suspend fun getAnswersToQuestion(
            @Path("question_id") id: Int,
            @Query("order") order: String?,
            @Query("sort") sortCondition: String?,
            @Query("site") site: String?,
            @Query(value = "filter", encoded = true) filter: String?,
            @Query("key") siteKey: String?): AnswerResponse

    @GET(AppConstants.SEARCH_END_POINT)
    fun getQuestionsWithTextInTitle(@Query("intitle") inTitle: String?): Call<QuestionsResponse?>

    @GET(AppConstants.TAGS_END_POINT)
    suspend fun getAllPopularTags(
            @Query("page") page: Int,
            @Query("pagesize") pageSize: Int,
            @Query("inname") inName: String?,
            @Query("key") siteKey: String?): TagsResponse
}