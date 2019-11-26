package fyp.saira.driverhiring.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import fyp.saira.driverhiring.Adatpters.UserAdapter;
import fyp.saira.driverhiring.ModelClasses.Chatlist;
import fyp.saira.driverhiring.ModelClasses.DriverModel;
import fyp.saira.driverhiring.ModelClasses.RiderModel;
import fyp.saira.driverhiring.ModelClasses.User;
import fyp.saira.driverhiring.Notifications.Token;
import fyp.saira.driverhiring.R;

import static android.content.Context.MODE_PRIVATE;


public class ChatsFragment extends Fragment {

    FirebaseUser fuser;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Chatlist> usersList;
    private String type_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences("saveddata", MODE_PRIVATE);

        type_user = prefs.getString("type", "");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {

        if (type_user.equals("rider")) {

            mUsers = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference("Drivers");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DriverModel user = snapshot.getValue(DriverModel.class);
                        for (Chatlist chatlist : usersList) {
                            if (user.getUid().equals(chatlist.getId())) {

                                mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));


                            }
                        }
                    }
                    userAdapter = new UserAdapter(getActivity(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (type_user.equals("driver")) {


            mUsers = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference("Riders");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RiderModel user = snapshot.getValue(RiderModel.class);
                        for (Chatlist chatlist : usersList) {
                            if (user.getUid().equals(chatlist.getId())) {
                                mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));

                                System.err.println("userdatac " + user.getFullname());

                            }
                        }
                    }
                    userAdapter = new UserAdapter(getActivity(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
