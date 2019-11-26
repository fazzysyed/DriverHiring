package fyp.saira.driverhiring.ProfilePageStuff;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fyp.saira.driverhiring.Adatpters.PostAdapter;
import fyp.saira.driverhiring.ModelClasses.PostDriver;
import fyp.saira.driverhiring.ModelClasses.PostRider;
import fyp.saira.driverhiring.ModelClasses.RequestsModel;
import fyp.saira.driverhiring.R;

public class RecivedRequestsActivity extends AppCompatActivity {
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();
    private String currentUserUid;
    private String type_user;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recived_requests);

        toolbar = findViewById(R.id.req_toolbar);
        if (getIntent() != null) {
            postId = getIntent().getStringExtra("id");

            System.err.println("podtid" + postId);
        }


        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");


        type_user = prefs.getString("type", "");

        recyclerView = findViewById(R.id.rec_my_requests1);

        myRef = FirebaseDatabase.getInstance().getReference("Requests").child(currentUserUid).child(postId);


        if (type_user.equals("driver")) {


            postAdapter = new PostAdapter(true, getApplicationContext(), postDriverArrayList, listrider, "not");
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(postAdapter);


            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnap, @Nullable String s) {
                    RequestsModel mode = dataSnap.getValue(RequestsModel.class);

                    //to avoid nuul exception
                    postDriverArrayList.add(new PostDriver("", mode.getPostid(), "", mode.getEndpoint(), mode.getSendername(), mode.getId(), "", "", "", "", mode.getReciverid(), "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid(), ""));


                    listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                    toolbar.setTitle("Recived Requests For Post (" + postDriverArrayList.size() + ")");
                    postAdapter.notifyDataSetChanged();

                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (type_user.equals("rider")) {

            postAdapter = new PostAdapter(false, getApplicationContext(), postDriverArrayList, listrider, "not");
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(postAdapter);
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnap, @Nullable String s) {


                    //to avoid nuul exception
                    postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                    RequestsModel mode = dataSnap.getValue(RequestsModel.class);


                    listrider.add(new PostRider("", "", mode.getEndpoint(), mode.getSendername(), postId, "", "", "", "", mode.getImgurl(), "", "", mode.getStartpoint(), mode.getSenderid()));

                    toolbar.setTitle("Recived Requests For Post (" + listrider.size() + ")");

                    postAdapter.notifyDataSetChanged();

                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }
}
