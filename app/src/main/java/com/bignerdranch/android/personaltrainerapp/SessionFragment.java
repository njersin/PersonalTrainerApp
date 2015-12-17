package com.bignerdranch.android.personaltrainerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.UUID;

public class SessionFragment extends Fragment {

    private static final String ARG_SESSION_ID = "session_id";
    private static final int REQUEST_PICTURE = 33;

    private Session mSession;
    private Client mClient;

    private EditText mSessionTitleField;
    private TextView mLocationTextView;
    private TextView mDateTimeStamp;
    private EditText mClientWeightField;
    private ImageView mPictureView;
    private ImageButton mPictureButton;
    private File mPictureFile;

    public static SessionFragment newInstance(UUID sessionID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESSION_ID, sessionID);

        SessionFragment sessionFragment = new SessionFragment();
        sessionFragment.setArguments(args);
        return sessionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID sessionID = (UUID) getArguments().getSerializable(ARG_SESSION_ID);
        mSession = SessionLab.get(getActivity()).getSession(sessionID);
        mClient = ClientLab.get(getActivity()).getClient(mSession.getClientID());
        mPictureFile = SessionLab.get(getActivity()).getPictureFile(mSession);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, container, false);

        //Session title
        mSessionTitleField = (EditText)view.findViewById(R.id.session_title_field);
        mSessionTitleField.setText(mSession.getTitle());
        mSessionTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSession.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Location
        mLocationTextView = (TextView)view.findViewById(R.id.location_text);
        mLocationTextView.setText("");

        //Date and Time
        mDateTimeStamp = (TextView)view.findViewById(R.id.date_time_stamp_text);
        mDateTimeStamp.setText(mSession.getDate().toString());

        //Weight
        mClientWeightField = (EditText)view.findViewById(R.id.weight_text);
        mClientWeightField.setText(mSession.getWeight() + "");
        mClientWeightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(""))
                    mSession.setWeight(Double.parseDouble(s.toString()));
            }
        });

        //Picture button
        mPictureButton = (ImageButton)view.findViewById(R.id.picture_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPictureFile != null && captureImage.resolveActivity(packageManager) != null;
        mPictureButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPictureFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PICTURE);
            }
        });

        //Picture image view
        mPictureView = (ImageView)view.findViewById(R.id.picture_image_view);
        updatePictureView();

        //set the title bar text to the client's name
        updateTitle();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_session, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_session:
                SessionLab.get(getActivity()).deleteSession(mSession);
                getActivity().finish();
                return true;
            case R.id.menu_item_save_session:
                SessionLab.get(getActivity()).updateSession(mSession);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SessionLab.get(getActivity()).updateSession(mSession);
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mClient.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PICTURE) {
            updatePictureView();
        }
    }

    private void updatePictureView() {
        if (mPictureFile == null || !mPictureFile.exists()) {
            mPictureView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaleBitmap(mPictureFile.getPath(), getActivity());
            mPictureView.setImageBitmap(bitmap);
        }
    }
}
