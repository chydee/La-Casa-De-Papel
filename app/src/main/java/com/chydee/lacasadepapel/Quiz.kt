package com.chydee.lacasadepapel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Quiz(
    val question: String,
    val options: List<String>,
    val answer: String
) : Parcelable