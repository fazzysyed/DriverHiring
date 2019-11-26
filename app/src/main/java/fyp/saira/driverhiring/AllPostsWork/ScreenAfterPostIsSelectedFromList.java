package fyp.saira.driverhiring.AllPostsWork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import fyp.saira.driverhiring.ChatStuff.MessageActivity;
import fyp.saira.driverhiring.HomePageMap;
import fyp.saira.driverhiring.R;


public class ScreenAfterPostIsSelectedFromList extends AppCompatActivity implements View.OnClickListener {

    String departuredatetime, endpoint, fullname, id, latend, latstart, lngend, lngstart, noofpassenger,
            offermessage, fareamount, phoneno, profileimgurl, regulartrip, roundtrip, startpoint, uid, vehicaltype;
    String nameOfCurrentLoginUser, phoneNoOfCurrentLoginUser;
    private Toolbar myToolbar;
    private TextView inviteFriendsId;
    private LinearLayout mainLayout;
    private ScrollView scrollView1;
    private ImageView offerDetailsImage;
    private TextView offerDetailsName;
    private TextView offerTypeMessage;
    private RelativeLayout watchVideoLayout;
    private RelativeLayout contactLayout;
    private ImageView routeArrow;
    private TextView offerDetailsStartName;
    private TextView offerDetailsStartAddress;
    private TextView offerDetailsEndName;
    private TextView offerDetailsEndAddress;
    private LinearLayout offerDetailstimeLayout;
    private LinearLayout startTimeLayout;
    private TextView startTimeHeader;
    private TextView startTimeDetailsDate;
    private TextView startTimeDetailsTime;
    private LinearLayout timeDividerLine;
    private LinearLayout returnTimeLayout;
    private TextView returnTimeHeader;
    private TextView returnTimeDetailsDate;
    private TextView returnTimeDetailsTime;
    private LinearLayout offerdetailsdaysLayout;
    private TextView offerDetailsSat;
    private TextView offerDetailsSun;
    private TextView offerDetailsMon;
    private TextView offerDetailsTue;
    private TextView offerDetailsWed;
    private TextView offerDetailsThu;
    private TextView offerDetailsFri;
    private TextView[] array_of_txt;
    private LinearLayout offerDetailsDaysLine;
    private LinearLayout vehicleTypeLayout;
    private TextView vehicleTypeTx;
    private TextView vehicleTypeDetailTx;
    private LinearLayout fareAmountLayout;
    private TextView fareAmountHeader;
    private TextView fareAmountDetails;
    private LinearLayout mobileDetailsLayout;
    private LinearLayout mobileNumberLayout;
    private TextView mobileHeaderTx;
    private TextView mobileDetailsTx;
    private LinearLayout offerMessageLayout;
    private TextView offermessageDetailsTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_after_post_is_selected_from_list);

        initiViews();

        if (getIntent() != null) {
            if (getIntent().getStringExtra("typeOfIntent") != null) {

                if (getIntent().getStringExtra("typeOfIntent").equals("driver")) {
                    getIntentDataForDrivers();

                    setUpDataForDriver();

                } else if (getIntent().getStringExtra("typeOfIntent").equals("passenger")) {

                    getIntentDataForPassengers();
                    setUpDataForPassenger();
                }

            }

        }
        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        nameOfCurrentLoginUser = prefs.getString("fullname", "");
        phoneNoOfCurrentLoginUser = prefs.getString("phoneno", "");


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePageMap.class));
        finish();
    }

    private void getIntentDataForDrivers() {

        departuredatetime = getIntent().getStringExtra("departuredatetime");
        endpoint = getIntent().getStringExtra("endpoint");
        fullname = getIntent().getStringExtra("fullname");
        id = getIntent().getStringExtra("id");
        latend = getIntent().getStringExtra("latend");
        latstart = getIntent().getStringExtra("latstart");
        lngend = getIntent().getStringExtra("lngend");
        lngstart = getIntent().getStringExtra("lngstart");
        noofpassenger = getIntent().getStringExtra("noofpassenger");
        profileimgurl = getIntent().getStringExtra("profileimgurl");
        regulartrip = getIntent().getStringExtra("regulartrip");
        roundtrip = getIntent().getStringExtra("roundtrip");
        startpoint = getIntent().getStringExtra("startpoint");
        uid = getIntent().getStringExtra("uid");
        phoneno = getIntent().getStringExtra("phoneno");
        vehicaltype = getIntent().getStringExtra("vehicaltype");
        offermessage = getIntent().getStringExtra("offermessage");
        fareamount = getIntent().getStringExtra("fareamount");
    }

    void setUpDataForDriver() {
        offerTypeMessage.setText("is offering a ride");

        offerDetailsName.setText(fullname);

        System.err.println("name is " + getIntent().getStringExtra("fullname"));
        System.err.println("fareamount is " + fareamount);
        offermessageDetailsTx.setText(offermessage);

        mobileDetailsTx.setText(phoneno);

        fareAmountDetails.setText(fareamount);

        vehicleTypeDetailTx.setText(vehicaltype);

        String split[] = regulartrip.split(",");

//        array_of_txt[0].setVisibility(View.VISIBLE);
//        System.err.println("day is "+array_of_txt[0].getText().toString());
//        System.err.println("day from up is "+split[0]);

        int len = split.length;

        int len2 = array_of_txt.length;

        for (int i = 0; i < len; i++) {

            for (int j = 0; j < len2; j++) {

                if (split[i].toLowerCase().equals(array_of_txt[j].getText().toString().toLowerCase())) {

                    array_of_txt[j].setVisibility(View.VISIBLE);
                }


            }

        }


        String split2[] = departuredatetime.split(",");

        if (split2.length < 2) {

            System.err.println("time isa" + split2[0] + "----" + split2[1]);

            startTimeDetailsDate.setText(split2[0]);
            startTimeDetailsTime.setText(split2[1]);
        }

        String split3[] = roundtrip.split(",");

        if (split3.length < 2) {
            returnTimeDetailsDate.setText(split3[0]);
            returnTimeDetailsTime.setText(split3[1]);

        }
        offerDetailsStartAddress.setText(startpoint);
        offerDetailsEndAddress.setText(endpoint);

        Picasso.get()
                .load(profileimgurl)
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.ic_close)
                .into(offerDetailsImage);


    }

    private void getIntentDataForPassengers() {

        departuredatetime = getIntent().getStringExtra("departuredatetime");
        endpoint = getIntent().getStringExtra("endpoint");
        fullname = getIntent().getStringExtra("fullname");
        id = getIntent().getStringExtra("id");
        latend = getIntent().getStringExtra("latend");
        latstart = getIntent().getStringExtra("latstart");
        lngend = getIntent().getStringExtra("lngend");
        lngstart = getIntent().getStringExtra("lngstart");
        noofpassenger = getIntent().getStringExtra("noofpassenger");
        profileimgurl = getIntent().getStringExtra("profileimgurl");
        regulartrip = getIntent().getStringExtra("regulartrip");
        roundtrip = getIntent().getStringExtra("roundtrip");
        startpoint = getIntent().getStringExtra("startpoint");
        uid = getIntent().getStringExtra("uid");
        phoneno = getIntent().getStringExtra("phoneno");


    }

    private void setUpDataForPassenger() {

        //vehical
        offerMessageLayout.setVisibility(View.GONE);
        fareAmountLayout.setVisibility(View.GONE);
        vehicleTypeLayout.setVisibility(View.GONE);


        offerTypeMessage.setText("Wants to take a ride");
        offerDetailsName.setText(fullname);


        String split[] = roundtrip.split(",");

        int len = split.length;
        int len2 = array_of_txt.length;

        for (int i = 0; i < len; i++) {

            for (int j = 0; j < len; j++) {

                if (split[i].equals(array_of_txt[j].getText().toString())) {

                    array_of_txt[j].setVisibility(View.VISIBLE);
                }

            }

        }

        String split2[] = departuredatetime.split(",");

        startTimeDetailsDate.setText(split2[0]);
        startTimeDetailsTime.setText(split2[1]);


        String split3[] = roundtrip.split(",");

        returnTimeDetailsDate.setText(split3[0]);
        returnTimeDetailsTime.setText(split3[1]);


        offerDetailsStartAddress.setText(startpoint);
        offerDetailsEndAddress.setText(endpoint);

        Picasso.get()
                .load(profileimgurl)
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.ic_close)
                .into(offerDetailsImage);

    }


    void initiViews() {


        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        inviteFriendsId = (TextView) findViewById(R.id.inviteFriendsId);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        offerDetailsImage = (ImageView) findViewById(R.id.offer_details_image);
        offerDetailsName = (TextView) findViewById(R.id.offer_details_name);
        offerTypeMessage = (TextView) findViewById(R.id.offer_type_message);
        watchVideoLayout = (RelativeLayout) findViewById(R.id.watch_video_layout);
        findViewById(R.id.send_request_btn).setOnClickListener(this);
        contactLayout = (RelativeLayout) findViewById(R.id.contact_layout);
        findViewById(R.id.call_bt).setOnClickListener(this);
        findViewById(R.id.start_chat_btn).setOnClickListener(this);
        findViewById(R.id.sms_bt).setOnClickListener(this);
        routeArrow = (ImageView) findViewById(R.id.route_arrow);
        offerDetailsStartName = (TextView) findViewById(R.id.offer_details_start_name);
        offerDetailsStartAddress = (TextView) findViewById(R.id.offer_details_start_address);
        offerDetailsEndName = (TextView) findViewById(R.id.offer_details_end_name);
        offerDetailsEndAddress = (TextView) findViewById(R.id.offer_details_end_address);
        findViewById(R.id.check_route_bt).setOnClickListener(this);
        offerDetailstimeLayout = (LinearLayout) findViewById(R.id.offer_detailstimeLayout);
        startTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        startTimeHeader = (TextView) findViewById(R.id.start_time_header);
        startTimeDetailsDate = (TextView) findViewById(R.id.start_time_details_date);
        startTimeDetailsTime = (TextView) findViewById(R.id.start_time_details_time);
        timeDividerLine = (LinearLayout) findViewById(R.id.time_divider_line);
        returnTimeLayout = (LinearLayout) findViewById(R.id.return_time_layout);
        returnTimeHeader = (TextView) findViewById(R.id.return_time_header);
        returnTimeDetailsDate = (TextView) findViewById(R.id.return_time_details_date);
        returnTimeDetailsTime = (TextView) findViewById(R.id.return_time_details_time);
        offerdetailsdaysLayout = (LinearLayout) findViewById(R.id.offerdetailsdaysLayout);


        offerDetailsSat = (TextView) findViewById(R.id.offer_details_sat);
        offerDetailsSun = (TextView) findViewById(R.id.offer_details_sun);
        offerDetailsMon = (TextView) findViewById(R.id.offer_details_mon);
        offerDetailsTue = (TextView) findViewById(R.id.offer_details_tue);
        offerDetailsWed = (TextView) findViewById(R.id.offer_details_wed);
        offerDetailsThu = (TextView) findViewById(R.id.offer_details_thu);
        offerDetailsFri = (TextView) findViewById(R.id.offer_details_fri);

        array_of_txt = new TextView[]{offerDetailsSat, offerDetailsSun, offerDetailsMon, offerDetailsTue, offerDetailsTue, offerDetailsWed, offerDetailsThu, offerDetailsFri};

        offerDetailsDaysLine = (LinearLayout) findViewById(R.id.offerDetails_daysLine);
        vehicleTypeLayout = (LinearLayout) findViewById(R.id.vehicle_type_layout);
        vehicleTypeTx = (TextView) findViewById(R.id.vehicleTypeTx);
        vehicleTypeDetailTx = (TextView) findViewById(R.id.vehicleTypeDetailTx);
        fareAmountLayout = (LinearLayout) findViewById(R.id.fare_amount_layout);
        fareAmountHeader = (TextView) findViewById(R.id.Fare_amount_header);
        fareAmountDetails = (TextView) findViewById(R.id.fare_amount_Details);
        mobileDetailsLayout = (LinearLayout) findViewById(R.id.mobile_details_layout);
        mobileNumberLayout = (LinearLayout) findViewById(R.id.mobile_number_layout);
        mobileHeaderTx = (TextView) findViewById(R.id.mobileHeaderTx);
        mobileDetailsTx = (TextView) findViewById(R.id.mobileDetailsTx);
        offerMessageLayout = (LinearLayout) findViewById(R.id.offer_message_layout);
        offermessageDetailsTx = (TextView) findViewById(R.id.offermessage_detailsTx);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_request_btn:
                SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);

                String uid2 = prefs.getString("uid", "");
                String nameo = prefs.getString("fullname", "");
                String profileima = prefs.getString("profileimageurl", "");


                if (!uid2.equals("")) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests")
                            .child(uid).child(id).push();


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", ref.getKey());
                    map.put("senderid", uid2);
                    map.put("reciverid", uid);
                    map.put("postid", id);
                    map.put("sendername", nameo);
                    map.put("imgurl", profileima);
                    map.put("startpoint", startpoint);
                    map.put("endpoint", endpoint);
                    map.put("status", "pending");

                    ref.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ScreenAfterPostIsSelectedFromList.this, "Done!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.call_bt:

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneno));//change the number.
                startActivity(callIntent);

                break;
            case R.id.start_chat_btn:

                if (!uid.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

                    intent.putExtra("userid", uid);

                    startActivity(intent);
                }

                break;
            case R.id.sms_bt:
                String message = "Hello iRide User,\n\nThis is to inform you that " + nameOfCurrentLoginUser + " wants to share ride with you," +
                        " if you wish to do the same kindly respond to him on his contact no (" + phoneNoOfCurrentLoginUser + ") \n\nThank You \niRide Pakistan";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("smsto:"));
                i.setType("vnd.android-dir/mms-sms");
                i.putExtra("address", phoneno);
                i.putExtra("sms_body", message);
                startActivity(Intent.createChooser(i, "Send sms via:"));

                break;
            case R.id.check_route_bt:


                Intent intent = new Intent(getApplicationContext(), ShowRouteOnMap.class);

                intent.putExtra("latstart", latstart);
                intent.putExtra("latend", latend);
                intent.putExtra("lngstart", lngstart);
                intent.putExtra("lngend", lngend);
                startActivity(intent);

                break;
        }
    }

}
