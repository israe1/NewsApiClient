package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.databinding.FragmentNewsBinding
import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.util.Utils
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel
import com.google.android.material.chip.Chip

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
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
        binding = FragmentNewsBinding.bind(view)
        initChipCategories()
        initRecyclerView()
    }

    private fun initChipCategories() {
        Utils.getCategoryList().forEachIndexed { index, category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true

                setChipBackgroundColorResource(R.color.chip_background_selector)
                setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_text_selector))

                shapeAppearanceModel = shapeAppearanceModel
                    .toBuilder()
                    .setAllCornerSizes(resources.getDimension(R.dimen._16dp))
                    .build()

                if (index == 0) isChecked = true
            }

            binding.categoryChipGroup.addView(chip)
        }

        binding.categoryChipGroup.setOnCheckedStateChangeListener { group, _ ->
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                handleChipClick(i, chip, chip.isChecked)
            }
        }
    }

    private fun handleChipClick(index: Int, chip: Chip, isChecked: Boolean) {
        if (isChecked) {
            getNews(if (index == 0) "" else chip.text.toString().lowercase())
            chip.animate()
                .scaleX(0.95f)
                .translationZ(16f)
                .setDuration(100)
                .start()
        } else {
            chip.animate()
                .scaleX(1f)
                .translationZ(0f)
                .setDuration(100)
                .start()
        }
    }

    private fun initRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.apply {
            newsHeadLineList.adapter = newsAdapter
            newsHeadLineList.layoutManager = LinearLayoutManager(activity)
        }
        displayNewsList()
    }

    private fun displayNewsList() {
        getNews("")
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

    private fun getNews(category: String) {
        viewModel.getNewsHeadlines(country, category, page)
    }

    private fun showProgressBar() {
        binding.newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.newsProgressBar.visibility = View.INVISIBLE
    }

}