package kr.ac.ajou.heidi.locatr

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_locatr.view.*
import java.io.IOException

class LocatrFragment : SupportMapFragment() {
    companion object {
        fun newInstance(): LocatrFragment {
            return LocatrFragment()
        }
    }

    private var client: GoogleApiClient? = null
    private var map: GoogleMap? = null
    private var mapImage: Bitmap? = null
    private var mapItem: GalleryItem? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            client = GoogleApiClient
                .Builder(it)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnectionSuspended(p0: Int) {
                    }

                    override fun onConnected(p0: Bundle?) {
                        activity?.invalidateOptionsMenu()
                    }

                })
                .build()

            getMapAsync { googleMap ->
                map = googleMap
                updateUI()
            }
        }
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_locatr, container, false)
//        return view
//    }

    fun updateUI() {
        if (map == null || mapImage == null) return

        mapItem?.let {
            val itemPoint = LatLng(it.lat, it.lon)
            val myPoint = LatLng(currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0)
            val bounds = LatLngBounds.Builder().include(itemPoint).include(myPoint).build()
            val margin = resources.getDimensionPixelSize(R.dimen.map_inset_margin)
            val update = CameraUpdateFactory.newLatLngBounds(bounds, margin)
            map?.animateCamera(update)
            val itemBitmap = BitmapDescriptorFactory.fromBitmap(mapImage)
            val itemMarker = MarkerOptions().position(itemPoint).icon(itemBitmap)
            val myMarker = MarkerOptions().position(myPoint)
            map?.clear()
            map?.addMarker(itemMarker)
            map?.addMarker(myMarker)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_locatr, menu)

        val searchItem = menu.findItem(R.id.action_locate)
        searchItem.isEnabled = client?.isConnected ?: false
    }

    private fun findImage() {
        val request = LocationRequest.create()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.numUpdates = 1
        request.interval = 0

        view?.context?.let {

            if (ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                LocationServices.getFusedLocationProviderClient(activity ?: return)
                    .lastLocation.addOnSuccessListener { location ->
                    SearchTask().execute(location)
                }

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_locate -> {
                findImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        activity?.invalidateOptionsMenu()
        client?.connect()
    }

    override fun onStop() {
        super.onStop()

        client?.disconnect()
    }

    inner class SearchTask : AsyncTask<Location, Unit, Unit>() {
        private var galleryItem: GalleryItem? = null
        private var bitmap: Bitmap? = null
        private var location: Location? = null

        override fun doInBackground(vararg params: Location?) {
            location = params[0]
            val fetchr = FlickrFetchr()
            params[0]?.let {
                val items = fetchr.searchPhotos(it)
                if (items.isEmpty()) return
                galleryItem = items[0]
            }

            try {
                val bytes = fetchr.getUrlBytes(galleryItem?.url ?: "")
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: IOException) {
                Log.i(LocatrFragment::class.java.name, "Unable to download bitmap $e")
            }
        }

        override fun onPostExecute(result: Unit?) {
//            view?.photoImageView?.setImageBitmap(bitmap)
            mapImage = bitmap
            mapItem = galleryItem
            currentLocation = location

            updateUI()
        }


    }
}
