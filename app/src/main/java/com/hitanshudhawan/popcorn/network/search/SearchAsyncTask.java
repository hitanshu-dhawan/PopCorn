package com.hitanshudhawan.popcorn.network.search;

import android.content.Context;
import android.os.AsyncTask;

import com.hitanshudhawan.popcorn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hitanshu on 15/8/17.
 */

public class SearchAsyncTask extends AsyncTask<String, Void, SearchResponse> {

    private Context mContext;
    private OnSearchDoneListener mOnSearchDoneListener;

    public SearchAsyncTask(Context mContext, OnSearchDoneListener listener) {
        this.mContext = mContext;
        mOnSearchDoneListener = listener;
    }

    @Override
    protected SearchResponse doInBackground(String... params) {

        try {
            String urlString = "https://api.themoviedb.org/3/" + "search/multi"
                    + "?"
                    + "api_key=" + mContext.getResources().getString(R.string.MOVIE_DB_API_KEY)
                    + "&"
                    + "query=" + params[0]
                    + "&"
                    + "page=" + params[1];
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            if (isCancelled()) return null;

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != 200) return null;

            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String jsonString = "";
            while (scanner.hasNext()) {
                jsonString += scanner.nextLine();
            }

            // Parse JSON
            JSONObject searchJsonObject = new JSONObject(jsonString);
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setPage(searchJsonObject.getInt("page"));
            searchResponse.setTotalPages(searchJsonObject.getInt("total_pages"));
            JSONArray resultsJsonArray = searchJsonObject.getJSONArray("results");
            List<SearchResult> searchResults = new ArrayList<>();
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                if (isCancelled()) return null;
                JSONObject result = (JSONObject) resultsJsonArray.get(i);
                SearchResult searchResult = new SearchResult();
                switch (result.getString("media_type")) {
                    case "movie":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPosterPath(result.getString("poster_path"));
                        searchResult.setName(result.getString("title"));
                        searchResult.setMediaType("movie");
                        searchResult.setOverview(result.getString("overview"));
                        searchResult.setReleaseDate(result.getString("release_date"));
                        break;
                    case "tv":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPosterPath(result.getString("poster_path"));
                        searchResult.setName(result.getString("name"));
                        searchResult.setMediaType("tv");
                        searchResult.setOverview(result.getString("overview"));
                        searchResult.setReleaseDate(result.getString("first_air_date"));
                        break;
                    case "person":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPosterPath(result.getString("profile_path"));
                        searchResult.setName(result.getString("name"));
                        searchResult.setMediaType("person");
                        searchResult.setOverview(null);
                        searchResult.setReleaseDate(null);
                        break;
                }
                searchResults.add(searchResult);
            }
            searchResponse.setResults(searchResults);

            return searchResponse;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(SearchResponse searchResponse) {
        mOnSearchDoneListener.onSearchDone(searchResponse);
    }

    public interface OnSearchDoneListener {
        void onSearchDone(SearchResponse searchResponse);
    }

}
