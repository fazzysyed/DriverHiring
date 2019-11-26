package fyp.saira.driverhiring.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fyp.saira.driverhiring.Adatpters.UserAdapter;
import fyp.saira.driverhiring.ModelClasses.DriverModel;
import fyp.saira.driverhiring.ModelClasses.RiderModel;
import fyp.saira.driverhiring.ModelClasses.User;
import fyp.saira.driverhiring.R;

import static android.content.Context.MODE_PRIVATE;


public class UsersFragment extends Fragment {

    EditText search_users;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers = new ArrayList<>();

    private String type_user;
    private List<DriverModel> mDriver = new ArrayList<>();

    private List<RiderModel> mRider = new ArrayList<>();
    private String uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("saveddata", MODE_PRIVATE);

        type_user = prefs.getString("type", "");

        uid = prefs.getString("uid", "");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {

        if (type_user.equals("rider")) {


            final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
            Query query = FirebaseDatabase.getInstance().getReference("Drivers").orderByChild("fullname")
                    .startAt(s)
                    .endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DriverModel user = snapshot.getValue(DriverModel.class);


                        assert user != null;
                        assert fuser != null;
                        if (!user.getUid().equals(fuser.getUid())) {
                            mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (type_user.equals("driver")) {

            final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
            Query query = FirebaseDatabase.getInstance().getReference("Riders").orderByChild("fullname")
                    .startAt(s)
                    .endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RiderModel user = snapshot.getValue(RiderModel.class);

                        System.err.println("riderData" + user.getFullname());
                        assert user != null;
                        assert fuser != null;
                        if (!user.getUid().equals(fuser.getUid())) {
                            mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void readUsers() {


        if (type_user.equals("rider")) {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Drivers");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (search_users.getText().toString().equals("")) {
                        mUsers.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DriverModel user = snapshot.getValue(DriverModel.class);


                            System.err.printf("mydata" + user.getFullname());

                            //    if (!user.getUid().equals(firebaseUser.getUid())) {


                            mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));
                            //  }

                        }

                        userAdapter = new UserAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (type_user.equals("driver")) {


            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Riders");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (search_users.getText().toString().equals("")) {
                        mUsers.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RiderModel user = snapshot.getValue(RiderModel.class);

                            if (!user.getUid().equals(firebaseUser.getUid())) {


                                mUsers.add(new User(user.getUid(), user.getFullname(), user.getProfileimageurl(), user.getOnline()));
                            }

                        }

                        userAdapter = new UserAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
