package fyp.saira.driverhiring;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fyp.saira.driverhiring.AllPostsWork.ListAllPosts;
import fyp.saira.driverhiring.AllPostsWork.PostYourTravel;
import fyp.saira.driverhiring.ChatStuff.MainActivity;
import fyp.saira.driverhiring.ModelClasses.StartRideRequestModel;
import fyp.saira.driverhiring.ModelClasses.User;
import fyp.saira.driverhiring.ProfilePageStuff.ProfilePage;
import fyp.saira.driverhiring.Requests.AllRequests;

public class HomePageMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    FloatingActionButton fab_homepage;
    ///navigasstion
    ImageView ImageViewNav;
    TextView EmailUserTxt, TypeUserTxt;
    String photo_url_user, email_user, type_user;
    ArrayList<User> data = new ArrayList<>();
    BottomAppBar bottomapp;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser fuser;
    private EditText name;
    private EditText age;
    private Button addData;
    private Button getData;
    private RecyclerView showDataTxt;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean isPermission;
    private DrawerLayout drawer;
    private DatabaseReference reference;
    private String uid;
    private String fullname;

    private List<Polyline> polylines;
    ArrayList<StartRideRequestModel> rideRequestModelArrayList = new ArrayList<>();

    private static final int[] COLORS = new int[]{R.color.primary_dark1, R.color.primary1, R.color.primary_light1, R.color.accent1, R.color.primary_dark};
    private LatLng start;
    private LatLng end;
    private boolean showRides = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_page_map);
        bottomapp = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomapp);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
        }

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //it was pre written
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation(); //check whether location service is enable or not in your  phone


        }

        fab_homepage = (FloatingActionButton) findViewById(R.id.fab_homepage);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bottomapp, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        photo_url_user = prefs.getString("profileimageurl", "");
        email_user = prefs.getString("email", "");
        type_user = prefs.getString("type", "");
        fullname = prefs.getString("fullname", "");

        View headerview = navigationView.getHeaderView(0);

        ImageViewNav = (ImageView) headerview.findViewById(R.id.ImageViewNav);
        EmailUserTxt = (TextView) headerview.findViewById(R.id.UsernameTxtNav);
        TypeUserTxt = (TextView) headerview.findViewById(R.id.PhoneNumberTxtNav);

        if (type_user.equals("rider")) {


            Picasso.get()
                    .load(photo_url_user)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ImageViewNav);

            EmailUserTxt.setText(email_user);
            TypeUserTxt.setText(fullname + "\n\t\t\t" + type_user);


        } else if (type_user.equals("driver")) {
            EmailUserTxt.setText(email_user);
            TypeUserTxt.setText(fullname + "\n\t\t\t" + type_user);
            Picasso.get()
                    .load(photo_url_user)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ImageViewNav);


        }

        fab_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PostYourTravel.class));
                finish();
            }
        });


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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        //   mMap.getUiSettings().setZoomControlsEnabled(true);

        if (latLng != null) {
            mMap.clear();
            //  mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation)));
            // CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            //  mMap.animateCamera(cameraUpdate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            mMap.setMyLocationEnabled(true);

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng2) {

//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions()
//                          .position(latLng2)
//                          .title("Your Selected Location"));
//
//
//                    System.err.println("your address is "+getCompleteAddressString(latLng2.latitude,latLng2.longitude));

            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed. Error: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        String currentlogitude = prefs.getString("currentlogitude", "");
        String currentlatitude = prefs.getString("currentlatitude", "");
        String uid = prefs.getString("uid", "");


        if (type_user.equals("driver")) {
            myRef = database.getReference("Drivers").child(uid);


            HashMap<String, String> updateLocationMap = new HashMap<>();
            updateLocationMap.put("currentlogitude", String.valueOf(location.getLongitude()));
            updateLocationMap.put("currentlatitude", String.valueOf(location.getLatitude()));

            myRef.updateChildren((HashMap) updateLocationMap);
        } else if (type_user.equals("rider")) {

            myRef = database.getReference("Riders").child(uid);


            HashMap<String, String> updateLocationMap = new HashMap<>();
            updateLocationMap.put("currentlogitude", String.valueOf(location.getLongitude()));
            updateLocationMap.put("currentlatitude", String.valueOf(location.getLatitude()));

            myRef.updateChildren((HashMap) updateLocationMap);
        }

    }


    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        }
        status("false");
        currentUser("none");

    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        //Single Permission is granted
//                        Toast.makeText(getApplicationContext(), "Single permission is granted!", Toast.LENGTH_SHORT).show();
//                        isPermission = true;
//
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        // check for permanent denial of permission
//                        if (response.isPermanentlyDenied()) {
//                            isPermission = false;
//
//
//                        }
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(HomePageMap.this);
//                        dialog.setTitle("Location Permission")
//                                .setMessage("You have to give location permission!!, the app will close now")
//
//                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                                        finishAffinity();
//                                    }
//                                });
//                        dialog.setCancelable(false);
//                        dialog.show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CALL_PHONE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.isAnyPermissionPermanentlyDenied()) {
                    // check for permanent denial of permission

                    isPermission = false;


                    final AlertDialog.Builder dialog = new AlertDialog.Builder(HomePageMap.this);
                    dialog.setTitle("Permissions")
                            .setMessage("You have to give all permissions!!, the app will close now")

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finishAffinity();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();

                } else if (report.areAllPermissionsGranted()) {

                    //Single Permission is granted
                    Toast.makeText(getApplicationContext(), "permissions are granted!", Toast.LENGTH_SHORT).show();
                    isPermission = true;

                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


        return isPermission;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.navigation_explore) {

            startActivity(new Intent(getApplicationContext(), ListAllPosts.class));
            finish();

        }

        if (item.getItemId() == R.id.navigation_profile) {


            if (type_user.equals("rider")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(HomePageMap.this);
                builder1.setMessage("Are You Sure You Want to Show Current Rides?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Show",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                showRides = true;

                                // sendrequesttodriversforridestart();

                                if (showRides) {

                                    showCurrentRides();
                                }

                            }
                        });

                builder1.setNegativeButton(
                        "Stop Showing",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                stopShowingCurrentRides();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                Toast.makeText(this, "You are not a Rider", Toast.LENGTH_SHORT).show();
            }

        }
        if (item.getItemId() == R.id.navigation_profile22) {

            startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    void showCurrentRides() {


        DatabaseReference re = FirebaseDatabase.getInstance().getReference("CurrentRides");

        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    final StartRideRequestModel requestModel = ds.getValue(StartRideRequestModel.class);

                    // rideRequestModelArrayList.add(requestModel);

                    System.err.println("mylat" + requestModel.getSourcelat());

                    start = new LatLng(Double.parseDouble(requestModel.getSourcelat()), Double.parseDouble(requestModel.getSourcelng()));

                    end = new LatLng(Double.parseDouble(requestModel.getDestenationlat()), Double.parseDouble(requestModel.getDestenationlng()));

                    if (showRides) {
                        Routing routing = new Routing.Builder()
                                .travelMode(Routing.TravelMode.DRIVING)
                                .withListener(new RoutingListener() {
                                    @Override
                                    public void onRoutingFailure(RouteException e) {
                                        Toast.makeText(HomePageMap.this, "failer", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onRoutingStart() {
                                        Toast.makeText(HomePageMap.this, "start", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onRoutingSuccess(ArrayList<Route> route, int position) {
                                        Toast.makeText(HomePageMap.this, "Success", Toast.LENGTH_SHORT).show();


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


//                                  CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(13.0f).build();
//                                  CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                                  mMap.animateCamera(cameraUpdate);

                                    }

                                    @Override
                                    public void onRoutingCancelled() {
                                        Toast.makeText(HomePageMap.this, "cancelled", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .waypoints(start, end)
                                .key(getResources().getString(R.string.google_maps_key))
//                .alternativeRoutes(true)
                                .build();
                        routing.execute();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void stopShowingCurrentRides() {

        showRides = false;

        polylines.clear();
        mMap.clear();


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profileMenu) {
            startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            finish();
        } else if (id == R.id.findserviceMenu) {
            startActivity(new Intent(getApplicationContext(), PostYourTravel.class));
            finish();

        } else if (id == R.id.messangerMenue) {
            startActivity(new Intent(HomePageMap.this, MainActivity.class));
            finish();
        } else if (id == R.id.contactMenu) {
            Toast.makeText(this, "contactMenu", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.requests) {
            startActivity(new Intent(getApplicationContext(), AllRequests.class));
            finish();
        } else if (id == R.id.logoutMenu) {


            SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);

            type_user = prefs.getString("type", "");


            SharedPreferences.Editor editor = getSharedPreferences("saveddata", MODE_PRIVATE).edit();
            editor.putString("currentlogitude", "");
            editor.putString("currentlatitude", "");
            editor.putString("uid", "");
            editor.putString("email", "");
            editor.putString("isLogedin", "false");

            if (type_user.equals("driver")) {
                editor.putString("type", "");
                editor.putString("vehicaltype", "");

                editor.putString("vehicalname", "");
                editor.putString("vehicalnoplate", "");

            }

            editor.putString("profileimageurl", "");
            editor.putString("fullname", "");
            editor.putString("cnicno", "");
            editor.putString("phoneno", "");
            editor.putString("provence", "");
            editor.putString("city", "");
            editor.putString("designetion", "");
            editor.putString("age", "");

            editor.apply();

            Toast.makeText(this, "Logged Out Successful", Toast.LENGTH_SHORT).show();

            if (mAuth != null) {
                mAuth.signOut();
            }
            startActivity(new Intent(getApplicationContext(), LoginPage.class));


        } else if (id == R.id.nav_shareNav) {
            Toast.makeText(this, "nav_shareNav", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sendNav) {
            Toast.makeText(this, "nav_sendNav", Toast.LENGTH_SHORT).show();

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void status(String status) {

        if (type_user.equals("driver")) {

            reference = FirebaseDatabase.getInstance().getReference("Drivers").child(fuser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", status);

            reference.updateChildren(hashMap);


        } else if (type_user.equals("rider")) {
            reference = FirebaseDatabase.getInstance().getReference("Riders").child(fuser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", status);

            reference.updateChildren(hashMap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("true");
        currentUser(fuser.getUid());
    }

    @Override
    protected void onPause() {
        super.onPause();

        status("false");
        currentUser("none");
    }

    private void currentUser(String userid) {
        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);

        uid = prefs.getString("uid", "");


        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

}