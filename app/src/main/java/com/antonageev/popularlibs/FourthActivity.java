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
import com.orm.SugarContext;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class FourthActivity extends AppCompatActivity implements FourthView {

    private ProgressBar progressBar;
    private Button requestBtn, sugarUploadBtn, sugarGetBtn, sugarClearBtn, roomUploadBtn, roomGetBtn, roomClearBtn;

    private List<GitHubUsers> users;

    private RecyclerView recyclerView;
    private UsersListAdapter adapter;

    private Disposable dLoadUsers, dLoadSingleUser, dUploadToSugar, dGetFromSugar, dClearSugar, dUploadToRoom, dGetFromRoom, dClearRoom;

    private TextView infoTextView;


    private FourthPresenter fourthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        initViews();

        if (savedInstanceState == null) {
            fourthPresenter = new FourthPresenter();
        } else {
            fourthPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        setListeners();

        initRecyclerView(users);

        SugarContext.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    private void initRecyclerView(List<GitHubUsers> users) {
        adapter = new UsersListAdapter(users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener((v, pos) -> requestSingleUser(pos));
        recyclerView.setAdapter(adapter);
        recyclerView.hasFixedSize();
    }

    public void requestSingleUser(int position){
        try {
            String user = adapter.getSource().get(position).getLogin();
            NetworkInfo networkInfo = getNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                progressBar.setVisibility(View.VISIBLE);
                dLoadSingleUser = fourthPresenter.presenterLoadSingleUser(user);
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
        infoTextView = findViewById(R.id.infoTextView);

        sugarUploadBtn = findViewById(R.id.sugarUploadBtn);
        sugarGetBtn = findViewById(R.id.sugarGetBtn);
        sugarClearBtn = findViewById(R.id.sugarClearBtn);

        roomUploadBtn = findViewById(R.id.roomUploadBtn);
        roomGetBtn = findViewById(R.id.roomGetBtn);
        roomClearBtn = findViewById(R.id.roomClearBtn);
    }

    private void setListeners() {
        requestBtn.setOnClickListener(v -> loadUsers());
        sugarUploadBtn.setOnClickListener(v -> uploadToSugar());
        sugarGetBtn.setOnClickListener(v-> getDataFromSugar());
        sugarClearBtn.setOnClickListener(v -> clearSugar());

        roomUploadBtn.setOnClickListener(v -> uploadToRoom());
        roomGetBtn.setOnClickListener(v -> getDataFromRoom());
        roomClearBtn.setOnClickListener(v -> clearRoom());
    }

    private void uploadToRoom() {
        dUploadToRoom = fourthPresenter.presenterUploadToRoom();
    }

    private void getDataFromRoom() {
        dGetFromRoom = fourthPresenter.presenterGetDataFromRoom();
    }

    private void clearRoom() {
        dClearRoom = fourthPresenter.presenterClearRoom();
    }

    private void uploadToSugar() {
        dUploadToSugar = fourthPresenter.presenterUploadToSugar();
    }

    private void getDataFromSugar() {
        dGetFromSugar = fourthPresenter.presenterGetDataFromSugar();
    }

    private void clearSugar() {
        dClearSugar = fourthPresenter.presenterClearSugar();
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
    public void onUpdateResultTextView(String message) {
        infoTextView.setText(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        fourthPresenter.bindView(this);
        fourthPresenter.bindView(this, s -> infoTextView.setText(s));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dLoadUsers != null && !dLoadUsers.isDisposed()) dLoadUsers.dispose();
        if (dLoadSingleUser !=null && !dLoadSingleUser.isDisposed()) dLoadSingleUser.dispose();
        if (dUploadToSugar != null && !dUploadToSugar.isDisposed()) dUploadToSugar.dispose();
        if (dGetFromSugar!=null && !dGetFromSugar.isDisposed()) dGetFromSugar.dispose();
        if (dClearSugar != null && !dClearSugar.isDisposed()) dClearSugar.dispose();

        if (dUploadToRoom != null && !dUploadToRoom.isDisposed()) dUploadToRoom.dispose();
        if (dGetFromRoom != null && !dGetFromRoom.isDisposed()) dGetFromRoom.dispose();
        if (dClearRoom != null && !dClearRoom.isDisposed()) dClearRoom.dispose();
        fourthPresenter.unbindView();
    }


}
