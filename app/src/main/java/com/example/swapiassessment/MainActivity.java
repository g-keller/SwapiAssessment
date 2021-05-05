package com.example.swapiassessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
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

public class MainActivity extends AppCompatActivity implements PersonAdapter.ItemClickListener{

    private RecyclerView peopleRecyclerView;
    private PersonAdapter personAdapter;
    SwapiService api;
    MediaPlayer player;

    ImageButton playButton;
    ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Person> people = new ArrayList<Person>();

        player = MediaPlayer.create(getApplicationContext(), R.raw.swintro);
        player.setLooping(true);

        peopleRecyclerView = findViewById(R.id.peopleRecyclerView);
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

    private void startScrolling() {
        LinearSmoothScroller scroller = new LinearSmoothScroller(peopleRecyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics metrics) {
                return 1;
            }

            @Override
            protected int calculateTimeForDeceleration(int x) {
                return 0;
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