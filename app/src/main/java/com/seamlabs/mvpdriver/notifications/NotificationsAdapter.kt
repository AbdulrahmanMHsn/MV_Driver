package com.seamlabs.mvpdriver.notifications


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.databinding.AdapterItemNotificationBinding


class NotificationsAdapter() :
    ListAdapter<String, NotificationsAdapter.NotificationsViewHolder>(NotificationsDiffUtil) {

    inner class NotificationsViewHolder(val binding: AdapterItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotificationsViewHolder {
        return NotificationsViewHolder(
            AdapterItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: NotificationsViewHolder,
        position: Int,
    ) {
        val item = getItem(position)

        holder.binding.apply {

            if (position == itemCount-1){
                divider1.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


object NotificationsDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean {
        return oldItem == newItem
    }
}