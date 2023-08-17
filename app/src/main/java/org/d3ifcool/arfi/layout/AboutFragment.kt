package org.d3ifcool.arfi.layout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.d3ifcool.arfi.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDev.setOnClickListener {
            val intent = Intent(activity, AboutDevActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.btnArtikel.setOnClickListener{
            val intent = Intent(activity, ArtikelActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.btnKalkulator.setOnClickListener {
            val intent = Intent(activity, KalkulatorKaloriActivity::class.java)
            activity?.startActivity(intent)
        }
    }
}