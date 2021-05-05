package com.example.swapiassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Person> people = new ArrayList<Person>();
        Person todd = new Person();
        todd.setName("Todd");
        Person joe = new Person();
        joe.setName("Joe");
        people.add(todd);
        people.add(joe);

        RecyclerView peopleRecyclerView = findViewById(R.id.peopleRecyclerView);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter(this, people);
        peopleRecyclerView.setAdapter(personAdapter);

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