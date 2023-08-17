package org.d3ifcool.arfi.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import org.d3ifcool.arfi.model.DataArtikel
import org.d3ifcool.arfi.model.DataWorkout
import org.d3ifcool.arfi.utils.DummyData
import org.d3ifcool.arfi.utils.DummyDataArtikel

class MainViewModelArtikel() : ViewModel(){
    private var data = ArrayList<DataArtikel>()
    init {
        data = DummyDataArtikel().getAllData()
    }
    fun getAllData(): ArrayList<DataArtikel> {
        return data
    }
}