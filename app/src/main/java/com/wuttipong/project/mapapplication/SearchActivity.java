package com.wuttipong.project.mapapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class SearchActivity extends AppCompatActivity {

    private Button btnSave;

    private ArrayAdapter<String> arrayAdapter;
    private MaterialBetterSpinner amphoe;
    private MaterialBetterSpinner select;
    String[] SPINNERLIST = {"t1", "t2", "t3", "t4"};
    String[] SELECT = {"t11", "t22", "t33", "t44"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

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
