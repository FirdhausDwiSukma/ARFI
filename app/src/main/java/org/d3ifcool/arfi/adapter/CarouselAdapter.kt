package org.d3ifcool.arfi.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.CardCarouselBinding
import org.d3ifcool.arfi.layout.DetailPageActivity
import org.d3ifcool.arfi.model.DataWorkout

class CarouselAdapter(private val data: ArrayList<DataWorkout>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    private lateinit var binding: CardCarouselBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        data
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.apply {
            titleCardCarousel.text = activity.resources.getStringArray(R.array.title_workout)[data[position].title_detail_page]
            Glide.with(holder.itemView)
                .load(data[position].img_detail_page)
                .into(imageView)
            carouselItemContainer.setOnClickListener{
                val intent = Intent(activity, DetailPageActivity::class.java)
                intent.putExtra("ID", data[position].id)
                activity.startActivity(intent)
            }
        }
    }
}