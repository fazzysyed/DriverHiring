package fyp.saira.driverhiring.AllPostsWork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fyp.saira.driverhiring.Helper.DirectionJSONParser;
import fyp.saira.driverhiring.HomePageMap;
import fyp.saira.driverhiring.ModelClasses.StartRideRequestModel;
import fyp.saira.driverhiring.R;
import fyp.saira.driverhiring.Services.IGoogleAPI;
import fyp.saira.driverhiring.Services.RetrofitClient;
import fyp.saira.driverhiring.TripDetails;

public class ShowRouteOnMap extends FragmentActivity implements OnMapReadyCallback {

    public static final String baseURL = "https://maps.googleapis.com";
    private static final int[] COLORS = new int[]{R.color.primary_dark1, R.color.primary1, R.color.primary_light1, R.color.accent1, R.color.primary_dark};
    //new
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    String startL, startLN, endL, endLN, title = "";
    LatLng start, waypoint, end;
    Polyline direction;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    IGoogleAPI mService;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private Marker driverMarker;
    private String currentUserUid, type, fullname;


    Button endrideforpostbtn;
    private String keyToStopRide;

    public static IGoogleAPI getGoogleAPI() {

        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService = getGoogleAPI();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        endrideforpostbtn = findViewById(R.id.endrideforpost);


        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");
        type = prefs.getString("type", "");
        fullname = prefs.getString("fullname", "");


        if (type.equals("driver")) {
            endrideforpostbtn.setVisibility(View.VISIBLE);
            endrideforpostbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("CurrentRides").child(keyToStopRide);

                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                String latStart = startL+","+endL;
                                String latEnd = startLN+","+endLN;


                                Intent intent = new Intent(ShowRouteOnMap.this, TripDetails.class);
                                intent.putExtra("startAdd",latStart);
                                intent.putExtra("endAdd",latEnd);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Ride completed", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                    startActivity(new Intent(getApplicationContext(), HomePageMap.class));
                    finish();

                }
            });
        }
    }

    private void buildLocationCallBack() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    mLastLocation = location;
                }
                displayLocation();
            }
        };

    }


    private void displayLocation() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {


                            if (location != null) {

                                mLastLocation = location;


                                double latitude = mLastLocation.getLatitude();
                                double longitude = mLastLocation.getLongitude();


                                if (driverMarker != null)
                                    driverMarker.remove();
                                driverMarker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title("You")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_pin)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));


                                if (direction != null) {
                                    direction.remove();
                                }


                                if (keyToStopRide == null || keyToStopRide.equals("")) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CurrentRides").push();


                                    keyToStopRide = ref.getKey();

                                    StartRideRequestModel startRideRequestModel = new StartRideRequestModel();
                                    startRideRequestModel.setRideid(ref.getKey());
                                    startRideRequestModel.setDriveruid(currentUserUid);
                                    startRideRequestModel.setDrivername(fullname);
                                    startRideRequestModel.setSourcelat(String.valueOf(latitude));
                                    startRideRequestModel.setSourcelng(String.valueOf(longitude));
                                    startRideRequestModel.setDestenationlat(startLN);
                                    startRideRequestModel.setDestenationlng(endLN);

                                    ref.setValue(startRideRequestModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(ShowRouteOnMap.this, "Done", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    getDirection();
                                } else {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CurrentRides").child(keyToStopRide);


                                    keyToStopRide = ref.getKey();

                                    HashMap<String, Object> map = new HashMap<>();

                                    map.put("sourcelat", String.valueOf(latitude));
                                    map.put("sourcelng", String.valueOf(longitude));


                                    ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(ShowRouteOnMap.this, "updated", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    getDirection();
                                }
                            }


                        }
                    });


        } else {

            Toast.makeText(this, "give location permission first ", Toast.LENGTH_SHORT).show();
        }


    }


    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10);


    }

    void getIntentdata() {


        startL = getIntent().getStringExtra("latstart");
        startLN = getIntent().getStringExtra("latend");
        endL = getIntent().getStringExtra("lngstart");
        endLN = getIntent().getStringExtra("lngend");

        title = getIntent().getStringExtra("title");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        try {
            boolean isSucess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
            if (!isSucess) {
                Toast.makeText(this, "Map style error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        mMap = googleMap;

        //new settings for map
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        getIntentdata();


        start = new LatLng(Double.parseDouble(startL), Double.parseDouble(endL));

        end = new LatLng(Double.parseDouble(startLN), Double.parseDouble(endLN));


//        start = new LatLng(33.997797, 71.490276);
//        waypoint= new LatLng(18.01455, -77.499333);
//        end = new LatLng(34.014011, 71.544101);

//


        // End marker


        mMap.addMarker(new MarkerOptions().position(end).title("End Point"));


        if (!title.equals("request")) {

            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(new RoutingListener() {
                        @Override
                        public void onRoutingFailure(RouteException e) {
                            Toast.makeText(ShowRouteOnMap.this, "failer", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onRoutingStart() {
                            Toast.makeText(ShowRouteOnMap.this, "start", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onRoutingSuccess(ArrayList<Route> route, int position) {
                            Toast.makeText(ShowRouteOnMap.this, "Success", Toast.LENGTH_SHORT).show();


//                        if(polylines.size()>0) {
//                            for (Polyline poly : polylines) {
//                                poly.remove();
//                            }
//                        }

                            polylines = new ArrayList<>();
                            //add route(s) to the map.
                            for (int i = 0; i < route.size(); i++) {

                                //In case of more than 5 alternative routes
                                int colorIndex = i % COLORS.length;

                                PolylineOptions polyOptions = new PolylineOptions();
                                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                                polyOptions.width(10 + i * 3);
                                polyOptions.addAll(route.get(i).getPoints());
                                Polyline polyline = mMap.addPolyline(polyOptions);
                                polylines.add(polyline);

                                Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();


                            }

                            // Start marker
                            mMap.addMarker(new MarkerOptions().position(start).title("Start Point"));

                            // End marker

                            mMap.addMarker(new MarkerOptions().position(end).title("End Point"));


                            CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(13.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            mMap.animateCamera(cameraUpdate);

                        }

                        @Override
                        public void onRoutingCancelled() {
                            Toast.makeText(ShowRouteOnMap.this, "cancelled", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .waypoints(start, end)
                    .key(getResources().getString(R.string.google_maps_key))
//                .alternativeRoutes(true)
                    .build();
            routing.execute();


        } else {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                buildLocationRequest();
                buildLocationCallBack();

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                displayLocation();
            }

        }
    }

    private void getDirection() {
        LatLng currentPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());


        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode = driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + currentPosition.latitude + "," + currentPosition.longitude + "&" +
                    "destination=" + startLN + "," + endLN + "&" +
                    "key=" + getResources().getString(R.string.google_maps_key);
            Log.d("Usama", requestApi);//print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {

                                new ParserTask().execute(response.body().toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {

        }

    }


   
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        ProgressDialog mDialog = new ProgressDialog(ShowRouteOnMap.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Please Wait.....");
            mDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {

                jObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                routes = parser.parse(jObject);


            } catch (Exception e) {

            }

            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mDialog.dismiss();

            ArrayList points;
            PolylineOptions polylineOptions = null;

            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);

            }

            if (direction != null) {
                direction.remove();
            }
            direction = mMap.addPolyline(polylineOptions);

        }
    }

}
