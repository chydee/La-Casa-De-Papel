package com.chydee.lacasadepapel.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(val name: String? = null, val score: Int? = null) : Parcelable
