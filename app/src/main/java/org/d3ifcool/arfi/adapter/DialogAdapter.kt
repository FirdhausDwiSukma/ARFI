package org.d3ifcool.arfi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.arfi.databinding.DialogTutorialBinding


class DialogAdapter(private val data: ArrayList<String>) :
    RecyclerView.Adapter<DialogAdapter.ViewHolder>() {

    private lateinit var binding: DialogTutorialBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogAdapter.ViewHolder {
        binding = DialogTutorialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DialogAdapter.ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tutorial.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

}