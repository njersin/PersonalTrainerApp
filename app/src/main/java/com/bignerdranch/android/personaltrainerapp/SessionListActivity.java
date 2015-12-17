package com.bignerdranch.android.personaltrainerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class SessionListActivity extends AppCompatActivity{

    private static final String EXTRA_LIST_CLIENT_ID = "com.bignerdranch.android.personaltrainerapp.list_client_id";

    public static Intent newIntent(Context packageContext, UUID clientID) {
        Intent intent = new Intent(packageContext, SessionListActivity.class);
        intent.putExtra(EXTRA_LIST_CLIENT_ID, clientID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        UUID clientID = (UUID) getIntent().getSerializableExtra(EXTRA_LIST_CLIENT_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new SessionListFragment().newInstance(clientID);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
