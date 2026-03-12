package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.databinding.FragmentNewsBinding
import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private val country = "us"
    private var page = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        fragmentNewsBinding = FragmentNewsBinding.bind(view)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        newsAdapter = NewsAdapter()
        fragmentNewsBinding.apply {
            newsHeadLineList.adapter = newsAdapter
            newsHeadLineList.layoutManager = LinearLayoutManager(activity)
        }
        displayNewsList()
    }

    private fun displayNewsList() {
        viewModel.getNewsHeadlines(country, page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        fragmentNewsBinding.newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        fragmentNewsBinding.newsProgressBar.visibility = View.INVISIBLE
    }

}