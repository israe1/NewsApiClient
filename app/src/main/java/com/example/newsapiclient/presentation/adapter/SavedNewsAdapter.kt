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
import com.example.newsapiclient.databinding.ItemSavedArticleBinding

class SavedNewsAdapter : RecyclerView.Adapter<SavedNewsAdapter.NewsViewHolder>() {

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
        val binding = ItemSavedArticleBinding
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

    inner class NewsViewHolder(val binding: ItemSavedArticleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                articleTitleText.text = article.title
                articleSourceText.text = article.source?.name
                articleOverviewText.text = article.description
                Glide.with(articleImage.context)
                    .load(article.urlToImage)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .placeholder(R.drawable.edittext_background)
                    .into(articleImage)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(article)
                    }
                }

                deleteArticle.setOnClickListener {
                    onDeleteItemListener?.let { it(article) }
                }

            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    private var onDeleteItemListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteItemListener(listener: (Article) -> Unit) {
        onDeleteItemListener = listener
    }
}