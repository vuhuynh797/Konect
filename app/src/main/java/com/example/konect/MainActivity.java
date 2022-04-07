package com.example.konect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.konect.fragment.KonectViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initAdapter();
        initListener();
    }

    private void initUI() {


        mNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);

    }

    private void initAdapter() {
        KonectViewPagerAdapter adapter = new KonectViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mNavigationView.getMenu().findItem(R.id.menu_chat).setChecked(true);
                        break;
                    case 1:
                        mNavigationView.getMenu().findItem(R.id.menu_feed).setChecked(true);
                        break;
                    case 2:
                        mNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;

                }
            }
        });
    }

    private void initListener() {


        mNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_chat:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.menu_feed:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.menu_profile:
                    viewPager.setCurrentItem(2);
                    break;
            }
                return true;
        });

    }
}