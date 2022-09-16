package incubasys.needcharge.profileandsettings.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import incubasys.needcharge.base.RecyclerViewCallback
import incubasys.needcharge.profileandsettings.data.Settings
import incubasys.needcharge.profileandsettings.databinding.SettingsListItemBinding

class SettingsAdapter(val callback: RecyclerViewCallback) : ListAdapter<Settings, SettingsAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(SettingsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(private val binding: SettingsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Settings) = with(binding) {
            settings = item
            executePendingBindings()
            rlSettingsListItem.setOnClickListener {
                callback.onListItemClicked(adapterPosition)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Settings>() {
    override fun areItemsTheSame(oldItem: Settings, newItem: Settings): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Settings, newItem: Settings): Boolean {
        return oldItem == newItem
    }
}