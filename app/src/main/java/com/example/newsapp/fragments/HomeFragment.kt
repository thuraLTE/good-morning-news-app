package com.example.newsapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.CategoryAdapter
import com.example.newsapp.adapters.HeadlineAdapter
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Article
import com.example.newsapp.models.getAllNewsCategories
import com.example.newsapp.utils.isConnectedToInternet
import com.example.newsapp.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HeadlineAdapter.OnNewsHeadlineClicked {

    private val homeTag = "Home Fragment"
    private lateinit var binding: FragmentHomeBinding
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter()
    }
    private val headlineAdapter: HeadlineAdapter by lazy {
        HeadlineAdapter(this)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    private var newsCategory: String? = null
    private var progressDialog: Dialog? = null
    private var failedConnectionDialog: Dialog? = null
    private var scrollState: Parcelable? = null
    private val QUERY_CHANGE = "Query Change"
    private var isQueryChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.rvHeadlineNews.layoutManager?.onRestoreInstanceState(scrollState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createProgressDialog()
        checkInternetConnection(view)
    }

    private fun checkInternetConnection(view: View) {
        if (!isConnectedToInternet(view.context)) {
            if (progressDialog?.isShowing == true) progressDialog!!.dismiss()
            createFailedConnectionDialog()
        } else {
            if (failedConnectionDialog?.isShowing == true) failedConnectionDialog!!.dismiss()

            prepareCategoryAdapter()
            prepareHeadlineAdapter()
            onClickCategoryButton()
            searchQuery()

            observeCurrentCategory()
            if (isQueryChanged) observeCurrentQuery()
            observeArticleList()
        }
    }

    private fun prepareCategoryAdapter() {
        binding.rvNewsCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun prepareHeadlineAdapter() {
        binding.rvHeadlineNews.apply {
            adapter = headlineAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeCurrentCategory() {
        homeViewModel.apply {
            currentCategory.observe(viewLifecycleOwner) { category ->
                getNewsHeadlinesByCategory(category)
            }
        }
    }

    private fun observeCurrentQuery() {
        homeViewModel.apply {
            currentQuery.observe(viewLifecycleOwner) { query ->
                if (query.isNotEmpty())
                    getSearchedNewsHeadlines(query)
                else return@observe
            }
        }
    }

    private fun observeArticleList() {
        homeViewModel.articleListLiveData.observe(viewLifecycleOwner) { list ->
            if (progressDialog?.isShowing == true) progressDialog?.dismiss()
            if (list != null) {
                Log.d(homeTag, "${list.size}")
                binding.apply {
                    rvHeadlineNews.visibility = View.VISIBLE
                    noResultsImg.visibility = View.GONE
                }
                headlineAdapter.setArticleList(list)
            }
            else {
                binding.apply {
                    rvHeadlineNews.visibility = View.GONE
                    noResultsImg.visibility = View.VISIBLE
                }
            }
            Log.d(homeTag, "${headlineAdapter.itemCount}")
        }
    }

    private fun onClickCategoryButton() {
        categoryAdapter.onCategoryItemClicked = { category ->
            progressDialog?.show()
            when (category) {
                getAllNewsCategories()[0] -> {
                    newsCategory = getAllNewsCategories()[0].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                getAllNewsCategories()[1] -> {
                    newsCategory = getAllNewsCategories()[1].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                getAllNewsCategories()[2] -> {
                    newsCategory = getAllNewsCategories()[2].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                getAllNewsCategories()[3] -> {
                    newsCategory = getAllNewsCategories()[3].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                getAllNewsCategories()[4] -> {
                    newsCategory = getAllNewsCategories()[4].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                getAllNewsCategories()[5] -> {
                    newsCategory = getAllNewsCategories()[5].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
                else -> {
                    newsCategory = getAllNewsCategories()[6].category
                    Log.d(homeTag, "$newsCategory Clicked!")
                }
            }
            binding.searchEdit.text?.clear()
            homeViewModel.apply {
                changeCategory(newsCategory!!)
                clearQuery()
            }
            observeCurrentQuery()
            observeCurrentCategory()
            observeArticleList()
        }
    }

    private fun createProgressDialog() {
        progressDialog = Dialog(context!!)
        progressDialog?.apply {
            setContentView(R.layout.loading_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            show()
        }
    }

    private fun createFailedConnectionDialog() {
        failedConnectionDialog = Dialog(context!!)
        failedConnectionDialog?.apply {
            setContentView(R.layout.failed_connection_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            show()
        }
    }

    private fun searchQuery() {
        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isQueryChanged = true
                if (s?.isNotEmpty() == true) {
                    val query = s.trim().toString()
                    Log.d(homeTag, "Current Query: $query")
                    searchHeadlinesAsTyped(query)
                    observeArticleList()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun searchHeadlinesAsTyped(queryString: String) {
        lifecycleScope.launch {
            delay(500)
            homeViewModel.changeQuery(queryString)
            observeCurrentQuery()
        }
    }

    private fun dismissDialogs() {
        progressDialog?.dismiss()
        failedConnectionDialog?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(QUERY_CHANGE, isQueryChanged)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            isQueryChanged = savedInstanceState.getBoolean(QUERY_CHANGE)
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onItemClicked(article: Article) {
        val action = HomeFragmentDirections.actionHomeFragmentToNewsInfoFragment(article)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        binding.rvHeadlineNews.layoutManager?.onRestoreInstanceState(scrollState)
    }

    override fun onResume() {
        super.onResume()
        binding.rvHeadlineNews.layoutManager?.onRestoreInstanceState(scrollState)
    }

    override fun onPause() {
        super.onPause()
        scrollState = binding.rvHeadlineNews.layoutManager?.onSaveInstanceState()
    }

    override fun onStop() {
        super.onStop()
        dismissDialogs()
        scrollState = binding.rvHeadlineNews.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroy()
        dismissDialogs()
        scrollState = binding.rvHeadlineNews.layoutManager?.onSaveInstanceState()
    }
}