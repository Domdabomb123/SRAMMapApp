package dominicschumerth.c.srammapapp.listener

import android.graphics.Bitmap

interface OnPhotoCompleted {
    fun onPhotoCompleted(photo: Bitmap?)
}