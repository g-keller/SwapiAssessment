package com.example.swapiassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements PersonAdapter.ItemClickListener{

    private PersonAdapter personAdapter;
    SwapiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Person> people = new ArrayList<Person>();

        RecyclerView peopleRecyclerView = findViewById(R.id.peopleRecyclerView);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter(this, people);
        personAdapter.setClickListener(this);
        peopleRecyclerView.setAdapter(personAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(SwapiService.class);

        populatePeoplePages();

    }

    private void populatePeoplePages() {
        // must get first page to have access to the count for calculating total number of pages
        api.listPeople().enqueue(new Callback<PersonList>() {
            @Override
            public void onResponse(Call<PersonList> call, Response<PersonList> response) {
                int totalPeople = response.body().getCount();
                // be sure not to lose the fractional part of the quotient
                double maxPeoplePerPage = 10;
                int numberOfPages = (int) Math.ceil(totalPeople / maxPeoplePerPage);
                List<Person> peopleReturned = response.body().getPeople();
                for (Person person : peopleReturned) {
                    personAdapter.addPerson(person);
                    Log.d("API", person.getName());
                }
                for (int i = 2; i <= numberOfPages; i++) {
                    populatePeoplePage(i);
                }
            }

            @Override
            public void onFailure(Call<PersonList> call, Throwable t) {
                Log.d("API EXCEPTION", t.toString());
            }
        });
    }

    private void populatePeoplePage(int pageNumber) {
        api.listPeopleByPage(pageNumber).enqueue(new Callback<PersonList>() {
            @Override
            public void onResponse(Call<PersonList> call, Response<PersonList> response) {
                List<Person> peopleReturned = response.body().getPeople();
                for (Person person : peopleReturned) {
                    personAdapter.addPerson(person);
                    Log.d("API", person.getName());
                }
            }

            @Override
            public void onFailure(Call<PersonList> call, Throwable t) {
                Log.d("API EXCEPTION", t.toString());
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Person person = personAdapter.getPerson(position);
        Toast.makeText(this, person.getEyeColor(), Toast.LENGTH_SHORT).show();
    }

}