package com.example.flixster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject


private const val API_KEY = "1cd9876f19987127737bfc0f7906fc0d"

class LatestMoviesFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_latest_movies_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        updateAdapter(progressBar, recyclerView)

        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // Using the client, perform the HTTP request
        client [
                "https://api.themoviedb.org/3/movie/now_playing",
                params,
                object: JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()
                    // Filtering JSON
                    val results : JSONArray = json.jsonObject.get("results") as JSONArray
                    val movies : String = results.toString()
                    val gson = Gson()
                    val arrayMovieType = object : TypeToken<List<LatestMovie>>() {}.type
                    // Create a list of movies
                    val models : List<LatestMovie> = gson.fromJson(movies, arrayMovieType)
                    recyclerView.adapter = LatestMoviesAdapter(models, this@LatestMoviesFragment)


                    //recyclerView.adapter = models?.let { LatestMoviesAdapter(it, this@LatestMoviesFragment) }
                    Log.d("LatestMoviesFragment", "response successful")
                    Log.d("DEBUG_ARRAY", json.jsonObject.toString())
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("LatestMoviesFragment", errorResponse)
                    }
                }

            }
        ]
    }

    override fun onItemClick(item: LatestMovie) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}