package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kakalra on 2/26/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieInfoObject  >
{
    private Context context;

    private static final String LOG_TAG=MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context,List<MovieInfoObject> movieList)
    {
        super(context,0,movieList);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MovieInfoObject movieInfoObject=getItem(position);
        if (convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie,parent,false);
        }

        ImageView iconView=(ImageView)convertView.findViewById(R.id.grid_item_movie_imageview);
        Picasso.with(context).load(movieInfoObject.moviePosterPath).into(iconView);

        return convertView;
    }
}
