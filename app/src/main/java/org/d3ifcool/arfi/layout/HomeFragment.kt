package org.d3ifcool.arfi.layout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.adapter.CardAdapter
import org.d3ifcool.arfi.adapter.CarouselAdapter
import org.d3ifcool.arfi.databinding.FragmentHomeBinding
import org.d3ifcool.arfi.model.DataWorkout
import org.d3ifcool.arfi.utils.DummyData
import org.d3ifcool.arfi.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dummyDataWorkout : DummyData
    private lateinit var viewModel : MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = MainViewModel()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dummyDataWorkout = DummyData()

        setupTitleHome()
        setCarouselImage()
        setCardRecommedation()
        //setCardRoom()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCardRecommedation() {
        val dataCard = viewModel.getAllData()
        dataCard.sortBy { activity?.resources!!.getStringArray(R.array.title_workout)[it.title_detail_page] }
        val layoutManager = GridLayoutManager(activity, 2)
        val adapterCard = CardAdapter(dataCard, requireActivity())

        binding.apply {
            cardWorkout.layoutManager = layoutManager
            cardWorkout.adapter = adapterCard
        }
    }

    private fun setupTitleHome() {

        val greetingIndex = when (SimpleDateFormat("HH", Locale.US).format(System.currentTimeMillis()).toInt()) {
            in 4 .. 9 -> {
                0
            }
            in 10..15 -> {
                1
            }
            in 16..18 -> {
                2
            }
            else -> {
                3
            }
        }

        val greeting = resources.getStringArray(R.array.greeting)[greetingIndex]
        binding.titleHome.text = resources.getString(R.string.title_home, greeting)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCarouselImage(){
        val data = viewModel.getAllData()

        //val adapter = CarouselAdapter(data.slice(0..5) as ArrayList<DataWorkout>, requireActivity())
        val adapter = CarouselAdapter(data.shuffled() as ArrayList<DataWorkout>, requireActivity())
        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
        binding.carouselRecyclerView.adapter = adapter
    }
}