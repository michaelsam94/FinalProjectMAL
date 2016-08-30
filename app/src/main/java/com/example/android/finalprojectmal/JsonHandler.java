package com.example.android.finalprojectmal;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by michael on 23/08/16.
 */
public class JsonHandler {

    JsonHandler() {

    }

    public JSONObject getJsonResult(String link) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        JSONObject jObject = null;
        String jsonResString;
        String line;
        try {

            URL url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            jsonResString = buffer.toString();
            jObject = new JSONObject(jsonResString);

        } catch (IOException e) {
            Log.e("FetchJsonFailed", "Error ", e);


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("JsonHandler", "Error closing stream", e);
                }
            }
        }
        return jObject;
    }

    public ArrayList<Movie> setJsonBasicDetailsToMovie(int page,String sortType) {
        ArrayList<Movie> movies = new ArrayList<>();
        final String API_KEY_PARAM = "api_key";
        final String API_PAGE_PARAM = "page";
        final String API_KEY = "";
        final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
        final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        JSONObject element;
        JSONArray jsonArray;
        JSONObject object;
        String baseURl = BASE_URL;
        baseURl += sortType;
        baseURl += "?";
        baseURl += API_KEY_PARAM;
        baseURl += "=";
        baseURl += API_KEY;
        baseURl += "&";
        baseURl += API_PAGE_PARAM;
        baseURl += "=";
        baseURl += page;

        Log.d("url",baseURl.toString());


        try {
            object = getJsonResult(baseURl.toString());
            jsonArray = object.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                Movie movieElement = new Movie();
                element = jsonArray.getJSONObject(i);
                movieElement.setTitle(element.getString("original_title"));
                movieElement.setRelaseDate(element.getString("release_date"));
                movieElement.setDescription(element.getString("overview"));
                movieElement.setRate(element.getString("vote_average"));
                movieElement.setPosterURL(BASE_IMAGE_URL + element.getString("poster_path"));

                String movieID;
                movieID = element.getString("id");
                movieElement.setId(movieID);

                String baseTrailerUrl = "http://api.themoviedb.org/3/movie/";
                baseTrailerUrl += movieID;
                baseTrailerUrl += "/videos?";
                baseTrailerUrl += API_KEY_PARAM;
                baseTrailerUrl += "=" + API_KEY;
                movieElement.setTrailerURL(baseTrailerUrl);

                String reviewURL = "http://api.themoviedb.org/3/movie/";
                reviewURL += movieID;
                reviewURL += "/reviews?";
                reviewURL += API_KEY_PARAM;
                reviewURL += "=" + API_KEY;
                movieElement.setReviewURL(reviewURL);
                movies.add(movieElement);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());
        }
        return movies;
    }

    public Movie setTrailerURLToMovie(Movie movie) {
        final String BASE_URL = "http://api.themoviedb.org/3/movie";
        final String YOUTUBE_PARAM_KEY = "v";
        final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?";

        JSONObject element;
        JSONArray jsonArray;
        JSONObject object = getJsonResult(movie.getTrailerURL());
        ;
        if (object != null) {
            try {
                jsonArray = object.getJSONArray("results");
                ArrayList<String> urls = new ArrayList<>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    element = jsonArray.getJSONObject(j);
                    if (element.getString("type").equals("Trailer")) {
                        String youTubeUrl = BASE_YOUTUBE_URL;
                        youTubeUrl += YOUTUBE_PARAM_KEY;
                        youTubeUrl += "=";
                        youTubeUrl += element.getString("key");
                        urls.add(youTubeUrl.toString());
                    }
                }
                movie.setYoutubeURLs(urls);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return movie;
    }

    public Movie setReviewsToMovie(Movie movie) {
        JSONObject element;
        JSONArray jsonArray;
        JSONObject object = getJsonResult(movie.getReviewURL());
        if (object != null) {
            try {
                jsonArray = object.getJSONArray("results");
                if (jsonArray != null) {
                    ArrayList<String> autors = new ArrayList<>();
                    ArrayList<String> contents = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        element = jsonArray.getJSONObject(j);
                        autors.add(element.getString("author"));
                        contents.add(element.getString("content"));
                    }
                    movie.setReviewsAutors(autors);
                    movie.setReviewsContents(contents);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movie;
    }




}
