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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapiclient.databinding.FragmentNewsDetailBinding

class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding

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
        (requireActivity() as MainActivity)
            .setSupportActionBar(view.findViewById(R.id.newsDetailToolbar))

        val args : NewsDetailFragmentArgs by navArgs()
        val article = args.selectedArticle

        binding.newsDetailWebView.apply {
            webViewClient = WebViewClient()
            if(article.url.isNotEmpty()) {
                loadUrl(article.url)
            }
        }

        binding.newsDetailToolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            title = article.source.name
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
}