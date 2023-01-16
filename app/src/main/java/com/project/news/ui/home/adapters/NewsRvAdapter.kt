package com.project.news.ui.home.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.project.news.R
import com.project.news.data.News
import com.project.news.databinding.RvNewsItemBinding

class NewsRvAdapter (val context :Context,
                     val data : ArrayList<News> = arrayListOf(),
                     val rv : RecyclerView,
                     val listener : NewsItemClicked,
                     val isBookmarkPage :Boolean = false) : RecyclerView.Adapter<NewsRvAdapter.NewsRvViewHolder>() {

    inner class NewsRvViewHolder(var binding: RvNewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun replaceData(newData : ArrayList<News>) {
        data.clear()
        data.addAll(newData)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRvViewHolder {
        val view =RvNewsItemBinding.inflate(LayoutInflater.from(context))
        view.root.layoutParams = rv.layoutManager?.let {
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return NewsRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsRvViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        val view = holder.binding
        val item = data[position]

//        by default animation view is hidden
        view.newsLike.visibility = View.GONE

        if(isBookmarkPage) {
            setLike(view.newsLike,true)
        }

        Glide.with(context).load(item.urlToImage).placeholder(R.drawable.ic_background_news).into(view.newsImage)
        view.newsTitle.text = item.title

        view.newsImage.setOnClickListener {
            listener.newsClicked(item)
        }
        view.newsTitle.setOnClickListener {
            listener.newsClicked(item)
        }
        view.newsShare.setOnClickListener {
            listener.shareClicked(item)
        }
        view.newsLike.setOnClickListener {
            if(isBookmarkPage) {
//                If it is from bookmark page then give warning
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to remove this from bookmark.." )
                builder.setCancelable(true)
                builder.setPositiveButton("Yes") { dialog, id ->
                    setLike(view.newsLike,false)
                    listener.removeFromBookmarkClicked(item)
                    dialog.cancel()
                }
                builder.setNegativeButton("No") { dialog, id ->
                    dialog.cancel()
                }
                val alert: AlertDialog = builder.create()
                alert.show()
            } else {
                setLike(view.newsLike,false)
                listener.removeFromBookmarkClicked(item)
            }
        }
        view.newsLikeBackground.setOnClickListener {
            setLike(view.newsLike,true)
            listener.addToBookmark(item)
        }

     }

    fun setLike(likedAnimationView: LottieAnimationView, liked: Boolean) {
        if(liked) {
            likedAnimationView.visibility = View.VISIBLE
            likedAnimationView.playAnimation()
        } else {
            likedAnimationView.visibility = View.GONE
        }
    }

    override fun getItemCount() = data.size
}

interface NewsItemClicked{
    fun newsClicked(item: News)
    fun addToBookmark(item: News)
    fun removeFromBookmarkClicked(item: News)
    fun shareClicked(item: News)
}