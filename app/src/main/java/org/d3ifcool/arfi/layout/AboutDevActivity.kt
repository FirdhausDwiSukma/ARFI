package org.d3ifcool.arfi.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.ActivityAboutDevBinding
import org.d3ifcool.arfi.utils.DummyData

class AboutDevActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAboutDevBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutDevBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}