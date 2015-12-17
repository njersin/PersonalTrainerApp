package com.bignerdranch.android.personaltrainerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class SessionPagerActivity extends AppCompatActivity {

    private static final String EXTRA_SESSION_ID = "com.bignerdranch.android.personaltrainerapp.session_id";

    private ViewPager mViewPager;
    private List<Session> mSessions;

    public static Intent newIntent(Context packageContext, UUID sessionID) {
        Intent intent = new Intent(packageContext, SessionPagerActivity.class);
        intent.putExtra(EXTRA_SESSION_ID, sessionID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_pager);

        UUID sessionID = (UUID) getIntent().getSerializableExtra(EXTRA_SESSION_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_session_pager_view_pager);

        mSessions = SessionLab.get(this).getSessions();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Session session = mSessions.get(position);
                return SessionFragment.newInstance(session.getSessionID());
            }

            @Override
            public int getCount() {
                return mSessions.size();
            }
        });

        for (int i = 0; i < mSessions.size(); i++) {
            if (mSessions.get(i).getSessionID().equals(sessionID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
