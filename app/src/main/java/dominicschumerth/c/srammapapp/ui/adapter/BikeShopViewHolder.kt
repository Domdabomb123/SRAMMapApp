package dominicschumerth.c.srammapapp.ui.adapter

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dominicschumerth.c.srammapapp.PhotoTask
import dominicschumerth.c.srammapapp.R
import dominicschumerth.c.srammapapp.listener.AdapterOnClickItemListener
import dominicschumerth.c.srammapapp.listener.OnPhotoCompleted
import dominicschumerth.c.srammapapp.model.BikeShop

class BikeShopViewHolder(view:View) : RecyclerView.ViewHolder(view), OnPhotoCompleted {

    lateinit var item: BikeShop
    private var mListener: AdapterOnClickItemListener<BikeShop>? = null

    fun bind(item: BikeShop, listener: AdapterOnClickItemListener<BikeShop>?) {
        this.item = item
        mListener = listener

        itemView.findViewById<TextView>(R.id.tv_name).text = item.name
        itemView.findViewById<TextView>(R.id.tv_address).text = item.address

        item.thumbnail?.let {
            val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + it + "&key=" + itemView.context.getString(R.string.google_maps_key)
            PhotoTask(this).execute(url)
        }

        itemView.setOnClickListener {
            mListener?.onItemClicked(adapterPosition, item)
        }
    }

    override fun onPhotoCompleted(photo: Bitmap?) {
        itemView.findViewById<ImageView>(R.id.iv_photo).setImageBitmap(photo)
    }
}