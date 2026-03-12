package com.example.newsapiclient.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.newsapiclient.R
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.databinding.ItemNewsHeadlineBinding
import com.example.newsapiclient.presentation.util.Utils

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(
            p0: Article,
            p1: Article
        ): Boolean {
            return p0.url == p1.url
        }

        override fun areContentsTheSame(
            p0: Article,
            p1: Article
        ): Boolean {
            return p0 == p1
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): NewsViewHolder {
        val binding = ItemNewsHeadlineBinding
            .inflate(LayoutInflater.from(p0.context), p0, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(
        p0: NewsViewHolder,
        p1: Int
    ) {
        val article = differ.currentList[p1]
        p0.bind(article)
    }

    override fun getItemCount(): Int = differ.currentList.size

    class NewsViewHolder(val binding: ItemNewsHeadlineBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                newsTitleText.text = article.title
                newsSourceText.text = article.source.name
                val publishedAt = "\u2022 ${Utils.timeAgo(article.publishedAt)}"
                newsPublishedTimeText.text = publishedAt
                Glide.with(newsImage.context)
                    .load(article.urlToImage)
                    .transform(CenterCrop())
                    .placeholder(R.color.background_light)
                    .into(newsImage)
            }
        }

    }

}