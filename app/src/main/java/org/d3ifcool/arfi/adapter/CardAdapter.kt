package org.d3ifcool.arfi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.CardWorkoutBinding
import org.d3ifcool.arfi.layout.DetailPageActivity
import org.d3ifcool.arfi.model.DataWorkout

class CardAdapter (private val data: ArrayList<DataWorkout>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private lateinit var binding: CardWorkoutBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.apply {
            Glide.with(holder.itemView)
                .load(data[position].img_detail_page)
                .into(cardImage)
            titleCardWorkout.text = activity.resources.getStringArray(R.array.title_workout)[data[position].title_detail_page]
            categoryCardWorkout.text = activity.resources.getStringArray(R.array.kategori_alat)[data[position].desc_saran]
            cardWorkoutActivity.setOnClickListener{
                val intent = Intent(activity, DetailPageActivity::class.java)
//                intent.putExtra("WORKOUT", data[position])
                intent.putExtra("ID", data[position].id)
                activity.startActivity(intent)
            }
        }
    }
}