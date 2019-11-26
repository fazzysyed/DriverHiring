package fyp.saira.driverhiring.Requests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fyp.saira.driverhiring.Adatpters.PostAdapter;
import fyp.saira.driverhiring.AllPostsWork.ShowRouteOnMap;
import fyp.saira.driverhiring.ModelClasses.PostDriver;
import fyp.saira.driverhiring.ModelClasses.PostRider;
import fyp.saira.driverhiring.ModelClasses.RequestsModel;
import fyp.saira.driverhiring.Notifications.Client;
import fyp.saira.driverhiring.Notifications.MyResponse;
import fyp.saira.driverhiring.Notifications.SenderMessageForRequest;
import fyp.saira.driverhiring.Notifications.Token;
import fyp.saira.driverhiring.ProfilePageStuff.MyAddedPosts;
import fyp.saira.driverhiring.R;
import fyp.saira.driverhiring.Services.APIService;

public class AcceptActivityPart2 extends AppCompatActivity {

    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    ArrayList<RequestsModel> requestsModelArrayList = new ArrayList<>();
    Button startrideforpostbtn;
    String idfromint;
    APIService apiService;
    private MyAddedPosts.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();
    private String type;
    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_part2);


        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");
        type = prefs.getString("type", "");
        fullname = prefs.getString("fullname", "");
        recyclerView = findViewById(R.id.rec_acceptedreq_posts);

        startrideforpostbtn = findViewById(R.id.startrideforpost);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        if (getIntent() != null) {
            idfromint = getIntent().getStringExtra("myid");
        }

        if (getIntent() != null) {


            idfromint = getIntent().getStringExtra("myid");

            System.err.println("mychect" + idfromint + "?????" + currentUserUid);
            if (type.equals("driver")) {

                Toast.makeText(this, "Long press on item to delete it!", Toast.LENGTH_SHORT).show();

                myRef = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(currentUserUid).child(idfromint);

                postAdapter = new PostAdapter(true, this, postDriverArrayList, listrider, "Daccept");
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(postAdapter);


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            RequestsModel mode = ds.getValue(RequestsModel.class);

                            requestsModelArrayList.add(mode);

                            //to avoid nuul exception
                            postDriverArrayList.add(new PostDriver("", mode.getPostid(), "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getReciverid(), "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid(), ""));


                            listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                            postAdapter.notifyDataSetChanged();
                            System.err.println("driver oput is " + mode.getSendername());

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } else if (type.equals("rider")) {

                myRef = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(currentUserUid);

                postAdapter = new PostAdapter(false, this, postDriverArrayList, listrider, "not");
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(postAdapter);


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        System.err.println("mydata" + dataSnapshot.getKey());
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            //to avoid nuul exception
                            postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                            RequestsModel mode = ds.getValue(RequestsModel.class);

                            System.err.println("mydata" + dataSnapshot.getValue());

                            listrider.add(new PostRider("", "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid()));


                            postAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//            myRef.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                        //to avoid nuul exception
//                        postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//
//
//                        RequestsModel mode = ds.getValue(RequestsModel.class);
//
//                        System.err.println("mydata" + mode.getStartpoint());
//
//                        listrider.add(new PostRider("", "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid()));
//
//
//                        postAdapter.notifyDataSetChanged();
//
//                    }
//
//
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            }

        } else {
            Toast.makeText(this, "Error!! Reload App", Toast.LENGTH_SHORT).show();
        }


        startrideforpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AcceptActivityPart2.this);
                builder1.setMessage("Are You Sure You Want to Start Ride?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                                // sendrequesttodriversforridestart();


                                DatabaseReference re = FirebaseDatabase.getInstance().getReference("PostsAsDriver").child(idfromint);

                                re.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        PostDriver driver = dataSnapshot.getValue(PostDriver.class);


                                        Intent intent = new Intent(AcceptActivityPart2.this, ShowRouteOnMap.class);

                                        intent.putExtra("latstart", driver.getLatstart());

                                        intent.putExtra("latend", driver.getLatend());
                                        intent.putExtra("lngstart", driver.getLngstart());
                                        intent.putExtra("lngend", driver.getLngend());
                                        intent.putExtra("title", "request");


                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


    }


    void sendrequesttodriversforridestart() {


        for (int i = 0; i < requestsModelArrayList.size(); i++) {

            final int p = i;
            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
            Query query = tokens.orderByKey().equalTo(requestsModelArrayList.get(i).getSenderid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Token token = snapshot.getValue(Token.class);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("title", "request");
                        map.put("reciver", requestsModelArrayList.get(p).getSenderid());
                        map.put("drivername", fullname);

                        SenderMessageForRequest sender = new SenderMessageForRequest(map, token.getToken());

                        apiService.sendRequestNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200) {
                                            if (response.body().success != 1) {
                                                Toast.makeText(AcceptActivityPart2.this, "Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
