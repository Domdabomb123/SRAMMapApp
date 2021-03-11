package dominicschumerth.c.srammapapp.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import dominicschumerth.c.srammapapp.PhotoTask
import dominicschumerth.c.srammapapp.R
import dominicschumerth.c.srammapapp.listener.OnPhotoCompleted
import dominicschumerth.c.srammapapp.model.BikeShop

class BikeShopDialogFragment : DialogFragment(), OnPhotoCompleted {
    companion object {
        fun newInstance(bikeShop: BikeShop) : BikeShopDialogFragment {
            val bsdf = BikeShopDialogFragment()
            bsdf.bikeShop = bikeShop
            return bsdf
        }
    }

    private var bikeShop: BikeShop? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bikeshop, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_dialog_name).text = bikeShop?.name
        view.findViewById<TextView>(R.id.tv_dialog_address).text = bikeShop?.address

        bikeShop?.thumbnail?.let {
            val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1500&photoreference=" + it + "&key=" + view.context.getString(R.string.google_maps_key)
            PhotoTask(this).execute(url)
        }
    }

    override fun onPhotoCompleted(photo: Bitmap?) {
        view?.findViewById<ImageView>(R.id.iv_dialog_photo)?.setImageBitmap(photo)
    }
}