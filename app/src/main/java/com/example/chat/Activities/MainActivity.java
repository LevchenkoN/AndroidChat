package com.example.chat.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chat.Adapters.CustomViewPager;
import com.example.chat.Fragments.FriendsFragment;
import com.example.chat.Fragments.MainScreenFragment;
import com.example.chat.Fragments.RecentChatsFragment;
import com.example.chat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ViewPager viewPager;

    TabLayout tabLayout;
    CustomViewPager customViewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.viewpager_message);
        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("CHAT");

        customViewPager = new CustomViewPager(getSupportFragmentManager());
        customViewPager.addFragment(new RecentChatsFragment(),"Chats");
        customViewPager.addFragment(new MainScreenFragment(),"Users");
        customViewPager.addFragment(new FriendsFragment(),"Friends");

        viewPager.setAdapter(customViewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//         Handle item selection
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Toast.makeText(getApplicationContext(), "CLICKED SEARCH", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfileDesign.class));
            return true;
        } else if (itemId == R.id.log_out) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}