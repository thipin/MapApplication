package com.wuttipong.project.mapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Amphoe;
import com.wuttipong.project.mapapplication.model.Listname;
import com.wuttipong.project.mapapplication.model.Specific;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.amphoe)
    AppCompatSpinner amphoe;
    @BindView(R.id.radio)
    RadioGroup radio;
    @BindView(R.id.select)
    AppCompatSpinner select;
    private Button btnSave;

    private ArrayAdapter<String> arrayAdapter;
    String[] SPINNERLIST;
    String[] SPECIFIC;
    private List<Specific> specificList;
    private int typeID;
    private List<Amphoe> amphoeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        showProgress();

        loadAmphoe();

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                typeID = radioGroup.indexOfChild(findViewById(i)) + 1;
            }
        });
    }

    private void loadAmphoe() {
        Ion.with(getApplicationContext())
                .load(ApiUrl.amphoe())
                .as(new TypeToken<List<Amphoe>>() {
                })
                .setCallback(new FutureCallback<List<Amphoe>>() {
                    @Override
                    public void onCompleted(Exception e, List<Amphoe> result) {
                        if (e != null) {
                            e.printStackTrace();
                            hideProgress();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                        } else {
                            amphoeList = result;
                            SPINNERLIST = new String[amphoeList.size()+1];
                            SPINNERLIST[0] = "-- เลือกทุกอำเภอ --";
                            for (int i = 0; i < amphoeList.size(); i++) {
                                SPINNERLIST[i+1] = amphoeList.get(i).getAmphoeName();
                            }
                            loadSpecific();
                        }
                    }
                });
    }

    private void loadSpecific() {
        Ion.with(getApplicationContext())
                .load(ApiUrl.specific())
                .as(new TypeToken<List<Specific>>() {
                })
                .setCallback(new FutureCallback<List<Specific>>() {
                    @Override
                    public void onCompleted(Exception e, List<Specific> result) {
                        if (e != null) {
                            e.printStackTrace();
                            hideProgress();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                        } else {
                            specificList = result;
                            SPECIFIC = new String[specificList.size()+1];
                            SPECIFIC[0] = "-- เลือกทุกเฉพาะทาง --";
                            for (int i = 0; i < specificList.size(); i++) {
                                SPECIFIC[i+1] = specificList.get(i).getSpecificName();
                            }

                            setSpinner();

                        }
                    }
                });
    }

    private void setSpinner() {
        hideProgress();
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        amphoe.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPECIFIC);
        select.setAdapter(arrayAdapter);
    }



    @OnClick(R.id.btn_search)
    public void onClick() {
        search();
    }

    private void search() {


        Intent intent = new Intent(SearchActivity.this, ListActivity.class);
        intent.putExtra("amphoe_id", amphoeList.get(amphoe.getSelectedItemPosition()).getAmphoeId());
        intent.putExtra("type_id", typeID);
        intent.putExtra("specific_id", specificList.get(select.getSelectedItemPosition()).getSpecificId());
        intent.putExtra("name", etName.getText().toString());
        startActivity(intent);



    }
}