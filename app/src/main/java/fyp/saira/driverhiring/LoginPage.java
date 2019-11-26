package fyp.saira.driverhiring;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import fyp.saira.driverhiring.PhoneVerify.ActivityPhoneVerify;

public class LoginPage extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public int test = 9;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText emailText;
    EditText passwordText;
    Button loginButton, signupButton;
    TextView resetpass;
    private FirebaseAuth mAuth;
    private boolean isPermission = false;
    private boolean isDriver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        String restoredText = prefs.getString("isLogedin", "false");

        if (restoredText.equals("true") && mAuth != null) {

            startActivity(new Intent(getApplicationContext(), HomePageMap.class));
            finish();
        }

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        resetpass = findViewById(R.id.reset_pass);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.pass);
        loginButton = findViewById(R.id.loginBtn);
        signupButton = findViewById(R.id.signup);

        if (!requestPermission()) {
            requestPermission();
        }

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                builder.setTitle("Reset Password");
                builder.setMessage("Enter Email");
                final EditText input = new EditText(LoginPage.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();

                        if (!email.equals("") && !email.equals(null)) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginPage.this, "Check Your Inbox For Reset Email", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(LoginPage.this, loginButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_select_login_option, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString()) {
                            case "Login As Driver":
                                isDriver = true;
                                login();
                                break;

                            case "Login As Passenger":
                                isDriver = false;
                                login();
                                break;


                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ActivityPhoneVerify.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    private boolean requestPermission() {


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.isAnyPermissionPermanentlyDenied()) {
                    final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(LoginPage.this);
                    dialog.setTitle("Location Permission")
                            .setMessage("You have to give location permission!!, the app will close now")

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    finishAffinity();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    isPermission = true;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

        return isPermission;
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginPage.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (isDriver) {
                                myRef = database.getReference("Drivers");

                            } else {
                                myRef = database.getReference("Riders");

                            }

                            myRef.orderByChild("uid").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                        String status = datas.child("status").getValue().toString();

                                        if (status.equals("false")) {
                                            progressDialog.dismiss();
                                            onAccountBlocked();
                                            return;

                                        }


                                        SharedPreferences.Editor editor = getSharedPreferences("saveddata", MODE_PRIVATE).edit();
                                        editor.putString("currentlogitude", datas.child("currentlogitude").getValue().toString());
                                        editor.putString("currentlatitude", datas.child("currentlatitude").getValue().toString());
                                        editor.putString("uid", datas.child("uid").getValue().toString());
                                        editor.putString("email", email);
                                        editor.putString("isLogedin", "true");

                                        if (isDriver) {
                                            editor.putString("type", "driver");
                                            editor.putString("vehicaltype", datas.child("vehicaltype").getValue().toString());

                                            editor.putString("vehicalname", datas.child("vehicalname").getValue().toString());
                                            editor.putString("vehicalnoplate", datas.child("vehicalnoplate").getValue().toString());

                                        } else {
                                            editor.putString("type", "rider");

                                        }

                                        editor.putString("profileimageurl", datas.child("profileimageurl").getValue().toString());
                                        editor.putString("fullname", datas.child("fullname").getValue().toString());
                                        editor.putString("cnicno", datas.child("cnicno").getValue().toString());
                                        editor.putString("phoneno", datas.child("phoneno").getValue().toString());
                                        editor.putString("provence", datas.child("provence").getValue().toString());
                                        editor.putString("city", datas.child("city").getValue().toString());
                                        editor.putString("designetion", datas.child("designetion").getValue().toString());
                                        editor.putString("age", datas.child("age").getValue().toString());

                                        editor.apply();

                                        onLoginSuccess();
                                        // onLoginFailed();
                                        progressDialog.dismiss();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(LoginPage.this, "Canceled ",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    loginButton.setEnabled(true);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            loginButton.setEnabled(true);


                        }

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {


                this.finish();
            }
        }
    }


    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        startActivity(new Intent(this, HomePageMap.class));
        finish();
    }

    public void onAccountBlocked() {
        loginButton.setEnabled(true);


        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(this);
        diaBuilder.setTitle("Account Blocked");
        diaBuilder.setCancelable(false);
        diaBuilder.setMessage("Your Account Has been blocked! for further information contact the admin at admin@admin.com");
        diaBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        diaBuilder.show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}