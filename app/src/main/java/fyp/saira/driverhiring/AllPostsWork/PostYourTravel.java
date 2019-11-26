package fyp.saira.driverhiring.AllPostsWork;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import fyp.saira.driverhiring.HomePageMap;
import fyp.saira.driverhiring.R;

public class PostYourTravel extends AppCompatActivity {
    LatLng latLngPostStart, latLngPostEnd;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    //for time picker return time for round trip
    int Hour;
    int Minute;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private Toolbar myToolbar;
    private LinearLayout mainLayout;
    private LinearLayout offerLiftLayout;
    private RelativeLayout riderLayout;
    private ImageButton riderImage;
    private TextView riderText;
    private RelativeLayout vehicleLayout;
    private ImageButton vehicleImage;
    private TextView vehicleText;
    private ImageView routeArrow;
    private LinearLayout startPointLayout;
    private TextView startPointTx;
    private TextView offerLiftStartName;
    private TextView offerLiftStartAddress;
    private LinearLayout endPointLayout;
    private TextView endPointTx;
    private TextView offerLiftEndName;
    private TextView offerLiftEndAddress;
    private LinearLayout daysLayout;
    private Switch regularTripsw;
    private LinearLayout offerdaysLayout;
    private ToggleButton sat;
    private ToggleButton sun;
    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private LinearLayout startDateTimeLayout;
    private TextView offerdeparturedate;
    private TextView offerdeparturedateHeader;
    private TextView offerdeparturedateDetail;
    private Switch offerRoundTripSw;
    private LinearLayout returnTimeLayout;
    private TextView offerReturnTimetx;
    private TextView offerreturndateHeader;
    private TextView offerreturndateDetail;
    private Button offerLiftConfirm;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__your__travel);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Post");
        setSupportActionBar(myToolbar);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        offerLiftLayout = (LinearLayout) findViewById(R.id.offer_lift_layout);
        routeArrow = (ImageView) findViewById(R.id.route_arrow);
        startPointLayout = (LinearLayout) findViewById(R.id.start_point_layout);
        startPointTx = (TextView) findViewById(R.id.startPointTx);
        offerLiftStartName = (TextView) findViewById(R.id.offer_lift_start_name);
        offerLiftStartAddress = (TextView) findViewById(R.id.offer_lift_start_address);
        endPointLayout = (LinearLayout) findViewById(R.id.end_point_layout);
        endPointTx = (TextView) findViewById(R.id.endPointTx);
        offerLiftEndName = (TextView) findViewById(R.id.offer_lift_end_name);
        offerLiftEndAddress = (TextView) findViewById(R.id.offer_lift_end_address);
        daysLayout = (LinearLayout) findViewById(R.id.days_layout);
        regularTripsw = (Switch) findViewById(R.id.regularTripsw);
        offerRoundTripSw = (Switch) findViewById(R.id.offerRoundTripSw);
        offerdaysLayout = (LinearLayout) findViewById(R.id.offerdaysLayout);
        sat = (ToggleButton) findViewById(R.id.Sat);
        sun = (ToggleButton) findViewById(R.id.Sun);
        mon = (ToggleButton) findViewById(R.id.Mon);
        tue = (ToggleButton) findViewById(R.id.Tue);
        wed = (ToggleButton) findViewById(R.id.Wed);
        thu = (ToggleButton) findViewById(R.id.Thu);
        fri = (ToggleButton) findViewById(R.id.Fri);
        startDateTimeLayout = (LinearLayout) findViewById(R.id.startDateTimeLayout);
        offerdeparturedate = (TextView) findViewById(R.id.offerdeparturedate);
        offerdeparturedateHeader = (TextView) findViewById(R.id.offerdeparturedate_header);
        offerdeparturedateDetail = (TextView) findViewById(R.id.offerdeparturedate_detail);
        vehicleImage = (ImageButton) findViewById(R.id.vehicle_image);
        riderImage = (ImageButton) findViewById(R.id.rider_image);
        returnTimeLayout = (LinearLayout) findViewById(R.id.return_time_layout);
        offerReturnTimetx = (TextView) findViewById(R.id.offerReturnTimetx);
        offerreturndateHeader = (TextView) findViewById(R.id.offerreturndate_header);
        offerreturndateDetail = (TextView) findViewById(R.id.offerreturndate_detail);


        //switches
        regularTripsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    offerdaysLayout.setVisibility(View.VISIBLE);
                } else {
                    offerdaysLayout.setVisibility(View.GONE);

                }

            }
        });

        offerRoundTripSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    returnTimeLayout.setVisibility(View.VISIBLE);
                } else {
                    returnTimeLayout.setVisibility(View.GONE);

                }

            }
        });

        //textview to select location
        startPointTx.setSelected(true);
        startPointTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Select_location_for_Post.class);

                intent.putExtra("start", "start");

                startActivityForResult(intent, 2);

            }
        });
        endPointTx.setSelected(true);
        endPointTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Select_location_for_Post.class);

                intent.putExtra("start", "end");

                startActivityForResult(intent, 3);

            }
        });
        //toggle btns

        toggleClickListners();
    }

    public void toggleClickListners() {
        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sat.setTextColor(getResources().getColor(R.color.white));
                } else {

                    sat.setTextColor(getResources().getColor(R.color.black_text_color));

                }

            }
        });
        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sun.setTextColor(getResources().getColor(R.color.white));
                } else {

                    sun.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });
        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mon.setTextColor(getResources().getColor(R.color.white));
                } else {

                    mon.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });
        tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tue.setTextColor(getResources().getColor(R.color.white));
                } else {

                    tue.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });
        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wed.setTextColor(getResources().getColor(R.color.white));
                } else {

                    wed.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });
        thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    thu.setTextColor(getResources().getColor(R.color.white));
                } else {

                    thu.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });
        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fri.setTextColor(getResources().getColor(R.color.white));
                } else {

                    fri.setTextColor(getResources().getColor(R.color.black_text_color));

                }
            }
        });


        offerdeparturedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        offerReturnTimetx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialo = new DatePickerDialog(PostYourTravel.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //*************Call Time Picker Here ********************

                                // Get Current Time
                                Calendar c = Calendar.getInstance();
                                Hour = c.get(Calendar.HOUR_OF_DAY);
                                Minute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialo = new TimePickerDialog(PostYourTravel.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                Hour = hourOfDay;
                                                Minute = minute;

                                                offerReturnTimetx.setText(date_time + ", " + hourOfDay + ":" + minute);
                                            }
                                        }, Hour, Minute, false);
                                timePickerDialo.show();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialo.show();


            }
        });


        riderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable drawable = riderImage.getDrawable();

                if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.passenger_enabled).getConstantState())) {

                    riderImage.setImageResource(R.drawable.passenger_disabled);
                } else {
                    riderImage.setImageResource(R.drawable.passenger_enabled);

                }
            }
        });

        vehicleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = vehicleImage.getDrawable();

                if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.vehicle_enabled).getConstantState())) {

                    vehicleImage.setImageResource(R.drawable.vehicle_disabled);
                } else {
                    vehicleImage.setImageResource(R.drawable.vehicle_enabled);

                }
            }
        });

        findViewById(R.id.offerLiftConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
                String uidfromperfs = prefs.getString("uid", "nouid");
                String profileimgurl = prefs.getString("profileimageurl", "noimg");
                String fullname = prefs.getString("fullname", "noname");
                String vehtype = prefs.getString("vehicaltype", "novtype");
                String phoneno = prefs.getString("phoneno", "nophone");

                String latstart = String.valueOf(latLngPostStart.latitude);
                String lngstart = String.valueOf(latLngPostStart.longitude);

                String latend = String.valueOf(latLngPostEnd.latitude);
                String lngend = String.valueOf(latLngPostEnd.longitude);

                Drawable drawable = riderImage.getDrawable();
                Drawable drawabledriver = vehicleImage.getDrawable();

                if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.passenger_enabled).getConstantState())) {

                    final ProgressDialog progressDialog = new ProgressDialog(PostYourTravel.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    myRef = database.getReference("PostsAsPassenger");


                    DatabaseReference dbref = myRef.push();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", dbref.getKey());
                    map.put("uid", uidfromperfs);
                    map.put("profileimgurl", profileimgurl);
                    map.put("fullname", fullname);

                    map.put("latstart", latstart);
                    map.put("lngstart", lngstart);

                    map.put("latend", latend);
                    map.put("lngend", lngend);
                    map.put("phoneno", phoneno);

                    String regulartripstring = "";
                    if (regularTripsw.isChecked()) {


                        if (sat.isChecked()) {

                            regulartripstring = regulartripstring + "sat,";
                        }
                        if (sun.isChecked()) {

                            regulartripstring = regulartripstring + "sun,";
                        }
                        if (mon.isChecked()) {

                            regulartripstring = regulartripstring + "mon,";
                        }
                        if (tue.isChecked()) {

                            regulartripstring = regulartripstring + "tue,";
                        }
                        if (wed.isChecked()) {

                            regulartripstring = regulartripstring + "wed,";
                        }
                        if (thu.isChecked()) {

                            regulartripstring = regulartripstring + "thu,";
                        }
                        if (fri.isChecked()) {

                            regulartripstring = regulartripstring + "fri,";
                        }


                    }
                    map.put("regulartrip", regulartripstring);

                    map.put("startpoint", startPointTx.getText().toString());
                    map.put("endpoint", endPointTx.getText().toString());
                    map.put("departuredatetime", offerdeparturedate.getText().toString());

                    String roundtrip = "";
                    if (offerRoundTripSw.isChecked()) {

                        roundtrip = offerReturnTimetx.getText().toString();

                    }
                    map.put("roundtrip", roundtrip);

                    dbref.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostYourTravel.this, "Post Added", Toast.LENGTH_SHORT).show();
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

                } else if (drawabledriver.getConstantState().equals(getResources().getDrawable(R.drawable.vehicle_enabled).getConstantState())) {


                    String regulartripstring = "";
                    if (regularTripsw.isChecked()) {


                        if (sat.isChecked()) {

                            regulartripstring = regulartripstring + "sat,";
                        }
                        if (sun.isChecked()) {

                            regulartripstring = regulartripstring + "sun,";
                        }
                        if (mon.isChecked()) {

                            regulartripstring = regulartripstring + "mon,";
                        }
                        if (tue.isChecked()) {

                            regulartripstring = regulartripstring + "tue,";
                        }
                        if (wed.isChecked()) {

                            regulartripstring = regulartripstring + "wed,";
                        }
                        if (thu.isChecked()) {

                            regulartripstring = regulartripstring + "thu,";
                        }
                        if (fri.isChecked()) {

                            regulartripstring = regulartripstring + "fri,";
                        }


                    }


                    String startP = startPointTx.getText().toString();
                    String endP = endPointTx.getText().toString();
                    String depeDate = offerdeparturedate.getText().toString();

                    String roundtrip = "";
                    if (offerRoundTripSw.isChecked()) {

                        roundtrip = offerReturnTimetx.getText().toString();

                    }

                    Intent intent = new Intent(getApplicationContext(), PostTravelStep2ForDriver.class);

                    intent.putExtra("uid", uidfromperfs);

                    intent.putExtra("profileimgurl", profileimgurl);
                    intent.putExtra("fullname", fullname);

                    intent.putExtra("regulartripstring", regulartripstring);

                    intent.putExtra("startP", startP);
                    intent.putExtra("endP", endP);

                    intent.putExtra("latstart", latstart);
                    intent.putExtra("lngstart", lngstart);

                    intent.putExtra("latend", latend);
                    intent.putExtra("lngend", lngend);


                    intent.putExtra("depeDate", depeDate);
                    intent.putExtra("roundtrip", roundtrip);
                    intent.putExtra("vehicaltype", vehtype);
                    startActivity(intent);


                }


            }
        });


    }

    void timepicker2forround() {

    }

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        offerdeparturedate.setText(date_time + ", " + hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePageMap.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            String loc = data.getStringExtra("locationtxt");
            String lat = data.getStringExtra("latstart");
            String lng = data.getStringExtra("lngstart");

            latLngPostStart = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

            startPointTx.setText(loc + "");
        }
        if (requestCode == 3) {
            String loc = data.getStringExtra("locationtxt");
            String lat = data.getStringExtra("latend");
            String lng = data.getStringExtra("lngend");

            latLngPostEnd = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

            endPointTx.setText(loc + "");
        }
    }
}
