package com.seamlabs.mvpdriver.marketRequests


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.databinding.AdapterItemMarketRequestBinding
import com.seamlabs.mvpdriver.models.TripModel


class MarketRequestsAdapter(private val callbackClickCard:(TripModel)->Unit) :
    ListAdapter<TripModel, MarketRequestsAdapter.MarketRequestsViewHolder>(MarketRequestsDiffUtil) {

    inner class MarketRequestsViewHolder(val binding: AdapterItemMarketRequestBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MarketRequestsViewHolder {
        return MarketRequestsViewHolder(
            AdapterItemMarketRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MarketRequestsViewHolder,
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
            
            txtTimeCreatedRequest.text = item.createdAt

            root.setOnClickListener {
                callbackClickCard(item)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


object MarketRequestsDiffUtil : DiffUtil.ItemCallback<TripModel>() {
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