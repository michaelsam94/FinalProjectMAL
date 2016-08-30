package com.example.android.finalprojectmal;

/**
 * Created by michael on 11/08/16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity{


    public static final String TAG = "MovieDetailsActivity";
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("movie");

        movie = new Movie(extras);
        if(savedInstanceState == null) {
            if (extras != null) {

                MovieDetailsFragment fragment = new MovieDetailsFragment();
                fragment.setArguments(extras);
                getFragmentManager().beginTransaction().add(R.id.fragmentDetailsContainer, fragment).commit();

            }
        }


    }

}