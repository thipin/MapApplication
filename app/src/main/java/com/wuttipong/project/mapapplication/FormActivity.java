package com.wuttipong.project.mapapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Amphoe;
import com.wuttipong.project.mapapplication.model.Specific;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

@RuntimePermissions
public class FormActivity extends BaseActivity {


    int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.etWeb)
    EditText etWeb;
    @BindView(R.id.amphoe)
    AppCompatSpinner amphoe;
    @BindView(R.id.radio)
    RadioGroup radio;
    @BindView(R.id.select)
    AppCompatSpinner select;
    @BindView(R.id.tvLocaltion)
    TextView tvLocaltion;
    @BindView(R.id.gallary)
    ImageButton gallary;
    private ArrayAdapter<String> arrayAdapter;
    String[] SPINNERLIST = {"t1", "t2", "t3", "t4"};
    String[] SPECIFIC = {"t11", "t22", "t33", "t44"};
    List<Amphoe> amphoeList;
    private List<Specific> specificList;
    private File file;
    private String location;
    private int typeID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);

        showProgress();

        loadAmphoe();

        /*EasyImage.configuration(this)
                .setAllowMultiplePickInGallery(false);*/

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
                            SPINNERLIST = new String[amphoeList.size()];
                            for (int i = 0; i < amphoeList.size(); i++) {
                                SPINNERLIST[i] = amphoeList.get(i).getAmphoeName();
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
                            SPECIFIC = new String[specificList.size()];
                            for (int i = 0; i < specificList.size(); i++) {
                                SPECIFIC[i] = specificList.get(i).getSpecificName();
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

    @OnClick({R.id.gallary, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallary:
                FormActivityPermissionsDispatcher.selectImageWithCheck(FormActivity.this);
                break;
            case R.id.btnSave:
                save();
                break;
        }
    }

    private void save() {

        if (file == null) {
            Toast.makeText(getApplicationContext(), "ต้องเลือกรูปภาพ", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getApplicationContext(), "ต้องเลือกพิกัด", Toast.LENGTH_LONG).show();
            return;
        }

        if (amphoe.getSelectedItemPosition() < 0) {
            Toast.makeText(getApplicationContext(), "ต้องเลือกอำเภอ", Toast.LENGTH_LONG).show();
            return;
        }

        if (select.getSelectedItemPosition() < 0) {
            Toast.makeText(getApplicationContext(), "ต้องเลือกเฉพาะทาง", Toast.LENGTH_LONG).show();
            return;
        }


        Ion.with(getApplicationContext())
                .load(ApiUrl.add_hospital())
                .setMultipartParameter("status", "0")
                .setMultipartFile("img", file)
                .setMultipartParameter("name", etName.getText().toString())
                .setMultipartParameter("tel", etTel.getText().toString())
                .setMultipartParameter("web", etWeb.getText().toString())
                .setMultipartParameter("localtion", location)
                .setMultipartParameter("amphoe_id", String.valueOf(amphoeList.get(amphoe.getSelectedItemPosition()).getAmphoeId()))
                .setMultipartParameter("type_id", String.valueOf(typeID))
                .setMultipartParameter("specific_id", String.valueOf(specificList.get(select.getSelectedItemPosition()).getSpecificId()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("form", "onCompleted: " + result.toString());
                            if (result.get("success").getAsBoolean()) {
                                Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกข้อมูลได้", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void selectImage() {
        EasyImage.openChooserWithGallery(FormActivity.this, "เลือกภาพ", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                if (imagesFiles != null && imagesFiles.size() > 0) {
                    file = imagesFiles.get(0);
                    Glide.with(getApplicationContext()).load(file).into(gallary);
                }
            }
        });

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                location = String.valueOf(place.getLatLng().latitude) +","+ String.valueOf(place.getLatLng().longitude);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.tvLocaltion)
    public void onClick() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(FormActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
