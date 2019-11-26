package fyp.saira.driverhiring.ProfilePageStuff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import fyp.saira.driverhiring.HomePageMap;
import fyp.saira.driverhiring.R;

public class ProfilePage extends AppCompatActivity implements
        MySentRequests.OnFragmentInteractionListener,
        MyAddedPosts.OnFragmentInteractionListener {


    CircularImageView circularImageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar app_toolbar;
    AppBarLayout appBarLayout;
    TextView UserNameTxt, TypeUserTxt, EmailUserTxt;
    private String email_user, type_user, photo_url_user, fullname_user;
    private ImageView ImageViewNav, main_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        circularImageView = (CircularImageView) findViewById(R.id.image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        app_toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);


        app_toolbar.setTitle("My Profile");

        setSupportActionBar(app_toolbar);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                int j = collapsingToolbarLayout.getExpandedTitleMarginTop() * 2;


                float f = ((float) (i + j)) / ((float) j);
                circularImageView.setScaleX(f >= 0.0f ? f : 0.0f);
                CircularImageView circularImagev = circularImageView;
                if (f < 0.0f) {
                    f = 0.0f;
                }
                circularImagev.setScaleY(f);
            }
        });


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("My Posts", MyAddedPosts.class)
                .add("Approved Requests", MySentRequests.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_profilepage);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab_profilepage);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Toast.makeText(ProfilePage.this, "Changed to " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        SharedPreferences prefs = getSharedPreferences("saveddata", MODE_PRIVATE);
        photo_url_user = prefs.getString("profileimageurl", "");
        email_user = prefs.getString("email", "");
        type_user = prefs.getString("type", "");
        fullname_user = prefs.getString("fullname", "");


        ImageViewNav = (ImageView) findViewById(R.id.image_header);
        main_image = (ImageView) findViewById(R.id.image);
        UserNameTxt = (TextView) findViewById(R.id.name_txt_profile);
        TypeUserTxt = (TextView) findViewById(R.id.type_txt_profie);
        EmailUserTxt = (TextView) findViewById(R.id.email_txt_profie);

        if (type_user.equals("rider")) {

            UserNameTxt.setText(fullname_user);
            TypeUserTxt.setText(type_user);
            EmailUserTxt.setText(email_user);

//
//            Picasso.get()
//                    .load(photo_url_user)
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .error(R.drawable.ic_launcher_background)
//                    .into(ImageViewNav);
            Picasso.get()
                    .load(photo_url_user)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(main_image);


        } else if (type_user.equals("driver")) {
            UserNameTxt.setText(fullname_user);
            TypeUserTxt.setText(type_user);
            EmailUserTxt.setText(email_user);


//            Picasso.get()
//                    .load(photo_url_user)
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .error(R.drawable.ic_launcher_background)
//                    .into(ImageViewNav);

            Picasso.get()
                    .load(photo_url_user)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(main_image);


        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePageMap.class));
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
