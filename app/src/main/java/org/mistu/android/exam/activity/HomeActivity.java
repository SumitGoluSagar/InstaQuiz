package org.mistu.android.exam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mistu.android.exam.R;
import org.mistu.android.exam.adapter.FeedAdapter;
import org.mistu.android.exam.fragment.AdFragment;
import org.mistu.android.exam.fragment.ProblemFragment;
import org.mistu.android.exam.model.Feed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdFragment.OnFragmentInteractionListener{

    private static final int RC_SIGN_IN = 831;
    private static final int API_LEVEL = Build.VERSION.SDK_INT;

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference homeFeedDbRef;
    private DatabaseReference homeAdDbRef;
    private ChildEventListener homeFeedEventListener;
    private ValueEventListener homeAdListener;
    private List<Feed> feedList;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userName;

    private List<String> adUrlList;
    private Toolbar toolbar;
    private Context context;


    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private LinearLayout dotsContainer;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initVariables();
        setAuthStateListener();
    }

    private void setAuthStateListener(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d("SIGNED_IN", user.getDisplayName());
                    userName = user.getDisplayName();
                    onSignedInInit();
                }else {
                    Log.d("SIGNED_OUT", "USER IS NOT SIGNED IN");
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.LoginTheme)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                            )
                                    ).build(), RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                Toast.makeText(context, "Signed In", Toast.LENGTH_SHORT).show();
                onSignedInInit();
            }
            else {
                if (response == null) {
                    Toast.makeText(context, "Sign In Cancelled", Toast.LENGTH_LONG).show();
                }
                else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(context, "   Please Check Your\nINTERNET CONNECTION !", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Sign In Cancelled", Toast.LENGTH_LONG).show();
                }
                this.finish();
            }
        }
    }

    private void onSignedInInit() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }else {
            findViewById(R.id.activity_home_title).setVisibility(View.GONE);
        }

        setNavigationDrawer();
        setFab();
        attachHomeAdListener();
        setHomeFeedRecyclerView();
    }

    private void onSignedOutCleanUp() {
        detachHomeAdListener();
        detachHomeFeedEventListener();
        feedAdapter = null;
    }

    private void setUpAdViewPager() {
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        addBottomDots(0);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    private void addBottomDots(int position) {
        dots = new TextView[adUrlList.size()];


        dotsContainer.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setTextSize(25);
            dots[i].setGravity(Gravity.BOTTOM);

            if (API_LEVEL >= 24) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }

            if (API_LEVEL >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.cardview_light_background, this.getTheme()));
            }
            else {
                dots[i].setTextColor(getResources().getColor(R.color.cardview_light_background));
            }


            dotsContainer.addView(dots[i]);
        }

        if (dots.length > 0) {
            if (API_LEVEL >= Build.VERSION_CODES.M) {
                dots[position].setTextColor(getResources().getColor(R.color.card_bg2, this.getTheme()));
            }
            else {
                dots[position].setTextColor(getResources().getColor(R.color.card_bg2));
            }
        }
    }
    private void removeAdViewPager() {
        viewPager.removeAllViews();
        viewPager.setAdapter(null);
        pagerAdapter = null;
    }

    @Override
    public void onAdFragmentInteraction(int position) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AdFragment.newInstance(adUrlList.get(position), position);
        }

        @Override
        public int getCount() {
            return adUrlList.size();
        }

    }

    private void setNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PracticeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setHomeFeedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        feedAdapter= new FeedAdapter(feedList);
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void attachHomeFeedEventListener() {
        if (homeFeedEventListener == null) {
            Log.d("attachFeedListener", "inside");
            homeFeedEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Feed feed = dataSnapshot.getValue(Feed.class);
                    Log.d("onChildAdded", "addingChild");
                    feedAdapter.addItem(feed);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            homeFeedDbRef.addChildEventListener(homeFeedEventListener);
        }
    }

    private void detachHomeFeedEventListener() {
        if (homeFeedEventListener != null) {
            Log.d("detachFeedListener", "inside");
            homeFeedDbRef.removeEventListener(homeFeedEventListener);
            homeFeedEventListener = null;
            if (feedAdapter != null) {
                feedAdapter.clearAdapter();
            }
        }
    }

    private void attachHomeAdListener() {
        if (homeAdListener == null) {
            Log.i("attachHomeAdListener", "inside");
            homeAdListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        adUrlList.add(snapshot.getValue(String.class));
                    }
                    setUpAdViewPager();
                    // showSingleAd();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            homeAdDbRef.addListenerForSingleValueEvent(homeAdListener);
        }
    }

    private void detachHomeAdListener() {
        Log.i("detachHomeAdListener", "inside");
        if (homeAdListener != null) {
            homeAdDbRef.removeEventListener(homeAdListener);
            homeAdListener = null;
            adUrlList.clear();
        }
        removeAdViewPager();
    }

    /*private void showSingleAd() {
        ImageView imageView = (ImageView) findViewById(R.id.activity_home_app_bar_image);
        Glide.with(this)
                .load(adUrlList.get(adUrlList.size()-1))
                .fitCenter()
                .placeholder(R.drawable.bg_hori_light)
                .into(imageView);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "inside");
        firebaseAuth.addAuthStateListener(authStateListener);
        attachHomeFeedEventListener();
    }

    @Override
    protected void onStop() {
        Log.d("onStop", "inside");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d("onPause", "inside");
        firebaseAuth.removeAuthStateListener(authStateListener);
        detachHomeFeedEventListener();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy", "inside");
        detachHomeAdListener();
        super.onDestroy();
    }

    private void initVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.home_feed_rv);
        feedList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        context = this;
        homeFeedDbRef = firebaseDatabase.getReference().child("home_feeds");
        viewPager = (ViewPager) findViewById(R.id.home_ad_view_pager);
        adUrlList = new ArrayList<>();
        homeAdDbRef = firebaseDatabase.getReference().child("home_ads");
        dotsContainer = (LinearLayout) findViewById(R.id.home_ad_dots_container);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_quiz) {
            // Handle the camera action
        } else if (id == R.id.nav_batches) {
            startActivity(new Intent(this, BatchActivity.class));
        } else if (id == R.id.nav_doubt) {

        } else if (id == R.id.nav_contact_us) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about_teacher) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
