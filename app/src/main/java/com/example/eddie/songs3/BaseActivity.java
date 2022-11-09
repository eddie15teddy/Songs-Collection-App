package com.example.eddie.songs3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

abstract public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{

    protected FrameLayout frameLayout;
    private DrawerLayout drawer;
    private NavigationView navView;
    private Context activityContext;

    abstract protected int getContentLayoutResId();
    abstract protected Context getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.eddie.songs3.R.layout.activity_base);
        onCreateDrawer();

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        View stubView = inflater.inflate(getContentLayoutResId(), frameLayout, false);
        frameLayout.addView(stubView, params);


    }

    protected void onCreateDrawer()
    {
        //set custom toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(com.example.eddie.songs3.R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(com.example.eddie.songs3.R.id.layout_drawer);

        //add a toggle for toolbar//

        //create the toggle
        androidx.appcompat.app.ActionBarDrawerToggle toggle =
                new androidx.appcompat.app.ActionBarDrawerToggle(
                        this, drawer, toolbar, com.example.eddie.songs3.R.string.navigation_drawer_open,
                        com.example.eddie.songs3.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);               //add the toggle
        toggle.syncState();

        frameLayout = findViewById(com.example.eddie.songs3.R.id.content_frame);
        navView = findViewById(com.example.eddie.songs3.R.id.nav_view);
        activityContext = getContext();

        navView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //getItemId

        int id = item.getItemId();

        if(id == com.example.eddie.songs3.R.id.nav_main_coll)
        {
            if(!(activityContext instanceof SearchActivityReg)) {
                Intent intent = new Intent(BaseActivity.this, SearchActivityReg.class);
                startActivity(intent);
            }
        }
        else if(id == com.example.eddie.songs3.R.id.nav_fav)
        {
            if(!(activityContext instanceof  FavouriteActivity))
            {
                Intent intent = new Intent(BaseActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        }
        else if(id == com.example.eddie.songs3.R.id.nav_site)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppTools.SITE_LINK));
            startActivity(intent);
        }
        else if(id == R.id.nav_youtube)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppTools.YOUTUBE_LINK));
            startActivity(intent);
        }
        else if(id == R.id.nav_bug)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppTools.BUG_LINK));
            startActivity(intent);
        }
        drawer.closeDrawers();
        return true;
    }

}
