package com.wuttipong.project.mapapplication;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Listname;

import java.util.ArrayList;

import cn.nekocode.badge.BadgeDrawable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private ImageButton btnAdmin;
    private ImageButton btnAuto;
    private ImageButton btnSearch;
    private ImageButton btnAdd;
    private ImageView imgBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdmin = (ImageButton) findViewById(R.id.btn_admin);
        btnAuto = (ImageButton) findViewById(R.id.btn_auto);
        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        imgBadge = (ImageView) findViewById(R.id.img_badge);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.startMapWithCheck(MainActivity.this);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void startMap() {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    private void loadData() {
        Ion.with(getApplicationContext())
                .load(ApiUrl.appove_hospital())
                .as(new TypeToken<ArrayList<Listname>>() {
                })
                .setCallback(new FutureCallback<ArrayList<Listname>>() {
                    @Override
                    public void onCompleted(Exception e, final ArrayList<Listname> result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                        } else {
                            if (result.size()>0) {
                                BadgeDrawable drawable =
                                        new BadgeDrawable.Builder()
                                                .type(BadgeDrawable.TYPE_NUMBER)
                                                .number(result.size())
                                                .badgeColor(0xffff0000)
                                                .textColor(0xffFFFFFF)
                                                .build();

                                imgBadge.setImageDrawable(drawable);
                            }else {
                                imgBadge.setImageResource(0);
                            }

                        }


                    }
                });
    }
}