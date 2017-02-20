package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Login;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private ProgressDialog pd;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        if(TextUtils.isEmpty(username.getText().toString())){
            username.setError("กรุณากรอกชื่อผู้ใช้งาน");
            username.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("กรุณากรอกรหัสผ่าน");
            password.requestFocus();
            return;
        }

        showProgress();
        Ion.with(getApplicationContext())
                .load(ApiUrl.login())
                .setBodyParameter("username", username.getText().toString())
                .setBodyParameter("password", password.getText().toString())
                .as(new TypeToken<Login>() {
                })
                .setCallback(new FutureCallback<Login>() {
                    @Override
                    public void onCompleted(Exception e, Login result) {
                        hideProgress();
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            if (result.getSuccess()) {


                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                                return;
                            }

                        }
                        Toast.makeText(LoginActivity.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProgress() {
        pd = new ProgressDialog(this);
        pd.setMessage("กำลังโหลด...");
        pd.setCancelable(false);
        pd.show();
    }

    private void hideProgress(){
        if(pd!=null){
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}
