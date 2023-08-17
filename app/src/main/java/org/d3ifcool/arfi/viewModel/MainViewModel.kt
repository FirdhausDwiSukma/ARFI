package org.d3ifcool.arfi.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import org.d3ifcool.arfi.model.DataWorkout
import org.d3ifcool.arfi.utils.DummyData

class MainViewModel() : ViewModel(){
    private var data = ArrayList<DataWorkout>()
    init {
        data = DummyData().getAllData()
    }
    fun getAllData(): ArrayList<DataWorkout> {
        return data
    }
}