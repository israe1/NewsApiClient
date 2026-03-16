package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.databinding.FragmentNewsDetailBinding
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsDetailBinding.bind(view)
        viewModel = (requireActivity() as MainActivity).viewModel
        (requireActivity() as MainActivity)
            .setSupportActionBar(view.findViewById(R.id.newsDetailToolbar))

        val args : NewsDetailFragmentArgs by navArgs()
        article = args.selectedArticle

        binding.newsDetailWebView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                if(it.isNotEmpty()) {
                    loadUrl(it)
                }
            }
        }

        binding.newsDetailToolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            title = article.source?.name
        }

        binding.saveArticleFab.setOnClickListener {
            saveArticle()
            binding.saveArticleFab.isVisible = false
        }

        handleOptionMenu()
    }

    private fun handleOptionMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.news_detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.saveArticle -> {
                        saveArticle()
                        true
                    }
                    R.id.shareArticle -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun saveArticle() {
        viewModel.saveArticleToDB(article)
        Snackbar.make(
            binding.root,
            getString(R.string.article_saved),
            Snackbar.LENGTH_LONG
        ).show()
    }
}