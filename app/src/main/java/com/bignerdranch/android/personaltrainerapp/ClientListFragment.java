package com.bignerdranch.android.personaltrainerapp;


import android.content.Intent;
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
import android.widget.TextView;

import java.util.List;

public class ClientListFragment extends Fragment{

    private RecyclerView mItemRecyclerView;
    private ClientAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);

        //get a reference and inflate the RecyclerView
        mItemRecyclerView = (RecyclerView)view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(); //populate the current list of clients
        return view;
    }

    private class ClientHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Client mClient;
        private TextView mClientNameTextView;

        public ClientHolder(View clientView) {
            super(clientView);
            clientView.setOnClickListener(this);
            mClientNameTextView = (TextView)clientView.findViewById(R.id.list_client_name);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ClientPagerActivity.newIntent(getActivity(), mClient.getID());
            startActivity(intent);
        }

        public void bindClient(Client client) {
            mClient = client;
            mClientNameTextView.setText(mClient.getName());
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {
        private List<Client> mClients;

        public ClientAdapter(List<Client> clients) {
            mClients = clients;
        }

        @Override
        public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_client, parent, false);
            return new ClientHolder(view);
        }

        @Override
        public void onBindViewHolder(ClientHolder holder, int position) {
            Client client = mClients.get(position);
            holder.bindClient(client);
        }

        @Override
        public int getItemCount() {
            return mClients.size();
        }

        public void setClients(List<Client> clients){
            mClients = clients;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_client_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_client:
                Client client = new Client();
                ClientLab.get(getActivity()).addClient(client);
                intent = ClientPagerActivity.newIntent(getActivity(), client.getID());
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
        ClientLab clientLab = ClientLab.get(getActivity());
        List<Client> clients = null;
        clients = clientLab.getClients();

        if (mAdapter == null) {
            mAdapter = new ClientAdapter(clients);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClients(clients);
            mAdapter.notifyDataSetChanged();
        }

        int clientsCount = mAdapter.getItemCount();
        updateTitle(clientsCount);
    }

    public void updateTitle(int clientsCount) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(getString(R.string.title_format, clientsCount));
    }
}
