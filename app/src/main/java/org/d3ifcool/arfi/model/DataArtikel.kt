package org.d3ifcool.arfi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataArtikel(
    val id: Int,
    val image_card_artikel: String,
    val title_card_artikel: Int,
    val web_artikel_workout: Int
): Parcelable