package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Listname;
import com.wuttipong.project.mapapplication.model.Success;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Listname> dataList;
    private dataAdapter adapter;
    private ProgressDialog pd;
    private TextView txtNotfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        listView = (ListView) findViewById(R.id.listview);
        txtNotfound = (TextView) findViewById(R.id.txt_not_found);

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
                .load(ApiUrl.list_hospital())
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
                                        Intent intent = new Intent(ManageActivity.this, DetailActivity.class);
                                        intent.putExtra("hospitalID", result.get(position).getHospitalId());
                                        startActivity(intent);
                                    }
                                });

                                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ManageActivity.this);

                                        builder.setTitle("ยืนยันการลบ");
                                        builder.setMessage("คุณต้องลบสถานพยาบาลนี้หรือไม่");
                                        builder.setCancelable(false);

                                        builder.setPositiveButton("ยันยัน", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                deleteHospital(result.get(position).getHospitalId());
                                            }
                                        });

                                        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        android.support.v7.app.AlertDialog dialog = builder.create();
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.show();
                                        return true;
                                    }
                                });
                            }
                        }

                        hideProgress();

                    }
                });
    }

    private void deleteHospital(int id) {
        Log.d("CHECK",ApiUrl.del_hospital(id));
        showProgress();
        Ion.with(getApplicationContext())
                .load(ApiUrl.del_hospital(id))
                .as(new TypeToken<Success>() {
                })
                .setCallback(new FutureCallback<Success>() {
                    @Override
                    public void onCompleted(Exception e, Success result) {
                        hideProgress();
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            if (result.getSuccess()) {
                                Toast.makeText(ManageActivity.this, "ลบข้อมูลกสำเร็จ", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                                return;
                            }

                        }
                        Toast.makeText(ManageActivity.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();
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
