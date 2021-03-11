package dominicschumerth.c.srammapapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import dominicschumerth.c.srammapapp.PlacesTask
import dominicschumerth.c.srammapapp.R
import dominicschumerth.c.srammapapp.listener.OnTaskCompleted
import dominicschumerth.c.srammapapp.model.BikeShop

class MapsActivity : AppCompatActivity(), OnTaskCompleted, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var bikeShops: ArrayList<BikeShop> = ArrayList()
    private var currentLat:Double = 0.0
    private var currentLong:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Manage device location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        getLocationAccess()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_menu -> {
                val intent = Intent(this, ListActivity::class.java)
                intent.putExtra("lat", currentLat)
                intent.putExtra("lng", currentLong)
                intent.putExtra("list", bikeShops)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun getCurrentLocation() {
        //Initialize task location
        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            currentLat = location.latitude
            currentLong = location.longitude

            supportMapFragment.getMapAsync { googleMap ->
                mMap = googleMap
                mMap.isMyLocationEnabled = true
                mMap.setOnMarkerClickListener(this)

                val latLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))

                getBikeShops()
            }
        }
    }

    private fun getBikeShops() {
        //Query string
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + currentLat + "," + currentLong +
                "&rankby=distance" + "&type=bicycle_store&key=" + getString(R.string.google_maps_key)

        //Places task method to download json
        PlacesTask(this).execute(url)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getLocationAccess()
            }
            else {
                Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onTaskCompleted(result: ArrayList<BikeShop>) {
        mMap.clear()
        bikeShops = result

        for (bikeShop in result) {
            val options = MarkerOptions()
            options.position(LatLng(bikeShop.latitude, bikeShop.longitude))
            options.title(bikeShop.name)
            mMap.addMarker(options)
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        for (bikeShop in bikeShops) {
            if (bikeShop.name == p0?.title) {
                BikeShopDialogFragment.newInstance(bikeShop).show(supportFragmentManager, null)
                break
            }
        }
        return true
    }
}
