package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieInformationActivityFragment extends Fragment {

    public MovieInformationActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.fragment_movie_information, container, false);
        Intent intent=getActivity().getIntent();
        if(intent!=null && intent.hasExtra("movieObj"))
        {
            MovieInfoObject movieObject=intent.getExtras().getParcelable("movieObj");

            //loading image title into text view.
            TextView movieTitleTextView=((TextView)rootView.findViewById(R.id.movie_title_textView));
            movieTitleTextView.setText(movieObject.movieOriginalTitle);

            //loading image into image view.
            ImageView moviePosterView=((ImageView)rootView.findViewById(R.id.movie_poster_imageView));
            Picasso.with(getContext()).load(movieObject.moviePosterPath).into(moviePosterView);

            //loading rating into text view.
            TextView movieRatingTextView=((TextView)rootView.findViewById(R.id.movie_rating_textView));
            movieRatingTextView.setText("Rating : "+movieObject.movieRating+"/10");

            //loading overview into text view.
            TextView movieOverviewTextView=((TextView)rootView.findViewById(R.id.movie_overview_textView));
            movieOverviewTextView.setText(movieObject.movieOverview);

            //loading release date into text view
            TextView movieReleaseDateTextView=((TextView)rootView.findViewById(R.id.movie_releaseDate_textView));
            movieReleaseDateTextView.setText("Release Date : "+movieObject.movieReleaseDate);
        }
        return rootView;
    }
}
