package org.d3ifcool.arfi.viewModel

import androidx.lifecycle.ViewModel
import org.d3ifcool.arfi.layout.DetailPageActivity
import org.d3ifcool.arfi.utils.DummyData
import org.d3ifcool.arfi.utils.DummyDataArtikel

class DetailViewModel() : ViewModel() {

    fun getDataById(id: Int) = DummyData().getDataByIndex(id)

}