package com.seamlabs.mvpdriver.myDeal


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.utility.customFormatDate
import com.seamlabs.mvpdriver.common.utility.getDifferenceBetweenDates
import com.seamlabs.mvpdriver.databinding.AdapterItemMyDealBinding
import com.seamlabs.mvpdriver.models.TripModel
import com.seamlabs.mvpdriver.models.VehicleType


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
        val context = holder.itemView.context

        holder.binding.apply {

            context.customFormatDate(item.customStartDate, "dd/MM/yyyy", "EEE, d MMM") {
                txtDateRequest.text = context.getString(R.string.starting_from," $it ")
            }

            val district = item.pickupAddress.split(",")
            txtTitleLocationHome.text = item.pickupAddress
            txtDescLocationHome.text = "${district[district.size - 1]}"


            item.goTripTime?.let { time->
                txtDateStartRequest.visibility = View.VISIBLE
                context.customFormatDate(time, "H:mm:ss", "hh:mm aa") {
                    txtDateStartRequest.text = it
                }
            }


            item.backTripTime?.let { time->
                txtDateEndRequest.visibility = View.VISIBLE
                context.customFormatDate(time, "H:mm:ss", "hh:mm aa") {
                    txtDateEndRequest.text = it
                }
            }


            val districtDropOff = item.pickupAddress.split(",")
            txtTitleLocation.text = item.dropOffAddress
            txtDescLocation.text = "${districtDropOff[districtDropOff.size - 1]}"

//            if (item.girlsCount != 0) {
            txtNoOfGirls.text = item.girlsCount.toString()
//                txtNoOfGirls.visibility = View.VISIBLE
//            }else{
//                txtNoOfGirls.visibility = View.GONE
//            }

//            if (item.boysCount != 0) {
            txtNoOfBoys.text = item.boysCount.toString()
//                txtNoOfBoys.visibility = View.VISIBLE
//            }else{
//                txtNoOfBoys.visibility = View.GONE
//            }


            context.getDifferenceBetweenDates(item.customStartDate, item.customEndDate) {
                txtNoOfDays.text = "$it ${holder.itemView.context.getString(R.string.days)}"
            }

            when (item.vehicleType) {
                VehicleType.BUS.name -> txtTypeCar.text = context.getString(R.string.bus)
                VehicleType.CARPOOLING.name -> txtTypeCar.text =
                    context.getString(R.string.carpooling)
                VehicleType.CAR.name -> txtTypeCar.text = context.getString(R.string.personal_car)
            }


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