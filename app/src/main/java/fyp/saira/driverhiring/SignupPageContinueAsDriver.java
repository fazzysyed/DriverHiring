package fyp.saira.driverhiring;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class SignupPageContinueAsDriver extends AppCompatActivity {


    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference myRef;
    Spinner genderSpinner, vehecaleType;
    ImageView selectImageV;
    String firstName, lastName, cnicSignup, phonenoSignup, signupProvence, signupCity, emailText, passwordTxt;
    String selectedImgURI = "";
    HashMap<String, String> map = new HashMap<>();
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page_continue_as_driver);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        genderSpinner = findViewById(R.id.spinner_gender);
        vehecaleType = findViewById(R.id.spinner_vehecle_type);

        getDataFromIntent();

        String[] genderdata = {"Male", "Female", "Other"};

        ArrayAdapter<String> genadapter = new ArrayAdapter<>(SignupPageContinueAsDriver.this, R.layout.custom_spinner_layout, genderdata);
        genadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        String[] vehicaldata = {"Car", "Bike", "Jeep", "Van", "Other"};

        ArrayAdapter<String> vehicaladapter = new ArrayAdapter<>(SignupPageContinueAsDriver.this, R.layout.custom_spinner_layout, vehicaldata);
        vehicaladapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genadapter);
        vehecaleType.setAdapter(vehicaladapter);

        selectImageV = findViewById(R.id.selectImageV);
        selectImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(SignupPageContinueAsDriver.this, selectImageV);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_select_image_selection_type, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString()) {
                            case "Select From Gallery":
                                get_gallery_image();
                                break;

                            case "Take Picture":
                                openCamera();
                                break;


                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

        });
        findViewById(R.id.signupBtnContDriver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    void getDataFromIntent() {

        firstName = getIntent().getStringExtra("fname");
        lastName = getIntent().getStringExtra("lname");
        cnicSignup = getIntent().getStringExtra("cnic");
        phonenoSignup = getIntent().getStringExtra("phoneno");
        signupProvence = getIntent().getStringExtra("provence");
        signupCity = getIntent().getStringExtra("city");


        emailText = getIntent().getStringExtra("email");
        passwordTxt = getIntent().getStringExtra("pass");

    }


    public void signup() {


        final ProgressDialog progressDialog = new ProgressDialog(SignupPageContinueAsDriver.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        final String email = emailText;

        final String password = passwordTxt;
        String reEnterPassword = passwordTxt;

        // TODO: Implement your own signup logic here.
        if (!email.equals("") && !password.equals("") && !reEnterPassword.equals("")) {

            if (password.equals(reEnterPassword)) {


                Uri file = Uri.fromFile(new File(selectedImgURI));
                System.out.println("file path " + file.toString());

                final StorageReference riversRef = mStorageRef.child("profileimages/" + email);


                UploadTask uploadTask = riversRef.putFile(file);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {


                            final Uri downloadUri = task.getResult();
                            System.err.println("Upload " + downloadUri);


                            if (downloadUri != null) {


                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignupPageContinueAsDriver.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {


                                                    // Sign in success, update UI with the signed-in user's information
                                                    user = mAuth.getCurrentUser();

                                                    myRef = database.getReference("Drivers");


                                                    DatabaseReference pushref = myRef.child(user.getUid());


                                                    String imageStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                                                    System.err.println("Upload " + imageStringLink);

                                                    map.put("uid", user.getUid());

                                                    map.put("email", email);
                                                    map.put("status", "true");
                                                    map.put("reported", "false");

                                                    map.put("fullname", firstName + " " + lastName);
                                                    map.put("cnicno", cnicSignup);
                                                    map.put("phoneno", phonenoSignup);
                                                    map.put("provence", signupProvence);
                                                    map.put("city", signupCity);
                                                    map.put("designetion", getDesignationSignup().getText().toString());

                                                    map.put("age", getAgeSignup().getText().toString());
                                                    map.put("vehicalname", getVehicleNameSignup().getText().toString());
                                                    map.put("vehicalnoplate", getVehicleNumberPlateSignup().getText().toString());

                                                    map.put("verified", "false");
                                                    map.put("currentlogitude", "");
                                                    map.put("currentlatitude", "");
                                                    map.put("profileimageurl", imageStringLink);
                                                    map.put("gender", genderSpinner.getSelectedItem().toString());
                                                    map.put("vehicaltype", vehecaleType.getSelectedItem().toString());

                                                    map.put("online", "true");
                                                    map.put("locationsharing", "true");


                                                    pushref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();

                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(getApplicationContext(), LoginPage.class));


                                                            } else {
                                                                Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });


                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(SignupPageContinueAsDriver.this, "User Creation failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();


                                                }

                                                // ...
                                            }
                                        });


                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error Uploading", Toast.LENGTH_LONG).show();

                        }
                    }
                });


            } else {
                Toast.makeText(this, "Password Not Matching!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter Empty Feilds!", Toast.LENGTH_LONG).show();
        }

    }


    public void get_gallery_image() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, PICK_FROM_GALLARY);

    }

    public void openCamera() {

        /* For Image capture from camera */
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);

    }


    private EditText getDesignationSignup() {
        return (EditText) findViewById(R.id.designation_signup);
    }

    private EditText getAgeSignup() {
        return (EditText) findViewById(R.id.age_signup);
    }

    private EditText getVehicleNameSignup() {
        return (EditText) findViewById(R.id.vehicle_name_signup);
    }

    private EditText getVehicleNumberPlateSignup() {
        return (EditText) findViewById(R.id.vehicle_number_plate_signup);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_FROM_CAMERA: {
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
                    selectImageV.setImageBitmap(bitmapImage);


                    selectedImgURI = data.getDataString();

                    System.err.println("path " + selectedImgURI);
                }
                break;
            }
            case PICK_FROM_GALLARY: {

                if (requestCode == PICK_FROM_GALLARY && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    selectImageV.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cursor.close();

                    selectedImgURI = picturePath;
                    System.err.println("path " + picturePath);

                }


            }


        }
    }


}
