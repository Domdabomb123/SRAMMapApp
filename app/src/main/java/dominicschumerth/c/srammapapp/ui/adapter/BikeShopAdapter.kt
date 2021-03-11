package dominicschumerth.c.srammapapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dominicschumerth.c.srammapapp.R
import dominicschumerth.c.srammapapp.listener.AdapterOnClickItemListener
import dominicschumerth.c.srammapapp.model.BikeShop

class BikeShopAdapter(list: ArrayList<BikeShop>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItems:ArrayList<BikeShop> = list
    private var mListener: AdapterOnClickItemListener<BikeShop>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BikeShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bikeshop_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BikeShopViewHolder).bind(mItems[position], mListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun setBikeShops(bikeShops: ArrayList<BikeShop>) {
        mItems = bikeShops
        notifyDataSetChanged()
    }

    fun setListener(listener: AdapterOnClickItemListener<BikeShop>) {
        this.mListener = listener
    }

}