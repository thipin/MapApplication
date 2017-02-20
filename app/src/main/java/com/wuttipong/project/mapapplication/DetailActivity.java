package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class DetailActivity extends AppCompatActivity {

    private ImageView img;
    private TextView txtName;
    private TextView txtAmphoe;
    private TextView txtType;
    private TextView txtSpecific;
    private TextView txtTel;
    private TextView txtWeb;

    int hospitalID;
    private HospitalDetail detail;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = (ImageView) findViewById(R.id.img);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAmphoe = (TextView) findViewById(R.id.txt_amphoe);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtSpecific = (TextView) findViewById(R.id.txt_specific);
        txtTel = (TextView) findViewById(R.id.txt_tel);
        txtWeb = (TextView) findViewById(R.id.txt_web);
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

        txtWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(detail.getHospitalWeb())) {
                    String url = detail.getHospitalWeb();
                    if (!url.startsWith("http")){
                        url = "http://"+url;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        txtTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(detail.getHospitalTel())) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:"+detail.getHospitalTel()));
                    startActivity(i);
                }
            }
        });



    }

    private void loadData() {
        showProgress();
        Log.d("URL",ApiUrl.hospital_detail(hospitalID));
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
                            txtTel.setText(result.getHospitalTel()+" [ โทร ]");
                            txtWeb.setText(result.getHospitalWeb() +" [ เข้าเว็บ ]");

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
