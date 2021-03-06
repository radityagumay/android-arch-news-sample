package com.akshay.newsapp.news.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akshay.newsapp.R
import com.akshay.newsapp.core.utils.inflate
import com.akshay.newsapp.news.model.NewsArticles
import com.akshay.newsapp.news.ui.model.NewsAdapterEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_news_article.view.*

/**
 * The News adapter to show the news in a list.
 */
class NewsArticlesAdapter(
    private val listener: (NewsAdapterEvent) -> Unit
) : ListAdapter<NewsArticles, NewsArticlesAdapter.NewsHolder>(DIFF_CALLBACK) {

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    /**
     * Inflate the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsHolder(parent.inflate(R.layout.row_news_article))

    /**
     * Bind the view with the data
     */
    override fun onBindViewHolder(newsHolder: NewsHolder, position: Int) = newsHolder.bind(getItem(position), listener)

    /**
     * Override [ListAdapter.submitList] and use [AsyncListDiffer.submitList]
     */
    override fun submitList(list: MutableList<NewsArticles>?) {
        differ.submitList(list)
    }

    /**
     * Override [ListAdapter.getItem] and use [AsyncListDiffer.getItem]
     */
    override fun getItem(position: Int): NewsArticles {
        return differ.currentList.get(position)
    }

    /**
     * Override [ListAdapter.getItemCount] and use [AsyncListDiffer.getItemCount]
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * View Holder Pattern
     */
    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Binds the UI with the data and handles clicks
         */
        fun bind(newsArticle: NewsArticles, listener: (NewsAdapterEvent) -> Unit) = with(itemView) {
            newsTitle.text = newsArticle.title
            newsAuthor.text = newsArticle.author
            //TODO: need to format date
            //tvListItemDateTime.text = getFormattedDate(newsArticle.publishedAt)
            newsPublishedAt.text = newsArticle.publishedAt
            Glide.with(context)
                .load(newsArticle.urlToImage)
                .apply(RequestOptions()
                    .placeholder(R.drawable.tools_placeholder)
                    .error(R.drawable.tools_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(newsImage)
            setOnClickListener { listener(NewsAdapterEvent.ClickEvent) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsArticles>() {
            override fun areItemsTheSame(oldItem: NewsArticles, newItem: NewsArticles): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: NewsArticles, newItem: NewsArticles): Boolean = oldItem == newItem
        }
    }
}