package dominicschumerth.c.srammapapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dominicschumerth.c.srammapapp.PlacesTask
import dominicschumerth.c.srammapapp.R
import dominicschumerth.c.srammapapp.listener.AdapterOnClickItemListener
import dominicschumerth.c.srammapapp.listener.OnTaskCompleted
import dominicschumerth.c.srammapapp.model.BikeShop
import dominicschumerth.c.srammapapp.ui.adapter.BikeShopAdapter

class ListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, AdapterOnClickItemListener<BikeShop>, OnTaskCompleted {

    private var bikeShops: ArrayList<BikeShop> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BikeShopAdapter

    private var currentLat:Double = 0.0
    private var currentLong:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        intent.extras?.let{
            currentLat = it.get("lat") as Double
            currentLong = it.get("lng") as Double
            bikeShops = it.get("list") as ArrayList<BikeShop>
        }

        setUpViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_menu -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpViews() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)

        adapter = BikeShopAdapter(bikeShops)
        adapter.setListener(this)
        recyclerView.adapter = adapter

        findViewById<SwipeRefreshLayout>(R.id.swipe_layout).setOnRefreshListener(this)

        if (bikeShops.isEmpty()) {
            getBikeShops()
        }
    }

    private fun getBikeShops() {
        //Query string
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + currentLat + "," + currentLong +
                "&rankby=distance" + "&type=bicycle_store&key=" + getString(R.string.google_maps_key)

        //Places task method to download json
        PlacesTask(this).execute(url)
    }

    override fun onRefresh() {
        getBikeShops()
    }

    override fun onItemClicked(position: Int, item: BikeShop) {
        BikeShopDialogFragment.newInstance(item).show(supportFragmentManager, null)
    }

    override fun onTaskCompleted(list: ArrayList<BikeShop>) {
        bikeShops = list
        adapter.setBikeShops(bikeShops)
        findViewById<SwipeRefreshLayout>(R.id.swipe_layout).isRefreshing = false
    }
}