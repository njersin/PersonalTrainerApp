package com.bignerdranch.android.personaltrainerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;

public class ClientFragment extends Fragment {

    private static final String ARG_CLIENT_ID = "client_id";

    private Client mClient;
    private EditText mClientNameField;
    private EditText mEmailAddressField;
    private TextView mCreatedDateTimeStamp;
    private Button mShowSessionsButton;

    public static ClientFragment newInstance(UUID clientID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, clientID);

        ClientFragment clientFragment = new ClientFragment();
        clientFragment.setArguments(args);
        return clientFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID clientID = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        mClient = ClientLab.get(getActivity()).getClient(clientID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        //get a reference to the name field and set a listener for text changes
        mClientNameField = (EditText)view.findViewById(R.id.name_field);
        mClientNameField.setText(mClient.getName());
        mClientNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClient.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get a reference to the email address field and set a listener for text changes
        mEmailAddressField = (EditText)view.findViewById(R.id.email_address_field);
        mEmailAddressField.setText(mClient.getEmailAddress());
        mEmailAddressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClient.setEmailAddress(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get a reference to the Added field and set the text to the datetime stamp
        mCreatedDateTimeStamp = (TextView)view.findViewById(R.id.created_field);
        mCreatedDateTimeStamp.setText(mClient.getDate().toString());

        //get a reference to the Show Sessions button and set an OnClickListener event
        mShowSessionsButton = (Button)view.findViewById(R.id.show_sessions_button);
        mShowSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SessionListActivity.newIntent(getActivity(), mClient.getID());
                startActivity(intent);
            }
        });

        updateTitle();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_client, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_client:
                ClientLab.get(getActivity()).deleteClient(mClient);
                getActivity().finish();
                return true;
            case R.id.menu_item_save_client:
                ClientLab.get(getActivity()).updateClient(mClient);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("");
    }

    @Override
    public void onPause() {
        super.onPause();
        ClientLab.get(getActivity()).updateClient(mClient);
    }
}
