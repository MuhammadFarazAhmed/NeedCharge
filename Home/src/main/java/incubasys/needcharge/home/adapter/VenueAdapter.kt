package incubasys.needcharge.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import incubasys.needcharge.base.RecyclerViewCallback
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.home.databinding.VenueItemBigBinding
import incubasys.needcharge.home.vm.VenueItemViewModel

class VenueAdapter(private val callback: RecyclerViewCallback? = null) : ListAdapter<VenueItem, VenueAdapter.ItemViewholder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(VenueItemBigBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(private val binding: VenueItemBigBinding) : RecyclerView.ViewHolder(binding.root) {
        private val vm = VenueItemViewModel()
        fun bind(item: VenueItem) = with(itemView) {
            vm.venueItem.set(item)
            binding.vm = vm
            binding.executePendingBindings()

            setOnClickListener {
                callback?.onListItemClicked(adapterPosition)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VenueItem>() {
        override fun areItemsTheSame(oldItem: VenueItem, newItem: VenueItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VenueItem, newItem: VenueItem): Boolean {
            return oldItem == newItem
        }
    }
}

