package com.bignerdranch.android.personaltrainerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SessionListFragment extends Fragment {

    private static final String ARG_SESSION_LIST_CLIENT_ID = "session_client_id";

    private RecyclerView mItemRecyclerView;
    private SessionAdapter mAdapter;
    private Client mClient;

    public static SessionListFragment newInstance(UUID clientID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESSION_LIST_CLIENT_ID, clientID);

        SessionListFragment sessionListFragment = new SessionListFragment();
        sessionListFragment.setArguments(args);
        return sessionListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID clientID = (UUID) getArguments().getSerializable(ARG_SESSION_LIST_CLIENT_ID);
        mClient = ClientLab.get(getActivity()).getClient(clientID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_list, container, false);

        //get a reference and inflate the RecyclerView
        mItemRecyclerView = (RecyclerView)view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(); //populate the list of sessions for the client
        return view;
    }

    private class SessionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Session mSession;
        private ImageView mThumbnailView;
        private TextView mSessionTitleTextView;
        private TextView mDateTextView;

        public SessionHolder(View sessionView) {
            super(sessionView);
            sessionView.setOnClickListener(this);

            mThumbnailView = (ImageView)sessionView.findViewById(R.id.list_session_thumbnail_view);
            mSessionTitleTextView = (TextView)sessionView.findViewById(R.id.list_session_title);
            mDateTextView = (TextView)sessionView.findViewById(R.id.list_session_date_textview);
        }

        @Override
        public void onClick(View v) {
            Intent intent = SessionPagerActivity.newIntent(getActivity(), mSession.getSessionID());
            startActivity(intent);
        }

        public void bindClient(Session session) {
            mSession = session;

            File pictureFile = SessionLab.get(getActivity()).getPictureFile(session);
            if (pictureFile == null || !pictureFile.exists()) {
                mThumbnailView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(pictureFile.getPath(), 60, 60);
                mThumbnailView.setImageBitmap(bitmap);
            }

            mSessionTitleTextView.setText(mSession.getTitle());
            mDateTextView.setText(mSession.getDate().toString());
        }
    }

    private class SessionAdapter extends RecyclerView.Adapter<SessionHolder> {
        private List<Session> mSessions;

        public SessionAdapter(List<Session> sessions) {
            mSessions = sessions;
        }

        @Override
        public SessionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_session, parent, false);
            return new SessionHolder(view);
        }

        @Override
        public void onBindViewHolder(SessionHolder holder, int position) {
            Session session = mSessions.get(position);
            holder.bindClient(session);
        }

        @Override
        public int getItemCount() {
            return mSessions.size();
        }

        public void setSessions(List<Session> sessions){
            mSessions = sessions;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_session_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_session:
                Session session = new Session();
                session.setClientID(mClient.getID());
                SessionLab.get(getActivity()).addSession(session);
                intent = SessionPagerActivity.newIntent(getActivity(), session.getSessionID());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SessionLab sessionLab = SessionLab.get(getActivity());
        List<Session> sessions = null;
        sessions = sessionLab.searchSessions(mClient.getID());

        if (mAdapter == null) {
            mAdapter = new SessionAdapter(sessions);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSessions(sessions);
            mAdapter.notifyDataSetChanged();
        }

        updateTitle();
    }

    public void updateTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mClient.getName());
    }
}
