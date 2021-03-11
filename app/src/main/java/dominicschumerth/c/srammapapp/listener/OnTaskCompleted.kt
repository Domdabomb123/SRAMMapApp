package dominicschumerth.c.srammapapp.listener

import dominicschumerth.c.srammapapp.model.BikeShop

interface OnTaskCompleted {
    fun onTaskCompleted(list: ArrayList<BikeShop>)
}