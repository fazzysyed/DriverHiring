package fyp.saira.driverhiring;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fyp.saira.driverhiring.AllPostsWork.ShowRouteOnMap;
import fyp.saira.driverhiring.Services.IGoogleAPI;


public class TripDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txtData;
    private TextView txtFee;
    private TextView txtBaseFare;
    private TextView txtTime;
    private TextView txtDistance;
    private TextView txtEstimatedPyout;
    private TextView txtFrom;
    private TextView txtTo;
    private Button btnDoneRide;
    public double base_fare = 2.55;
    public double time_rate = 0.35;
    public double distance_rate = 1.75;
    private IGoogleAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mService = ShowRouteOnMap.getGoogleAPI();
        txtData = (TextView) findViewById(R.id.txtData);
        txtFee = (TextView) findViewById(R.id.txtFee);
        txtBaseFare = (TextView) findViewById(R.id.txtBaseFare);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtEstimatedPyout = (TextView) findViewById(R.id.txtEstimatedPyout);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtTo = (TextView) findViewById(R.id.txtTo);

        btnDoneRide = (Button) findViewById(R.id.btnDoneRide);

        btnDoneRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomePageMap.class));
                finish();

            }
        });

        if (getIntent().getStringExtra("startAdd") != null) {
            System.out.println("error is " + getIntent().getStringExtra("startAdd"));

            getPrice(getIntent().getStringExtra("startAdd"), getIntent().getStringExtra("endAdd"));
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    private void settingInformation(String total, String time, String distance, String startAdd, String endAddress) {


        System.out.println("inten data is " + total + time + distance + startAdd + endAddress);


        System.out.println("inten work");


        txtBaseFare.setText(base_fare + "");

        txtTime.setText(time + "min");
        txtDistance.setText(distance + "km");

        txtFrom.setText("From:\n" + startAdd);
        txtTo.setText("To:\n" + endAddress);

//
//        String[] location_end = endAddress.split(",");
//
//        LatLng dropoff = new LatLng(Double.parseDouble(location_end[0]), Double.parseDouble(location_end[1]));
//
//        mMap.addMarker(new MarkerOptions()
//                .position(dropoff)
//                .title("Drop off here")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropoff, 12.0f));





        txtEstimatedPyout.setText("Rs. "+ total);

        txtFee.setText("Rs. " + total);

        System.out.println("inten work45b " + total + time + distance);

        System.out.println("inten work4 " + String.format("$ %.2f", total));

        Calendar calender = Calendar.getInstance();
        String data = String.format("%s, %d/%d", convertToDayOfWeek(calender.get(Calendar.DAY_OF_WEEK)), calender.get(Calendar.DAY_OF_MONTH), calender.get(Calendar.MONTH));

        txtData.setText(data);



    }

    private void getPrice(String mLocation, String mDestination) {

        String requestUrl = "";


        requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                "mode=driving&" +
                "transit_routing_preference=less_driving&" +
                "origin=" + mLocation + "&"
                + "destination=" + mDestination + "&"
                + "key=" + getResources().getString(R.string.google_maps_key);
        System.out.println("my url is " + requestUrl);

        try {
            mService.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        System.out.println("my url 2 ");


                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);


                        //get distance

                        JSONObject distance = legsobject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                        //get time

                        JSONObject time = legsobject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));


                        System.err.println("data is gone" + formulaPrice(distance_value, time_value) + "" + String.valueOf(time_value) + "" + String.valueOf(distance_value) + "" + legsobject.getString("start_address") + "" + legsobject.getString("end_address"));

                        settingInformation(String.valueOf(formulaPrice(distance_value, time_value)), String.valueOf(time_value), String.valueOf(distance_value), legsobject.getString("start_address"), legsobject.getString("end_address"));


                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.i("error is true", response.toString());
                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            Log.e("on erroe 2", "erroe 2 ");
        }
    }

    public double formulaPrice(double km, int min) {
        return (base_fare + (time_rate * min) + distance_rate * km);
    }

    private String convertToDayOfWeek(int day) {

        switch (day) {

            case Calendar.SUNDAY:
                return "SUNDAY";

            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";


            default:
                return "UNK";

        }


    }

}
