package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.databinding.FragmentSavedNewsBinding
import com.example.newsapiclient.presentation.adapter.SavedNewsAdapter
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: SavedNewsAdapter
    private lateinit var binding: FragmentSavedNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as MainActivity).viewModel
        adapter = (requireActivity() as MainActivity).savedNewsAdapter
        adapter.setOnItemClickListener {
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToNewsDetailFragment(it)
            findNavController().navigate(action)
        }
        adapter.setOnDeleteItemListener {
            deleteArticle(it)
        }
        binding = FragmentSavedNewsBinding.bind(view)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val divider = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)?.let {
            divider.setDrawable(it)
        }
        binding.savedNewsList.apply {
            adapter = this@SavedNewsFragment.adapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(divider)
        }
        displayList()
    }

    private fun displayList() {
        viewModel.getSavedArticles().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                showEmptyList(true)
            } else {
                showEmptyList(false)
                adapter.differ.submitList(it)
            }
        })
    }

    private fun deleteArticle(article: Article) {
        viewModel.deleteArticle(article)
        Snackbar.make(
            binding.root,
            getString(R.string.article_deleted_successfully),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showEmptyList(show: Boolean) {
        binding.layoutEmptyList.visibility = if (show) View.VISIBLE else View.GONE
        binding.savedNewsList.visibility = if (show) View.GONE else View.VISIBLE
    }

}