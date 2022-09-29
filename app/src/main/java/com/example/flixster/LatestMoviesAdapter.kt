package com.example.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LatestMoviesAdapter(
    private val movies: List<LatestMovie>,
    private val mListener: OnListFragmentInteractionListener?
    )
    : RecyclerView.Adapter<LatestMoviesAdapter.MovieViewHolder>()
    {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_latest_movies, parent, false)
        return MovieViewHolder(view)
    }

    /**
     * This inner class lets us refer to all the different View elements
     * (Yes, the same ones as in the XML layout files!)
     */
    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: LatestMovie? = null
        val mPoster: ImageView = mView.findViewById<View>(R.id.movieImage) as ImageView
        val mTitle: TextView = mView.findViewById<View>(R.id.title) as TextView
        val mOverview: TextView = mView.findViewById<View>(R.id.overview) as TextView

        override fun toString(): String {
            return mTitle.toString() + " '" + mOverview.text + "'"
        }
    }

    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        holder.mTitle.text = movie.title
        holder.mOverview.text = movie.overview

        Glide.with(holder.mView)
            .load("https://image.tmdb.org/t/p/w500"+movie.poster)
            .centerInside()
            .into(holder.mPoster)

        holder.mView.setOnClickListener {
            holder.mItem?.let { movie ->
                mListener?.onItemClick(movie)
            }
        }
    }

    /**
     * Remember: RecyclerView adapters require a getItemCount() method.
     */
    override fun getItemCount(): Int {
        return movies.size
    }
}