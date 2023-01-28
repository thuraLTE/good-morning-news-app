package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Article(
    var source: @RawValue Source? = null,
    var author: @RawValue String? = null,
    var title: @RawValue String? = null,
    var description: @RawValue String? = null,
    var url: @RawValue String,
    var urlToImage: @RawValue String? = null,
    var publishedAt: @RawValue String? = null,
    var content: @RawValue String? = null
): Parcelable
