package org.d3ifcool.arfi.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.CardArtikelBinding
import org.d3ifcool.arfi.layout.DetailArtikelActivity
import org.d3ifcool.arfi.model.DataArtikel

class CardArtikelAdapter (private val data: ArrayList<DataArtikel>, private val activity: Activity) :
    RecyclerView.Adapter<CardArtikelAdapter.ViewHolder>() {

    private lateinit var binding: CardArtikelBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.apply {
            Glide.with(holder.itemView)
                .load(data[position].image_card_artikel)
                .into(cardImage)
            titleCardArtikel.text = activity.resources.getStringArray(R.array.title_workout)[data[position].title_card_artikel]
            cardArtikelActivity.setOnClickListener {
                val intent = Intent(activity, DetailArtikelActivity::class.java)
                intent.putExtra("ID", data[position].id)
                activity.startActivity(intent)
            }
        }
    }
}