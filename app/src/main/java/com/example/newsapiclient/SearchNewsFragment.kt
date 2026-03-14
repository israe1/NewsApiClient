package com.example.newsapiclient

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.databinding.FragmentSearchNewsBinding
import com.example.newsapiclient.presentation.adapter.SearchedNewsAdapter
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel

class SearchNewsFragment : Fragment() {
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: SearchedNewsAdapter
    private val country = "us"
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        newsAdapter = (activity as MainActivity).searchedNewsAdapter
        newsAdapter.setOnItemClickListener {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToNewsDetailFragment(it)
            findNavController().navigate(action)
        }
        (requireActivity() as MainActivity)
            .setSupportActionBar(view.findViewById(R.id.searchNewsToolbar))
        binding.apply {
            searchNewsToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            searchNewsInput.apply {
                setOnEditorActionListener { _, _, _ ->
                    val searchQuery = searchNewsInput.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        viewModel.searchNewsHeadlines(searchQuery, country, page)
                    }
                    true
                }
                requestFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(searchNewsInput, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.newsHeadLineList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        displayNewsList()
    }

    private fun displayNewsList() {
        viewModel.searchedNewsHeadlines.observe(viewLifecycleOwner, { response ->
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
        binding.newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.newsProgressBar.visibility = View.INVISIBLE
    }
}