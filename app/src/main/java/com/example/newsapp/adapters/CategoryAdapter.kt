package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.CategoryItemsBinding
import com.example.newsapp.models.NewsCategory
import com.example.newsapp.models.getAllNewsCategories

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    lateinit var onCategoryItemClicked: ((NewsCategory) -> Unit)
    private val categoryList = getAllNewsCategories()

    class CategoryViewHolder(private val binding: CategoryItemsBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(newsCategory: NewsCategory) {
                binding.categoryBtn.text = newsCategory.category
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = CategoryItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryAtPosition = categoryList[position]
        holder.bind(categoryAtPosition)
        holder.itemView.setOnClickListener {
            onCategoryItemClicked.invoke(categoryAtPosition)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}