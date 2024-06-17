package com.example.movie2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private String uriString = "content://com.example.movie2.MovieProvider/movie";

    private static final String TAG = "SearchFragment";
    private static final String API_KEY = "ae17eddf49d4c524fe22bb813b0f9463";
    private static final String BASE_URL = "http://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new MovieAdapter();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(requireContext());


        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearMovie();
                adapter.clearItems();  
                makeRequest(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }

    private void makeRequest(String query) {
        String url = BASE_URL + "?key=" + API_KEY + "&movieNm=" + query;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.toString());
                    }
                }
        );

        if (requestQueue != null) {
            request.setShouldCache(false);
            requestQueue.add(request);
        }
    }

    private void processResponse(String response) {
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);
        insertMoviesToDatabase(movieList);

        for(int i = 0; i < movieList.movieListResult.movieList.size(); i++) {
            Movie movie = movieList.movieListResult.movieList.get(i);

            adapter.addItem(movie);

        }

        adapter.notifyDataSetChanged();


    }

    private void insertMoviesToDatabase(MovieList movieList) {
        Uri uri = new Uri.Builder().build().parse(uriString);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        if(movieList.movieListResult.movieList.size() != 0) {
            for (int i = 0; i < movieList.movieListResult.movieList.size(); i++) {
                Movie movie = movieList.movieListResult.movieList.get(i);
                ContentValues values = new ContentValues();
                values.put("title", movie.movieNm);
                values.put("openDt", movie.openDt);
                values.put("nationAlt", movie.nationAlt);
                values.put("genreAlt", movie.genreAlt);
                values.put("director", getDirectorNames(movie.directors));

                uri = getActivity().getContentResolver().insert(uri, values);
            }
        }
    }

    private String getDirectorNames(ArrayList<Director> directors) {
        StringBuilder directorNames = new StringBuilder();

        for (int i = 0; i < directors.size(); i++) {
            Director director = directors.get(i);
            directorNames.append(director.peopleNm);

            if (i < directors.size() - 1) {
                directorNames.append(", ");
            }
        }

        return directorNames.toString();
    }

    private void clearMovie() {
        Uri uri = Uri.parse(uriString);
        int count = requireActivity().getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Deleted " + count + " movies from the database.");
    }


}