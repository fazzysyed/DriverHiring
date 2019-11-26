package fyp.saira.driverhiring.Adatpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fyp.saira.driverhiring.AllPostsWork.ScreenAfterPostIsSelectedFromList;
import fyp.saira.driverhiring.ModelClasses.PostDriver;
import fyp.saira.driverhiring.ModelClasses.PostRider;
import fyp.saira.driverhiring.ModelClasses.RequestsModel;
import fyp.saira.driverhiring.ProfilePageStuff.RecivedRequestsActivity;
import fyp.saira.driverhiring.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    boolean isDriverP = true;
    String isProfile = "false";


    ArrayList<String> dataremoveTemp = new ArrayList<>();

    private ArrayList<PostDriver> list = new ArrayList<>();
    private ArrayList<PostRider> listrider = new ArrayList<>();


    public PostAdapter(Context context, ArrayList<PostDriver> list, ArrayList<PostRider> listrider, boolean isDriverP) {
        this.list = list;
        this.listrider = listrider;
        this.isDriverP = isDriverP;
        this.context = context;
    }

    public PostAdapter(boolean isDriverP, Context context, ArrayList<PostDriver> list, ArrayList<PostRider> listrider, String isProfile) {
        this.isDriverP = isDriverP;
        this.context = context;
        this.list = list;
        this.listrider = listrider;
        this.isProfile = isProfile;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row_rec_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final int pos = position;
        System.err.println("working " + position);

        if (isProfile.equals("false")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);

                System.err.println("name is is " + obj.getFullname());
                System.err.println("name 2  is  " + list.get(pos).getFullname());
                System.err.println("driver is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ScreenAfterPostIsSelectedFromList.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("noofpassenger", obj.getNoofpassenger());
                        intent.putExtra("offermessage", obj.getOffermessage());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("vehicaltype", obj.getVehicaltype());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "driver");
                        intent.putExtra("fareamount", obj.getFareamount());
                        intent.putExtra("fullname", obj.getFullname());
                        context.startActivity(intent);


                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final PostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());

                System.err.println("rider is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ScreenAfterPostIsSelectedFromList.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "passenger");
                        intent.putExtra("fullname", obj.getFullname());

                        context.startActivity(intent);

                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }
        } else if (isProfile.equals("true")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);

                System.err.println("name is is " + obj.getFullname());
                System.err.println("name 2  is  " + list.get(pos).getFullname());
                System.err.println("driver is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RecivedRequestsActivity.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("noofpassenger", obj.getNoofpassenger());
                        intent.putExtra("offermessage", obj.getOffermessage());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("vehicaltype", obj.getVehicaltype());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "driver");
                        intent.putExtra("fareamount", obj.getFareamount());
                        intent.putExtra("fullname", obj.getFullname());
                        context.startActivity(intent);


                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final PostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());

                System.err.println("rider is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RecivedRequestsActivity.class);
                        intent.putExtra("departuredatetime", obj.getDeparturedatetime());
                        intent.putExtra("endpoint", obj.getEndpoint());
                        intent.putExtra("id", obj.getId());
                        intent.putExtra("latend", obj.getLatend());
                        intent.putExtra("latstart", obj.getLatstart());
                        intent.putExtra("lngend", obj.getLngend());
                        intent.putExtra("lngstart", obj.getLngstart());
                        intent.putExtra("profileimgurl", obj.getProfileimgurl());
                        intent.putExtra("regulartrip", obj.getRegulartrip());
                        intent.putExtra("roundtrip", obj.getRoundtrip());
                        intent.putExtra("startpoint", obj.getStartpoint());
                        intent.putExtra("uid", obj.getUid());
                        intent.putExtra("phoneno", obj.getPhoneno());
                        intent.putExtra("typeOfIntent", "passenger");
                        intent.putExtra("fullname", obj.getFullname());

                        context.startActivity(intent);

                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }

        } else if (isProfile.equals("not")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);


                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        System.err.println("pro here");

                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(context, holder.mainLayout);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.menu_select_request_option, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {


                                switch (item.getTitle().toString()) {
                                    case "Accept Request":

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accepted Requests").child(obj.getUid()).push();

                                        RequestsModel requestsModel = new RequestsModel();
                                        requestsModel.setEndpoint(obj.getEndpoint());
                                        requestsModel.setStartpoint(obj.getStartpoint());
                                        requestsModel.setImgurl(obj.getProfileimgurl());
                                        requestsModel.setPostid(obj.getPhoneno());
                                        requestsModel.setSendername(obj.getFullname());
                                        requestsModel.setSenderid(obj.getUid());
                                        requestsModel.setReciverid(obj.getNoofpassenger());

                                        requestsModel.setId(ref.getKey());

                                        requestsModel.setStatus("accepted");


                                        ref.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        });

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(user.getUid()).child(obj.getPhoneno()).push();


                                        ref2.setValue(requestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//dofrom here
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request Accepted, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        });


                                        break;

                                    case "Reject Request":
                                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Declined Requests").child(obj.getUid()).push();

                                        RequestsModel requestsM = new RequestsModel();
                                        requestsM.setEndpoint(obj.getEndpoint());
                                        requestsM.setStartpoint(obj.getStartpoint());
                                        requestsM.setImgurl(obj.getProfileimgurl());
                                        requestsM.setPostid(obj.getId());
                                        requestsM.setSendername(obj.getFullname());
                                        requestsM.setSenderid(obj.getUid());
                                        requestsM.setReciverid(obj.getNoofpassenger());

                                        requestsM.setId(ref3.getKey());

                                        requestsM.setStatus("accepted");

                                        System.err.println("myid is" + obj.getNoofpassenger());
                                        System.err.println("myid is" + obj.getPhoneno());
                                        System.err.println("myid is" + obj.getId());


                                        ref3.setValue(requestsM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Requests").child(obj.getNoofpassenger()).child(obj.getPhoneno()).child(obj.getId());

                                                    ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        });


                                        break;


                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu
                    }
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final PostRider obj = listrider.get(position);

                System.err.println("nameR is is " + obj.getFullname());
                System.err.println("nameR 2  is  " + listrider.get(pos).getFullname());

                System.err.println("rider is here");

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference r = FirebaseDatabase.getInstance().getReference("PostsAsDriver").child(obj.getPhoneno());


                        r.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                PostDriver postDriver = dataSnapshot.getValue(PostDriver.class);

                                Intent intent = new Intent(context, ScreenAfterPostIsSelectedFromList.class);
                                intent.putExtra("departuredatetime", postDriver.getDeparturedatetime());
                                intent.putExtra("endpoint", postDriver.getEndpoint());
                                intent.putExtra("id", postDriver.getId());
                                intent.putExtra("latend", postDriver.getLatend());
                                intent.putExtra("latstart", postDriver.getLatstart());
                                intent.putExtra("lngend", postDriver.getLngend());
                                intent.putExtra("lngstart", postDriver.getLngstart());
                                intent.putExtra("noofpassenger", postDriver.getNoofpassenger());
                                intent.putExtra("offermessage", postDriver.getOffermessage());
                                intent.putExtra("profileimgurl", postDriver.getProfileimgurl());
                                intent.putExtra("regulartrip", postDriver.getRegulartrip());
                                intent.putExtra("roundtrip", postDriver.getRoundtrip());
                                intent.putExtra("startpoint", postDriver.getStartpoint());
                                intent.putExtra("uid", postDriver.getUid());
                                intent.putExtra("vehicaltype", postDriver.getVehicaltype());
                                intent.putExtra("phoneno", postDriver.getPhoneno());
                                intent.putExtra("typeOfIntent", "driver");
                                intent.putExtra("fareamount", postDriver.getFareamount());
                                intent.putExtra("fullname", postDriver.getFullname());
                                context.startActivity(intent);


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
                });

                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }
        } else if (isProfile.equals("Daccept")) {

            if (isDriverP) {

                final PostDriver obj = list.get(position);


                holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();


                        //TODO

                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(fuser.getUid()).child(obj.getId());


                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
//
//                                    System.err.println("mykeyis"+ds.getKey());
//                                    dataremoveTemp.add(ds.getKey());
//
//                                }
//
//                                DatabaseReference ref3 = FirebaseDatabase.getInstance()
//                                        .getReference("Driver Accepted Requests")
//                                        .child(fuser.getUid())
//                                        .child(obj.getId())
//                                        .child(dataremoveTemp.get(pos));
//
//
//                                ref3.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                    }
//                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        return false;
                    }
                });


                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


            } else {

                final PostRider obj = listrider.get(position);


                holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Driver Accepted Requests").child(fuser.getUid()).child(obj.getId());

                        ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "Request removed, list will be updated on next launch", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                        return false;
                    }
                });


                holder.nametxt.setText(obj.getFullname());
                holder.startpointtxt.setText(obj.getStartpoint());
                holder.startpointtxt.setSelected(true);

                holder.endpointtxt.setText(obj.getEndpoint());
                holder.endpointtxt.setSelected(true);


                Picasso.get()
                        .load(obj.getProfileimgurl())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.ic_close)
                        .into(holder.user_profile_img_rec_post);
            }
        }

    }

    @Override
    public int getItemCount() {

        if (isDriverP) {
            return list.size();

        } else {
            return listrider.size();

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView mainLayout;
        private TextView startpointtxt;
        private TextView endpointtxt;
        private TextView nametxt;
        private CircularImageView user_profile_img_rec_post;

        public MyViewHolder(View view) {
            super(view);
            startpointtxt = (TextView) view.findViewById(R.id.startpointtxt);
            endpointtxt = (TextView) view.findViewById(R.id.endpointtxt);
            nametxt = (TextView) view.findViewById(R.id.name_rec_post);
            mainLayout = view.findViewById(R.id.main_layout);
            user_profile_img_rec_post = (CircularImageView) view.findViewById(R.id.user_profile_img_rec_post);
        }
    }
}
