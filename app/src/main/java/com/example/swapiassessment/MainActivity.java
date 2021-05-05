package com.example.swapiassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements PersonAdapter.PersonClickListener {

    private RecyclerView peopleRecyclerView;
    private PersonAdapter personAdapter;

    private ImageButton playButton;
    private ImageButton pauseButton;

    private SwapiService api;

    private MediaPlayer player;

    List<Person> people = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureRecyclerView();
        api = getApiRetrofit();
        populatePeople();
        configureSlowScrollFeature();
    }

    @Override
    public void onItemClick(View view, int position) {
        Person person = personAdapter.getPerson(position);
        Toast.makeText(this, person.getEyeColor(), Toast.LENGTH_SHORT).show();
    }

    // helpers

    // In a normal application, some of this logic may be better placed in separate files (models,
    // viewmodels, presenters, controllers, etc. depending on the chosen architecture). Since this
    // project is simple and won't grow much larger, I've elected to leave the API accesses here
    // in the main activity.

    private void configureRecyclerView() {
        peopleRecyclerView = findViewById(R.id.peopleRecyclerView);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter(this, people);
        personAdapter.setClickListener(this);
        peopleRecyclerView.setAdapter(personAdapter);
    }

    private SwapiService getApiRetrofit() {
        // Use retrofit to automate making API requests (and handling the responses)
        // so that I can spend more time on features.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SwapiService.class);
    }

    private void populatePeople() {
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
                Log.e("SWAPI Call Failed", t.toString());
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
                Log.e("SWAPI Call Failed", t.toString());
            }
        });
    }

    private void configureSlowScrollFeature() {
        player = MediaPlayer.create(getApplicationContext(), R.raw.swintro);
        // replay Star Wars intro song if the slow scroll isn't complete yet
        player.setLooping(true);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                player.start();
                peopleRecyclerView.suppressLayout(true);
                startScrolling();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                player.pause();
                peopleRecyclerView.suppressLayout(false);
                peopleRecyclerView.stopScroll();
            }
        });
    }

    private void startScrolling() {
        // LinearSmoothScroller allows us control over the speed at which the RecyclerView
        // autoscrolls. This is necessary since the default speed is much too fast to mimic
        // the Star Wars intro text.
        LinearSmoothScroller scroller = new LinearSmoothScroller(peopleRecyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics metrics) {
                // 10ms per pixel gets us pretty close to the speed at which the Star Wars intro text flows
                float speedPerPixel = 10;
                return speedPerPixel;
            }

            @Override
            protected int calculateTimeForDeceleration(int x) {
                // remove deceleration time since the Star Wars intro text flow does not decelerate
                int noDecelerationTime = 0;
                return noDecelerationTime;
            }

            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                player.pause();
                peopleRecyclerView.suppressLayout(false);
            }
        };

        scroller.setTargetPosition(personAdapter.getItemCount() - 1);
        peopleRecyclerView.getLayoutManager().startSmoothScroll(scroller);
    }

}