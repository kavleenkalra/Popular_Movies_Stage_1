package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakalra on 2/26/2016.
 */
public class MovieInfoObject implements Parcelable
{
    int movieId;
    String movieOriginalTitle;
    String moviePosterPath;
    String movieOverview;
    String movieReleaseDate;
    String movieRating;

    public MovieInfoObject(int movieId,String movieOriginalTitle,String moviePosterPath,String movieOverview,String movieReleaseDate,String movieRating)
    {
        this.movieId=movieId;
        this.movieOriginalTitle=movieOriginalTitle;
        this.moviePosterPath="http://image.tmdb.org/t/p/w185/"+moviePosterPath;
        this.movieOverview=movieOverview;
        this.movieReleaseDate=movieReleaseDate;
        this.movieRating=movieRating;
    }

    private MovieInfoObject(Parcel in)
    {
        movieId=in.readInt();
        movieOriginalTitle=in.readString();
        moviePosterPath=in.readString();
        movieOverview=in.readString();
        movieReleaseDate=in.readString();
        movieRating= in.readString();
    }

    public String toString()
    {
        return movieId+"--"+movieOriginalTitle+"--"+moviePosterPath+"--"+movieOverview+"--"+movieReleaseDate+"--"+movieRating;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i)
    {
        p.writeInt(movieId);
        p.writeString(movieOriginalTitle);
        p.writeString(moviePosterPath);
        p.writeString(movieOverview);
        p.writeString(movieReleaseDate);
        p.writeString(movieRating);
    }

    public static final Parcelable.Creator<MovieInfoObject> CREATOR=new Parcelable.Creator<MovieInfoObject>()
    {
        @Override
        public MovieInfoObject createFromParcel(Parcel source) {
            return new MovieInfoObject(source);
        }

        @Override
        public MovieInfoObject[] newArray(int i) {
            return new MovieInfoObject[i];
        }
    };
}
