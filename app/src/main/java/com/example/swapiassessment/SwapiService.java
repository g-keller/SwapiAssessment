package com.example.swapiassessment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SwapiService {

    @GET("people/")
    Call<PersonList> listPeople();

}
