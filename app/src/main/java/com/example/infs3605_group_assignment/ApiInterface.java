package com.example.infs3605_group_assignment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsModel> getNews(

            @Query("country") String country ,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<NewsModel> getNewsSearch(

            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );

}
