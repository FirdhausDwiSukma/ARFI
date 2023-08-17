package org.d3ifcool.arfi.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.ActivityDetailArtikelBinding
import org.d3ifcool.arfi.model.DataArtikel
import org.d3ifcool.arfi.viewModel.DetailViewModelArtikel

class DetailArtikelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArtikelBinding
    private lateinit var viewModel: DetailViewModelArtikel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = DetailViewModelArtikel()
        val data = intent.extras?.getInt("ID")
        val dataArtikel = viewModel.getDataById(data!!)
        setWebViewArtikel(dataArtikel)
    }

    private fun setWebViewArtikel(data: DataArtikel){
        val urlArtikel = resources.getStringArray(R.array.artikel_workout)[data.id]
        val webView: WebView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.loadUrl(urlArtikel) // Ganti URL dengan artikel yang ingin ditampilkan
    }
}