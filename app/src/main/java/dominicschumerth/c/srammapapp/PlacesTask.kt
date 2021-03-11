package dominicschumerth.c.srammapapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import dominicschumerth.c.srammapapp.listener.OnPhotoCompleted
import dominicschumerth.c.srammapapp.listener.OnTaskCompleted
import dominicschumerth.c.srammapapp.model.BikeShop
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

class PlacesTask(private val listener: OnTaskCompleted) : AsyncTask<String, Integer, String>() {

    override fun doInBackground(vararg params: String?): String {
        params[0]?.let {
            return downloadUrl(it)
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        ParserTask(listener).execute(result)
    }


    private fun downloadUrl(string: String): String {
        val url = URL(string)
        val connection = url.openConnection()
        connection.connect()
        val stream = connection.getInputStream()
        val reader = BufferedReader(InputStreamReader(stream))
        val builder = StringBuilder()
        var line = reader.readLine()
        while (line != null) {
            builder.append(line)
            line = reader.readLine()
        }
        val data = builder.toString()
        reader.close()
        return data

    }
}

private class ParserTask(val listener: OnTaskCompleted) : AsyncTask<String, Integer, ArrayList<BikeShop>>() {

    override fun doInBackground(vararg params: String?): ArrayList<BikeShop> {
        val dataList = ArrayList<BikeShop>()
        params[0]?.let {
            val jsonArray = JSONObject(it).getJSONArray("results")

            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    if (i == 10) break
                    dataList.add(BikeShop(jsonArray.get(i) as JSONObject))
                }
            }
        }

        return dataList
    }

    override fun onPostExecute(result: ArrayList<BikeShop>) {
        listener.onTaskCompleted(result)
    }

}

class PhotoTask(private val listener: OnPhotoCompleted) : AsyncTask<String, Integer, Bitmap?>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        params[0]?.let {
            return BitmapFactory.decodeStream(URL(it).openConnection().getInputStream())
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        listener.onPhotoCompleted(result)
    }
}