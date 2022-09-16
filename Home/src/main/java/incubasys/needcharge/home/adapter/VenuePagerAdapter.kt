package incubasys.needcharge.home.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import incubasys.needcharge.base.RecyclerViewCallback
import incubasys.needcharge.datainterfaces.VenueItem
import incubasys.needcharge.home.databinding.VenueItemSmallBinding
import incubasys.needcharge.home.vm.VenueItemViewModel

class VenuePagerAdapter(private val items: MutableList<VenueItem> = ArrayList(), private val callback: RecyclerViewCallback? = null, private val mContext: Context) : PagerAdapter() /* RecyclerView.Adapter<VenueAdapter.ItemViewHolder>()<VenueItem, VenueAdapter.ItemViewHolder>(DiffCallback())*/ {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val vm = VenueItemViewModel()
        vm.venueItem.set(items[position])
        val binding = VenueItemSmallBinding.inflate(LayoutInflater.from(mContext), collection, false)
        collection.addView(binding.root)
        binding.vm = vm
        with(binding.root) {
            setOnClickListener {
                callback?.onListItemClicked(position)
            }
        }
        binding.executePendingBindings()
        return binding.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int) = ""

    override fun getCount() = items.size

    fun updateList(items: List<VenueItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

/*  override fun getItemCount() = items.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
      return ItemViewHolder(VenueItemSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      holder.bind(getItem(position))
  }*/

/* private fun getItem(position: Int) = items[position]

 inner class ItemViewHolder internal constructor(private val binding: VenueItemSmallBinding) :
         RecyclerView.ViewHolder(binding.root) {

     val vm = VenueItemViewModel()

     init {
         binding.vm = vm
         binding.executePendingBindings()
     }

     internal fun bind(item: VenueItem) {
         vm.venueItem.set(item)
         binding.executePendingBindings()
     }
 }
}*/

/*
class DiffCallback : DiffUtil.ItemCallback<VenueItem>() {
    override fun areItemsTheSame(oldItem: VenueItem, newItem: VenueItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VenueItem, newItem: VenueItem): Boolean {
        return oldItem == newItem
    }
}*/
