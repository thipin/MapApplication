package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Listname;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout btnAdd;
    private LinearLayout btnManage;

    private ListView listView;
    private ArrayList<Listname> dataList;
    private dataAdapter adapter;
    private ProgressDialog pd;
    private TextView txtNotfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAdd = (LinearLayout) findViewById(R.id.btnAdd);
        btnManage = (LinearLayout) findViewById(R.id.btnManage);

        listView = (ListView) findViewById(R.id.listview);
        txtNotfound = (TextView) findViewById(R.id.txt_not_found);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ManageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataList = new ArrayList<Listname>();

        loadData();
    }

    private void loadData() {
        showProgress();
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
                            for (Listname list : result) {
                                dataList.add(list);
                            }
                            if (result.size() == 0) {
                                txtNotfound.setVisibility(View.VISIBLE);
                            } else {
                                txtNotfound.setVisibility(View.GONE);
                                adapter = new dataAdapter();
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(HomeActivity.this, AppoveActivity.class);
                                        intent.putExtra("hospitalID", result.get(position).getHospitalId());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        hideProgress();

                    }
                });
    }

    private class dataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                mHolder.textView = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.textView.setText(dataList.get(position).getHospitalName().toString());


            return convertView;
        }
    }

    public static class ViewHolder {
        TextView textView;
    }

    private void showProgress() {
        pd = new ProgressDialog(this);
        pd.setMessage("กำลังโหลด..");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    private void hideProgress() {
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }
}
