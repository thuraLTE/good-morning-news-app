package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.HeadlinesItemsBinding
import com.example.newsapp.models.Article
import java.io.Serializable

class HeadlineAdapter(
    private val listener: OnNewsHeadlineClicked
) : RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>(), Serializable {

    private var articleList = ArrayList<Article>()

    fun setArticleList(articles: List<Article>?) {
        if (articles?.isNotEmpty() == true) {
            articleList.apply {
                clear()
                addAll(articles)
            }
            notifyDataSetChanged()
        }
    }

    class HeadlineViewHolder(private val binding: HeadlinesItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                headlineTitle.text = article.title
                headlineSourceName.text = article.source?.name

                if (article.urlToImage != null)
                    Glide.with(itemView)
                        .load(article.urlToImage)
                        .placeholder(R.drawable.loading_animation)
                        .into(headlineImg)
                else
                    headlineImg.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeadlineViewHolder {
        val itemView = HeadlinesItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HeadlineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val articleAtCurrentPosition = articleList[position]
        holder.bind(articleAtCurrentPosition)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(articleAtCurrentPosition)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    interface OnNewsHeadlineClicked {
        fun onItemClicked(article: Article)
    }
}