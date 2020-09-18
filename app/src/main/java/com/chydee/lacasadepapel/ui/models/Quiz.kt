package com.chydee.lacasadepapel.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Quiz(
    val question: @RawValue Any?,
    val options: @RawValue List<Any?>,
    val answer: @RawValue Any?
) : Parcelable