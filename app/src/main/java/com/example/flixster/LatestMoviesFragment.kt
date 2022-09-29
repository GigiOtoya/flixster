package com.example.flixster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"


/**
 * A simple [Fragment] subclass.
 * Use the [LatestMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        params["api-key"] = API_KEY

        // Using the client, perform the HTTP request
        client [
            "https://api.themoviedb.org/3/movie/now_playing",
            params,
            object: JsonHttpResponseHandler() {
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

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers?,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()
                    val models : List<LatestMovie>? = null
                    recyclerView.adapter = models?.let { LatestMoviesAdapter(it, this@LatestMoviesFragment) }
                    Log.d("LatestMoviesFragment", "response successful")
                }

            }
        ]
    }

    override fun onItemClick(item: LatestMovie) {
        TODO("Not yet implemented")
    }
}