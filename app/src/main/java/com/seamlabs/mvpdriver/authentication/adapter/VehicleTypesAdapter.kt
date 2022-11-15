package com.seamlabs.mvpdriver.authentication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.databinding.AdapterVehicleTypeBinding
import com.seamlabs.mvpdriver.models.VehicleTypeModel


class VehicleTypesAdapter(private val callbackClickItem:(VehicleTypeModel)->Unit) :
    ListAdapter<VehicleTypeModel, VehicleTypesAdapter.VehicleTypesViewHolder>(VehicleTypesDiffUtil) {

    inner class VehicleTypesViewHolder(val binding: AdapterVehicleTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VehicleTypesViewHolder {
        return VehicleTypesViewHolder(
            AdapterVehicleTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: VehicleTypesViewHolder,
        position: Int,
    ) {
        val item = getItem(position)


        holder.binding.apply {

            txtVehicleType.text = item.name

            root.setOnClickListener {
                callbackClickItem(item)
            }

            if (position == itemCount-1){
                viewLine.visibility = View.GONE
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


object VehicleTypesDiffUtil : DiffUtil.ItemCallback<VehicleTypeModel>() {
    override fun areItemsTheSame(
        oldItem: VehicleTypeModel,
        newItem: VehicleTypeModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: VehicleTypeModel,
        newItem: VehicleTypeModel,
    ): Boolean {
        return oldItem == newItem
    }
}