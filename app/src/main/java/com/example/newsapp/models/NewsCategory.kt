package com.example.newsapp.models

enum class NewsCategory(val category: String) {

    GENERAL("general"),
    BUSINESS("business"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology"),
    ENTERTAINMENT("entertainment")
}

fun getAllNewsCategories() = listOf(
    NewsCategory.GENERAL,
    NewsCategory.BUSINESS,
    NewsCategory.HEALTH,
    NewsCategory.SCIENCE,
    NewsCategory.SPORTS,
    NewsCategory.TECHNOLOGY,
    NewsCategory.ENTERTAINMENT
)