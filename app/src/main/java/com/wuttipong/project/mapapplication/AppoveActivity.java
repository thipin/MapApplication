package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.HospitalDetail;
import com.wuttipong.project.mapapplication.model.Success;

public class AppoveActivity extends AppCompatActivity {

    private ImageView img;
    private TextView txtName;
    private TextView txtAmphoe;
    private TextView txtType;
    private TextView txtSpecific;
    private TextView txtTel;
    private TextView txtWeb;

    private Button btnT;
    private Button btnF;

    String alert;

    int hospitalID;
    private HospitalDetail detail;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appove);

        img = (ImageView) findViewById(R.id.img);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAmphoe = (TextView) findViewById(R.id.txt_amphoe);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtSpecific = (TextView) findViewById(R.id.txt_specific);
        txtTel = (TextView) findViewById(R.id.txt_tel);
        txtWeb = (TextView) findViewById(R.id.txt_web);

        btnT = (Button) findViewById(R.id.btnT);
        btnF = (Button) findViewById(R.id.btnF);

        btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appove(1);
            }
        });

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appove(2);
            }
        });


        ImageButton map = (ImageButton) findViewById(R.id.btn_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+detail.getHospitalLocaltion());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        hospitalID = getIntent().getIntExtra("hospitalID",0);

        loadData();


    }

    private void appove(final int status) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        if (status == 1) {
            alert = "ยืนยันการอนุมัติ";
        } else {
            alert = "ยืนยันการไม่อนุมัติ";
        }

        builder.setTitle("แจ้งเตือน");
        builder.setMessage(alert);
        builder.setCancelable(false);

        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                
                showProgress();
                Ion.with(getApplicationContext())
                        .load(ApiUrl.update_hospital())
                        .setBodyParameter("id", hospitalID + "")
                        .setBodyParameter("action", status + "")
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
                                        Toast.makeText(AppoveActivity.this, "เปลี่ยนสถานะสำเร็จ", Toast.LENGTH_SHORT).show();
                                        finish();
                                        return;
                                    }

                                }
                                Toast.makeText(AppoveActivity.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();
                            }
                        });

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
    }

    private void loadData() {
        showProgress();
        Log.d("URL", ApiUrl.hospital_detail(hospitalID));
        Ion.with(getApplicationContext())
                .load(ApiUrl.hospital_detail(hospitalID))
                .as(new TypeToken<HospitalDetail>() {
                })
                .setCallback(new FutureCallback<HospitalDetail>() {
                    @Override
                    public void onCompleted(Exception e, HospitalDetail result) {
                        hideProgress();
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG).show();
                        } else {
                            detail = result;

                            txtName.setText(result.getHospitalName());
                            txtAmphoe.setText(result.getAmphoeName());
                            txtType.setText(result.getTypeName());
                            txtSpecific.setText(result.getSpecificName());
                            txtTel.setText(result.getHospitalTel()+"");
                            txtWeb.setText(result.getHospitalWeb());

                            if (!TextUtils.isEmpty(result.getHospitalImg())) {
                                Glide.with(getApplicationContext()).load(result.getHospitalImg()).into(img);
                            }

                        }

                    }
                });
    }

    private void showProgress() {
        pd = new ProgressDialog(this);
        pd.setMessage("loading..");
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
