package com.wuttipong.project.mapapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class FormActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private MaterialBetterSpinner amphoe;
    private MaterialBetterSpinner select;
    String[] SPINNERLIST = {"t1", "t2", "t3", "t4"};
    String[] SELECT = {"t11", "t22", "t33", "t44"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        amphoe = (MaterialBetterSpinner)
                findViewById(R.id.amphoe);
        amphoe.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SELECT);
        select = (MaterialBetterSpinner)
                findViewById(R.id.select);
        select.setAdapter(arrayAdapter);
    }
}
