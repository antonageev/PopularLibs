package com.antonageev.popularlibs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antonageev.popularlibs.models.GitHubUsers;
import com.antonageev.popularlibs.presenters.FourthPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class FourthActivity extends AppCompatActivity implements FourthView {

    private ProgressBar progressBar;
    private Button requestBtn;

    private List<GitHubUsers> users;

    private RecyclerView recyclerView;
    private UsersListAdapter adapter;

    private Disposable dLoadUsers, dLoadSingleUser;


    private FourthPresenter fourthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        initViews();

        if (savedInstanceState == null) {
            fourthPresenter = new FourthPresenter();
        } else {
            PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        setListeners();

        initRecyclerView(users);
    }

    private void initRecyclerView(List<GitHubUsers> users) {
        adapter = new UsersListAdapter(users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener((v, pos) -> {
            progressBar.setVisibility(View.VISIBLE);
            requestSingleUser(pos);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.hasFixedSize();
    }

    public void requestSingleUser(int position){
        try {
            String user = adapter.getSource().get(position).getLogin();
            NetworkInfo networkInfo = getNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                progressBar.setVisibility(View.VISIBLE);
                fourthPresenter.presenterLoadSingleUser(user);
            } else {
                Toast.makeText(this, "Network is unavailable. Check Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(fourthPresenter, outState);
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        requestBtn = findViewById(R.id.requestBtn);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setListeners() {
        requestBtn.setOnClickListener(v -> loadUsers());
    }

    public void loadUsers() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            dLoadUsers = fourthPresenter.presenterLoadUsers();
        } else {
            Toast.makeText(this, "Network is unavailable. Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    @Override
    public void onUpdateViews(List<GitHubUsers> users) {
        progressBar.setVisibility(View.GONE);
        this.users = users;
        adapter.setSource(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onShowMessageDialog(String data) {
        progressBar.setVisibility(View.GONE);
        CustomDialogFragment dialog = new CustomDialogFragment(data);
        dialog.show(getSupportFragmentManager(), null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        fourthPresenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!dLoadUsers.isDisposed()) dLoadUsers.dispose();
        fourthPresenter.unbindView();
    }
}
