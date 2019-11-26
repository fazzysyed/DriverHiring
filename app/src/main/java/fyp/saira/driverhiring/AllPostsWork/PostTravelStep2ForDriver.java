package fyp.saira.driverhiring.AllPostsWork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import fyp.saira.driverhiring.HomePageMap;
import fyp.saira.driverhiring.R;

public class PostTravelStep2ForDriver extends AppCompatActivity {


    String uid, profileimgurl, fullname, regulartripstring, startP, endP, depeDate, roundtrip, vehtype;
    LatLng latLngPostStart, latLngPostEnd;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private Toolbar myToolbar;

    private Spinner vehicletypeSpinner;

    private EditText fareAmountEt, offermessage, noOfPassenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_travel_step2_for_driver);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Driver Post");
        setSupportActionBar(myToolbar);

        vehicletypeSpinner = (Spinner) findViewById(R.id.vehicletype_spinner);
        fareAmountEt = (EditText) findViewById(R.id.fareAmountEt);
        offermessage = (EditText) findViewById(R.id.message_detailsTx);
        noOfPassenger = (EditText) findViewById(R.id.no_of_passengersEdtx);


        if (!getIntent().getStringExtra("uid").equals("")) {
            getIntentData();
        }
        database = FirebaseDatabase.getInstance();

        findViewById(R.id.offerLiftConfirmbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!getIntent().getStringExtra("uid").equals("")) {

                    String offmess = offermessage.getText().toString();
                    String noofP = noOfPassenger.getText().toString();

                    SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);

                    String phoneno = prefs.getString("phoneno", "nophone");

                    final ProgressDialog progressDialog = new ProgressDialog(PostTravelStep2ForDriver.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    myRef = database.getReference("PostsAsDriver");


                    DatabaseReference dbref = myRef.push();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", dbref.getKey());
                    map.put("uid", uid);
                    map.put("profileimgurl", profileimgurl);
                    map.put("regulartrip", regulartripstring);
                    map.put("fareamount", fareAmountEt.getText().toString());

                    map.put("startpoint", startP);
                    map.put("endpoint", endP);
                    map.put("departuredatetime", depeDate);

                    map.put("roundtrip", roundtrip);
                    map.put("vehicaltype", vehtype);
                    map.put("fullname", fullname);
                    map.put("offermessage", offmess);
                    map.put("noofpassenger", noofP);
                    map.put("phoneno", phoneno);

                    map.put("latstart", String.valueOf(latLngPostStart.latitude));
                    map.put("lngstart", String.valueOf(latLngPostStart.longitude));

                    map.put("latend", String.valueOf(latLngPostEnd.latitude));
                    map.put("lngend", String.valueOf(latLngPostEnd.longitude));


                    dbref.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostTravelStep2ForDriver.this, "Post Added", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), HomePageMap.class));
                            finish();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            progressDialog.dismiss();
                        }
                    });

                }


            }
        });


        findViewById(R.id.positive_increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(fareAmountEt.getText().toString());
                int b = a + 1;
                if (b > -1) {
                    fareAmountEt.setText(b + "");

                }
            }
        });
        findViewById(R.id.negative_increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(fareAmountEt.getText().toString());
                int b = a - 1;
                if (b > -1) {
                    fareAmountEt.setText(b + "");

                }
            }
        });

    }


    public void getIntentData() {

        uid = getIntent().getStringExtra("uid");
        profileimgurl = getIntent().getStringExtra("profileimgurl");
        fullname = getIntent().getStringExtra("fullname");
        regulartripstring = getIntent().getStringExtra("regulartripstring");
        startP = getIntent().getStringExtra("startP");
        endP = getIntent().getStringExtra("endP");
        depeDate = getIntent().getStringExtra("depeDate");
        roundtrip = getIntent().getStringExtra("roundtrip");

        vehtype = getIntent().getStringExtra("vehicaltype");

        String startLat = getIntent().getStringExtra("latstart");
        String startLng = getIntent().getStringExtra("lngstart");

        latLngPostStart = new LatLng(Double.parseDouble(startLat), Double.parseDouble(startLng));

        String endLat = getIntent().getStringExtra("latend");
        String endLng = getIntent().getStringExtra("lngend");

        latLngPostEnd = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLng));

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PostYourTravel.class));
        finish();
    }
}
