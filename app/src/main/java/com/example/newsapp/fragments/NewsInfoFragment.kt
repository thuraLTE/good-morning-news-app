package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsInfoBinding
import com.example.newsapp.models.Article

class NewsInfoFragment : Fragment() {

    private lateinit var binding: FragmentNewsInfoBinding
    private val args: NewsInfoFragmentArgs by navArgs()

    private var currentArticle: Article? = null

    private val TAG = "NewsInfo Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentArticle = args.article
        Log.d(TAG, "Current article title: ${currentArticle?.title}")

        bindSelectedArticleIntoUI()
    }

    private fun bindSelectedArticleIntoUI() {
        currentArticle?.let { article ->
            binding.apply {
                newsTitle.text = article.title

                if (article.urlToImage != null)
                    Glide.with(this@NewsInfoFragment)
                        .load(article.urlToImage)
                        .into(newsImage)
                else Glide.with(this@NewsInfoFragment)
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.news_bg))
                    .into(newsImage)

                if (article.author != null) newsAuthor.text =
                    article.author else newsAuthor.visibility = View.GONE

                if (article.publishedAt != null) newsPublishedDate.text =
                    article.publishedAt else newsPublishedDate.visibility = View.GONE

                if (article.content != null) newsContent.text =
                    getString(R.string.tab).plus(article.content) else newsContent.visibility =
                    View.GONE

                readMore.setOnClickListener {
                    onClickReadMore(article)
                }
            }
        }
    }

    private fun onClickReadMore(article: Article) {
        val action = NewsInfoFragmentDirections.actionNewsInfoFragmentToWebFragment(article.url)
        findNavController().navigate(action)
    }
}