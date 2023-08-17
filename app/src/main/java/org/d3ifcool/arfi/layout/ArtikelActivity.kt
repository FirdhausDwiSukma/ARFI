package org.d3ifcool.arfi.layout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.adapter.CardArtikelAdapter
import org.d3ifcool.arfi.databinding.ActivityArtikelBinding
import org.d3ifcool.arfi.utils.DummyDataArtikel
import org.d3ifcool.arfi.viewModel.MainViewModelArtikel

class ArtikelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtikelBinding
    private lateinit var dummyDataArtikel : DummyDataArtikel
    private lateinit var viewModel : MainViewModelArtikel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dummyDataArtikel = DummyDataArtikel()
        buttonBack()
        viewModel = ViewModelProvider(this)[MainViewModelArtikel::class.java]
        setCardRecommedation()
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCardRecommedation() {
        val dataCard = viewModel.getAllData()
        dataCard.sortBy { resources.getStringArray(R.array.title_workout)[it.title_card_artikel] }
        val layoutManager = GridLayoutManager(this, 1)
        val adapterCard = CardArtikelAdapter(dataCard, this)
        binding.apply {
            cardArtikel.layoutManager = layoutManager
            cardArtikel.adapter = adapterCard
        }
    }
    private fun buttonBack(){
        binding.backButtonArtikel.setOnClickListener{
            finish()
        }
    }
}