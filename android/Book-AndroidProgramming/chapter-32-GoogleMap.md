# 32. 구글 맵
> Locatr 애플리케이션, 이미지의 위도와 경도를 사용하여 지도에 보여줄 것이다.  

## 플레이 서비스 맵 라이브러리 import 하기
App 모듈에 라이브러리 추가하기. 
`implementation ‘com.google.android.gms:play-services-maps:16.0.0’`

## 맵 API 설정하기
AndroidManifest.xml에 권한 추가하기
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
// 네트워크 상태를 조회하는데 필요하다.
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
// 맵 데이터를 외부 스토리지에 임시로 저장하는데 필요하다.
```

## 맵 설정하기
맵은 MapView에서 보여준다.
MapView는 다른 뷰와 유사하지만, 액티비티나 프래그먼트의 생명주기 콜백 메서드가 호출되었을 때 그것을 MapView에 전달해주어야 한다.
```kotlin
override fun onCreate(savedInstanceState: Bundle) {
	super.onCreate(savedInstanceState)
	mapView.onCreate(savedInstatnceState)
}
```

MapFragment나 SupportMapFragment를 사용하면 위의 코드를 알아서 처리해준다. 이 두 클래스에서는 MapView 인스턴스를 생성하고 호스팅해주며, 적합한 생명주기 콜백 메소드도 호출해준다.
 
LocatrFragment의 부모 클래스를 Fragment에서 SupportMapFragment로 바꾼다.
onCreateView(…)를 지운다.
```kotlin
class LocatrFragment : SupportMapFragment() {
    companion object {
        fun newInstance(): LocatrFragment {
            return LocatrFragment()
        }
    }

    private var client: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        /activity/?./let/*{*
client = GoogleApiClient
                .Builder(*it*)
                .addApi(LocationServices./API/)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnectionSuspended(p0: Int) {
                    }

                    override fun onConnected(p0: Bundle?) {
                        /activity/?.invalidateOptionsMenu()
                    }

                })
                .build()
        *}*
}

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_locatr, container, false)
//        return view
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu./menu_locatr/, menu)

        val searchItem = menu.findItem(R.id./action_locate/)
        searchItem./isEnabled/= client?./isConnected/?: false
    }

    private fun findImage() {
        val request = LocationRequest.create()
        request./priority/= LocationRequest./PRIORITY_HIGH_ACCURACY/
request./numUpdates/= 1
        request./interval/= 0

        /view/?./context/?./let/*{*

if (ActivityCompat.checkSelfPermission(
                    *it*,
                    android.Manifest.permission./ACCESS_FINE_LOCATION/
)
                == PackageManager./PERMISSION_GRANTED/
) {
                LocationServices.getFusedLocationProviderClient(/activity/?: return)
                    ./lastLocation/.addOnSuccessListener *{*location *->*
SearchTask().execute(location)
                *}*

}
        *}*

}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item./itemId/) {
            R.id./action_locate/-> {
                findImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        /activity/?.invalidateOptionsMenu()
        client?.connect()
    }

    override fun onStop() {
        super.onStop()

        client?.disconnect()
    }

    inner class SearchTask : AsyncTask<Location, Unit, Unit>() {
        private var galleryItem: GalleryItem? = null
        private var bitmap: Bitmap? = null

        override fun doInBackground(vararg params: Location?) {
            val fetchr = FlickrFetchr()
            params[0]?./let/*{*
val items = fetchr.searchPhotos(*it*)
                if (items.isEmpty()) return
                galleryItem = items[0]
            *}*

try {
                val bytes = fetchr.getUrlBytes(galleryItem?.url ?: "")
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: IOException) {
                Log.i(LocatrFragment::class./java/./name/, "Unable to download bitmap $e")
            }
        }

        override fun onPostExecute(result: Unit?) {
//            view?.photoImageView?.setImageBitmap(bitmap)
        }


    }
```
이제 화면에 GoogleMap이 보인다.

## 더 많은 위치 데이터 얻기
플리커에서 갖고 온 이미지를 맵에 나타내려면 해당 이미지가갖고 있는 위치 데이터를 알아야 한다.
플리커 API 쿼리에 매개변수를 추가하여 위도와 경도를 받자.
```kotlin
/* FlickrFetchr */
val ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
    .buildUpon()
    .appendQueryParameter("api_key", API_KEY)
    .appendQueryParameter("format", "json")
    .appendQueryParameter("nojsoncallback", "1")
    .appendQueryParameter("extras", "url_s, geo")
    .build()
// geo 추가
```

```kotlin
/* GalleryItem */
var lat: Double = 0.0
var lon: Double = 0.0
```

```kotlin
/* FlickrFetchr */
fun parseItems(items: ArrayList<GalleryItem>, jsonBody: JSONObject) {
    // ...
        item.url = photoJsonObject.getString("url_s")
        item.owner = photoJsonObject.getString("owner")
        item.lat = photoJsonObject.getDouble("latitude")
// 추가
        item.lon = photoJsonObject.getDouble("longitude")
// 추가
        items.add(item)
    }
}
```

맵 관련 데이터를 가질 수 있다.

### 맵 사용하기
SupportMapFragment에서는 MapView를 생성하고, MapView는 실제로 일을 수행하는 GoogleMap 객체를 호스팅한다.

1. GoogleMap 객체 참조 얻기
```kotlin
/* LocatrFragment - onCreate*/
getMapAsync *{*googleMap *->*map = googleMap *}*
```
onCreate 메소드 내부에서 호출하면 GoogleMap 객체가 생성되어 초기화되었을 때 그것의 참조를 얻게 된다.
2. 맵 확대하는 메소드 작성
```kotlin
/* LocatrFragment */
private fun updateUI() {
    if (map == null || mapImage == null) return

    mapItem?./let/*{*
val itemPoint = LatLng(*it*.lat, *it*.lon)
        val myPoint = LatLng(currentLocation?./latitude/?: 0.0, currentLocation?./longitude/?: 0.0)
        val bounds = LatLngBounds.Builder().include(itemPoint).include(myPoint).build()
        val margin = /resources/.getDimensionPixelSize(R.dimen./map_inset_margin/)
        val update = CameraUpdateFactory.newLatLngBounds(bounds, margin)
        map?.animateCamera(update)
    *}*
}
```

맵 주위를 이동하기 위해서 CameraUpdate 객체를 생성한다.
CameraUpdateFactory는 서로 다른 종류의 CameraUpdate 객체를 생성하는 여러 가지 static 메소드를 갖고 있다. 이 객체를 사용하면 화면에 보이는 맵에서 위치나 확대 레벨 및 그 외 다른 속성들을 조정할 수 있다.
여기서는 특정 LatLngBounds의  CameraUpdate 객체를 생성한다. LatLngBounds는 지점들로 구성된 사각형 영역으로 생각할 수 있으며, 생성할 때는 사각형 왼쪽 아래 모서리 위치와 오른쪽 위 모서리 위치를 지정하면 된다. 

그러나 대게의 경우에는 사각형 영역에 포함시킬 지점들의 내역을 알려주는 것이 더 쉽다. 이때는 LatLngBounds.Builder를 사용하면 된다.

위의 updateUI()sms 맵을 최초로 바들 때와 플리커 사진의 검색이 끝났을 때 호출한다. 

## 맵 위에 그리기
맵 위에 그리기 -> 객체를 생성하여 GoogleMap에 추가하기
이런 객체들을 GoogleMap 객체가 생성한다. 대신에 개발자는 GoogleMap이 생성해주기 원하는 것을 나타내는 객체를 생성한다. 
: Options objects (옵션 객체)

```kotlin
fun updateUI() {
...
	val itemBitmap = 	BitmapDescriptorFactory.fromBitmap(mapImage)
	val itemMarker = MarkerOptions().position(itemPoint).icon(itemBitmap)
	val myMarker = MarkerOptions().position(myPoint)
	map?.clear()
	map?.addMarker(itemMarker)
	map?.addMarker(myMarker)
}
```



#android/책