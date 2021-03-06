package com.destinyapp.onlineshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.destinyapp.onlineshop.API.ApiRequest;
import com.destinyapp.onlineshop.API.RetroServer;
import com.destinyapp.onlineshop.Adapter.AdapterHistory;
import com.destinyapp.onlineshop.Adapter.AdapterProduct;
import com.destinyapp.onlineshop.Adapter.MyAdapterProduct;
import com.destinyapp.onlineshop.Model.DataModel;
import com.destinyapp.onlineshop.Model.Method;
import com.destinyapp.onlineshop.Model.ResponseModel;
import com.destinyapp.onlineshop.R;
import com.destinyapp.onlineshop.SharedPreferance.DB_Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView rv;
    Method method;
    private List<DataModel> mItems = new ArrayList<>();
    DB_Helper dbHelper;
    String username,nama,email,profile,alamat,level;
    LinearLayout available;
    MenuItem Wallet,Isi,Logout,Prof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        method=new Method();
        dbHelper = new DB_Helper(HistoryActivity.this);
        Cursor cursor = dbHelper.checkSession();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                username = cursor.getString(0);
                nama = cursor.getString(1);
                email = cursor.getString(2);
                profile = cursor.getString(3);
                alamat = cursor.getString(4);
                level = cursor.getString(5);
            }
        }
        if (username==null){
            Intent intent = new Intent(HistoryActivity.this,MainActivity.class);
            startActivity(intent);
        }
        rv=findViewById(R.id.recycler);
        available=findViewById(R.id.linearAvailable);
        Intent data = getIntent();
        String ACTIVITY = data.getStringExtra("ACTIVITY");
        Logic(ACTIVITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Wallet = menu.findItem(R.id.action_wallet);
        Isi = menu.findItem(R.id.action_isi);
        Prof = menu.findItem(R.id.action_profile);
        Logout = menu.findItem(R.id.action_settings);
        if (level.equals("1")){
            Wallet.setVisible(true);
            Isi.setVisible(true);
            Prof.setVisible(true);
            Logout.setVisible(true);
        }else{
            Wallet.setVisible(true);
            Isi.setVisible(false);
            Prof.setVisible(true);
            Logout.setVisible(true);
        }
        Checker();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            method.Logout(HistoryActivity.this);
        }else if(id == R.id.action_wallet){
            method.Wallet(this);
        }else if(id == R.id.action_isi){
            method.Isi(this);
        }
        return super.onOptionsItemSelected(item);
    }
    private void Checker(){
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        Call<ResponseModel> log = api.Checker(username);
        log.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatus().equals("success")){
                    CharSequence charSequence = new StringBuilder(method.MagicRP(Double.parseDouble(response.body().getData().get(0).wallet)));
                    Wallet.setTitle(charSequence);
                }else{
                    Toast.makeText(HistoryActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Koneksi internet Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Logic(String activity){
        ApiRequest api = RetroServer.getClient().create(ApiRequest.class);
        if (activity.equals("All")){
            Call<ResponseModel> Log = api.AllHistory(username);
            Log.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("success")){
                        available.setVisibility(View.GONE);
                        rv.setLayoutManager(new GridLayoutManager(HistoryActivity.this, 1));
                        mItems=response.body().getData();
                        AdapterHistory gridAdapter = new AdapterHistory(HistoryActivity.this,mItems);
                        rv.setAdapter(gridAdapter);
                    }else{
                        available.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    available.setVisibility(View.VISIBLE);
                }
            });
        }else if(activity.equals("Beli")){
            Call<ResponseModel> Log = api.BeliHistory(username);
            Log.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("success")){
                        available.setVisibility(View.GONE);
                        rv.setLayoutManager(new GridLayoutManager(HistoryActivity.this, 1));
                        mItems=response.body().getData();
                        AdapterHistory gridAdapter = new AdapterHistory(HistoryActivity.this,mItems);
                        rv.setAdapter(gridAdapter);
                    }else{
                        available.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    available.setVisibility(View.VISIBLE);
                }
            });
        }else{
            Call<ResponseModel> Log = api.JualHistory(username);
            Log.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("success")){
                        available.setVisibility(View.GONE);
                        rv.setLayoutManager(new GridLayoutManager(HistoryActivity.this, 1));
                        mItems=response.body().getData();
                        AdapterHistory gridAdapter = new AdapterHistory(HistoryActivity.this,mItems);
                        rv.setAdapter(gridAdapter);
                    }else{
                        available.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    available.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
