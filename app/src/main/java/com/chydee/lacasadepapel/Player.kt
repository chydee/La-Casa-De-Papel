package com.chydee.lacasadepapel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(val name: String? = null, val score: Int? = null) : Parcelable
