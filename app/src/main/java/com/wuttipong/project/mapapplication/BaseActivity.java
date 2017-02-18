package com.wuttipong.project.mapapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by layer on 18/2/2560.
 */

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog pd;

    protected void showProgress(){
        if (pd == null) {
            pd = new ProgressDialog(this);
        }
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

    }

    protected void hideProgress(){
        if (pd!=null){
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}
