package com.example.swapiassessment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SwapiService {

    @GET("people/")
    Call<PersonList> listPeople();

    @GET("people/")
    Call<PersonList> listPeopleByPage(@Query("page") int page);

}
