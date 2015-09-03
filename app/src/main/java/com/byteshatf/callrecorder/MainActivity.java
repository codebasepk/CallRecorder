package com.byteshatf.callrecorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.byteshatf.callrecorder.fragments.RecordingListFragment;
import com.byteshatf.callrecorder.fragments.RulesFragment;
import com.byteshatf.callrecorder.services.CallRecordingService;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private String[] mListTitles;
    private ViewPager mViewPager;
    private MaterialTabHost mMaterialTabHost;
    private Resources mResources;
    private Fragment mFragment;
    private AlertDialog levelDialog;
    private SharedPreferences sharedPreferences;
    private Helpers mHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MyAppTheme);
        setContentView(R.layout.activity_main);
        mHelpers = new Helpers(getApplicationContext());
        sharedPreferences = mHelpers.getPreferenceManager();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006666")));
        getSupportActionBar().setElevation(0);
        mMaterialTabHost = (MaterialTabHost) findViewById(R.id.tab_host);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mResources = getResources();
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mMaterialTabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            mMaterialTabHost.addTab(mMaterialTabHost.newTab().setIcon(getIcon(i)).setTabListener(this));
        }
        startService(new Intent(getApplicationContext(), CallRecordingService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            final CharSequence[] items = {"Mic", "Voice Call", "Voice Up-Link", "Voice Down-Link"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Audio Source");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch(item)
                    {
                        case 0:
                            Toast.makeText(getApplicationContext(), "Audio Source: MIC", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putInt("radio_int", 0);
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), "Audio Source: Voice Call", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putInt("radio_int", 1);
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), "Audio Source: Voice Down-Link", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putInt("radio_int", 2);
                            break;
                        case 3:
                            Toast.makeText(getApplicationContext(), "Audio Source: Voice UP-Link", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putInt("radio_int", 3);
                            break;
                    }
                    levelDialog.dismiss();
                }
            });
            levelDialog = builder.create();
            levelDialog.show();
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), AddRuleActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {}

    @Override
    public void onTabUnselected(MaterialTab materialTab) {}

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            mFragment = new Fragment();

            switch (num) {
                case 0:
                    mFragment = new RulesFragment();
                    break;
                case 1:
                    mFragment = new RecordingListFragment();
                    break;
            }
            return mFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private Drawable getIcon(int position) {
        switch (position) {
            case 0:
                return mResources.getDrawable(R.drawable.rule);
            case 1:
                return mResources.getDrawable(R.drawable.ic_list_filled);
            default:
                return null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
    }
}
