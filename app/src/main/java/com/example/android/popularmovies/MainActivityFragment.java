package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    private final String MAIN_TAG=MainActivityFragment.class.getSimpleName();

    private MovieAdapter movieAdapter;
    MovieInfoObject movieInfoArray[];

    private ArrayList<MovieInfoObject> movieList;

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null || !savedInstanceState.containsKey("movies"))
        {
            movieList=new ArrayList<MovieInfoObject>();
        }
        else
        {
            movieList=savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter=new MovieAdapter(getActivity(), movieList);
        GridView gridView=(GridView)rootView.findViewById(R.id.mainGrid);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfoObject movieObject=movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieInformationActivity.class).putExtra("movieObj",movieObject);
                startActivity(intent);
                movieAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    //this function is responsible for calling the background thread functions.
    private void updateMovies()
    {
        FetchMovieTask movieTask=new FetchMovieTask();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType=prefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_way_default));
        movieTask.execute(sortType);//sortType parameter signifies which sorting criteria to use.
    }

    public class FetchMovieTask extends AsyncTask<String,Void,MovieInfoObject[]>
    {
        private final String  LOG_TAG=FetchMovieTask.class.getSimpleName();

        private MovieInfoObject[] getMovieDataFromJsonStr(String movieJsonStr)throws JSONException
        {
            final String OWM_RESULT="results";
            final String OWM_ID="id";
            final String OWM_TITLE="original_title";
            final String OWM_OVERVIEW="overview";
            final String OWM_RELEASEDATE="release_date";
            final String OWM_POSTERPATH="poster_path";
            final String OWM_RATING="vote_average";

            JSONObject movieJson=new JSONObject(movieJsonStr);
            JSONArray jsonMovieArray=movieJson.getJSONArray(OWM_RESULT);

            movieInfoArray=new MovieInfoObject[jsonMovieArray.length()];

            for (int i=0;i<jsonMovieArray.length();i++)
            {
                JSONObject movieObject=jsonMovieArray.getJSONObject(i);
                int id=movieObject.getInt(OWM_ID);
                String title=movieObject.getString(OWM_TITLE);
                String posterPath=movieObject.getString(OWM_POSTERPATH);
                String overview=movieObject.getString(OWM_OVERVIEW);
                String releaseDate=movieObject.getString(OWM_RELEASEDATE);
                String rating=movieObject.getString(OWM_RATING);

                movieInfoArray[i]=new MovieInfoObject(id,title,posterPath,overview,releaseDate,rating);

            }
            return movieInfoArray;
        }

        @Override
        protected MovieInfoObject[] doInBackground(String... params)
        {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;

            String movieJsonStr=null;

            String sort_criteria=params[0];
            String apiKey="";

            try
            {
                final String BASE_URL="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY="sort_by";
                final String API_KEY="api_key";

                Uri builtUri=Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY,sort_criteria)
                        .appendQueryParameter(API_KEY,apiKey)
                        .build();

                URL url=new URL(builtUri.toString());

                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if (inputStream==null)
                    return null;

                reader=new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line=reader.readLine())!=null)
                    buffer.append(line+"\n");

                if (buffer.length()==0)
                    return null;

                movieJsonStr=buffer.toString();
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG,"Error",e);
                return null;
            }
            finally
            {
                if (urlConnection!=null)
                    urlConnection.disconnect();
                if (reader!=null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        Log.e(LOG_TAG,"Error closing stream",e);
                    }
                }
            }

            try
            {
                return getMovieDataFromJsonStr(movieJsonStr);
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG,"Error in parsing Json string",e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieInfoObject[] result)
        {
            if(result!=null)
            {
                movieAdapter.clear();
                Log.v(LOG_TAG,"in postExecute");
                for (MovieInfoObject obj:result)
                    movieAdapter.add(obj);
            }
        }
    }
}
