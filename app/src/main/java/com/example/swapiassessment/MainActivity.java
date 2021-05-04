package com.example.swapiassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SwapiService api = retrofit.create(SwapiService.class);

        api.listPeople().enqueue(new Callback<PersonList>() {
            @Override
            public void onResponse(Call<PersonList> call, Response<PersonList> response) {
                List<Person> people = response.body().getPeople();
                for (Person person : people) {
                    Log.d("API", person.getName());
                }
            }

            @Override
            public void onFailure(Call<PersonList> call, Throwable t) {
                Log.d("API EXCEPTION", t.toString());
            }
        });
    }
}