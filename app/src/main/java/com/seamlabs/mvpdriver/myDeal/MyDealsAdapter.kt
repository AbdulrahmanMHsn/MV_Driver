package com.seamlabs.mvpdriver.myDeal


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.databinding.AdapterItemMyDealBinding
import com.seamlabs.mvpdriver.models.TripModel


class MyDealsAdapter(private val onClickItem:(TripModel)->Unit) :
    ListAdapter<TripModel, MyDealsAdapter.MyDealsViewHolder>(MyDealsDiffUtil) {

    inner class MyDealsViewHolder(val binding: AdapterItemMyDealBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyDealsViewHolder {
        return MyDealsViewHolder(
            AdapterItemMyDealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyDealsViewHolder,
        position: Int,
    ) {
        val item = getItem(position)

        holder.binding.apply {

            txtDateRequest.text = "Starting ${item.createdAt}"

            txtTitleLocationHome.text = "Almohammadiyah"

            txtDescLocationHome.text = "Mohammadiyah district"

            item.goTripTime?.let {
                txtDateStartRequest.text = it
            }

            item.backTripTime?.let {
                txtDateEndRequest.text = it
            }

            txtTitleLocation.text = "Alandalus school"

            txtDescLocation.text = "Abasatin district"

            if (item.girlsCount != 0) {
                txtNoOfGirls.text = item.girlsCount.toString()
                txtNoOfGirls.visibility = View.VISIBLE
            }else{
                txtNoOfGirls.visibility = View.GONE
            }

            if (item.boysCount != 0) {
                txtNoOfBoys.text =  item.boysCount.toString()
                txtNoOfBoys.visibility = View.VISIBLE
            }else{
                txtNoOfBoys.visibility = View.GONE
            }

            txtNoOfDays.text = item.tripPeriod
            txtTypeCar.text = item.vehicleType

            txtDateRequest.text = item.createdAt

            txtStatusRequest.text = item.status

            root.setOnClickListener {
                onClickItem(item)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


object MyDealsDiffUtil : DiffUtil.ItemCallback<TripModel>() {
    override fun areItemsTheSame(
        oldItem: TripModel,
        newItem: TripModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TripModel,
        newItem: TripModel,
    ): Boolean {
        return oldItem == newItem
    }
}