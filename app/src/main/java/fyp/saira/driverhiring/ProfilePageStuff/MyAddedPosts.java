package fyp.saira.driverhiring.ProfilePageStuff;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import fyp.saira.driverhiring.R;

import static android.content.Context.MODE_PRIVATE;


public class MyAddedPosts extends Fragment {

    String currentUserUid;
    PostAdapter postAdapter;
    ArrayList<PostDriver> postDriverArrayList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<PostRider> listrider = new ArrayList<>();
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_added_posts, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences("saveddata", MODE_PRIVATE);
        currentUserUid = prefs.getString("uid", "");
        type = prefs.getString("type", "");

        recyclerView = view.findViewById(R.id.rec_my_posts);


        if (type.equals("driver")) {
            myRef = FirebaseDatabase.getInstance().getReference("PostsAsDriver");

            postAdapter = new PostAdapter(true, getActivity(), postDriverArrayList, listrider, "true");
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(postAdapter);

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnap, @Nullable String s) {

                    //to avoid nuul exception
                    listrider.add(new PostRider("", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                    PostDriver value = dataSnap.getValue(PostDriver.class);

                    if (currentUserUid.equals(value.getUid())) {
                        postDriverArrayList.add(value);
                        postAdapter.notifyDataSetChanged();

                        System.err.println("driver is " + value.getFullname());

                    }

                    System.err.println("driver oput is " + value.getFullname());

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
        } else if (type.equals("rider")) {

            myRef = FirebaseDatabase.getInstance().getReference("PostsAsPassenger");

            postAdapter = new PostAdapter(true, getActivity(), postDriverArrayList, listrider, "true");
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(postAdapter);

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //to avoid nuul exception
                    postDriverArrayList.add(new PostDriver("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                    PostRider value = dataSnapshot.getValue(PostRider.class);

                    if (currentUserUid.equals(value.getUid())) {
                        listrider.add(value);
                        postAdapter.notifyDataSetChanged();
                        System.err.println("rider is " + value.getFullname());

                    }

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


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
