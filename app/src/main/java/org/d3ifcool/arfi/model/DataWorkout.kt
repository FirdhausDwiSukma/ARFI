package org.d3ifcool.arfi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataWorkout(
    val id: Int,
    val img_detail_page : String,
    val title_detail_page : Int,
    //val icon_saran : Int,
    val desc_saran : Int,
    //val desc_dialog : ArrayList<String>
    val id_video : Int
): Parcelable