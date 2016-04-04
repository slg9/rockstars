package com.sebastien.example.rockstars.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.fragment.BookmarksFragment;
import com.sebastien.example.rockstars.fragment.ProfileFragment;
import com.sebastien.example.rockstars.fragment.RockstarsFragment;
import com.sebastien.example.rockstars.utils.Utils;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context ctx = this;
    private AHBottomNavigation bottomNavigation;


    //Declaration of Fragments
    private BookmarksFragment bookmarksFragment;
    private ProfileFragment profileFragment;
    private RockstarsFragment rockstarsFragment;
    ArrayList<Fragment> mFragmentlist = new ArrayList<Fragment>();
    ArrayList<String> mFragmentTitlelist = new ArrayList<String>();


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // We intiliaze the position 0 for the fragment in sharedpreference
        if(Utils.getBooleanValue(ctx,"first_launch")) {
            Utils.putIntValue(ctx, "current_fragment_position", 0);
            Utils.putBooleanValue(ctx, "first_launch", false);
        }

        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initview() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);


        //Function to create our bottom navigation bar with 3 items, only for the First_launch
        create_bottom_navigation_bar();
        //Function to create our Fragments
        addFragment();

        //Function to display the current Fragment when the app is launched and we reload the activity
        Display_default_fragment(Utils.getIntValue(ctx, "current_fragment_position"));

        //Function to create the OnTablistener
        initlistener();

    }

    private void addFragment() {
        //We instanciate our 3 fragments
        bookmarksFragment = new BookmarksFragment();
        profileFragment = new ProfileFragment();
        rockstarsFragment = new RockstarsFragment();
        //We add our 3 fragment to and ArrayList

        mFragmentlist.add(0, rockstarsFragment);
        mFragmentlist.add(1, bookmarksFragment);
        mFragmentlist.add(2, profileFragment);

        mFragmentTitlelist.add(0, "Rockstars");
        mFragmentTitlelist.add(1, "Bookmarks");
        mFragmentTitlelist.add(2, "Profile");

    }

    private void Display_default_fragment(int cur_pos) {
        fragmentManager = getSupportFragmentManager();
        bottomNavigation.setCurrentItem(cur_pos);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, mFragmentlist.get(cur_pos), mFragmentTitlelist.get(cur_pos))

                .commit();

    }

    private void create_bottom_navigation_bar() {
        //We create our 3 the Bottom Bar item, thanks to the library 'com.aurelhubert:ahbottomnavigation:0.1.3' added in the dependencies of build.gradle
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Rockstars", R.drawable.ic_list_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Bookmarks", R.drawable.ic_bookmark_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.ic_profile_black_24dp);

        // we add the items to the Bottom Navigation Bar
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        //We display the default background color of the Bottom Navigation Bar
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#D2671F"));
        bottomNavigation.setInactiveColor(Color.parseColor("#F0A96A"));
        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));

    }

    private void initlistener() {
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //Here we compare the position of last fragment store in the sharedpreference, and the current position, to determinate the transaction direction
                if (Utils.getIntValue(ctx, "current_fragment_position") > position) {
                    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                } else {
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                // We put in sharedpreferences the current position

                Utils.putIntValue(ctx, "current_fragment_position", position);
                //Here we replace the fragment in the layout with the fragment that correspond to bottom_navigation_bar item position
                transaction.replace(R.id.frame, mFragmentlist.get(position), mFragmentTitlelist.get(position))
                        //.show(mFragmentlist.get(position))

                        .addToBackStack(null)
                        .commit();
                //getActionBar().setTitle(mFragmentTitlelist.get(position));

            }
        });

    }
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
