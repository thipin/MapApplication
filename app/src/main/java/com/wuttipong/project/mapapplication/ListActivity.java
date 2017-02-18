package com.wuttipong.project.mapapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> dataList;
    private CateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        dataList = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            dataList.add("โรงพยาบาล "+i);
        }

        adapter = new CateAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("id", "onItemClick "+id);
                Intent intent = new Intent(ListActivity.this,DetailActivity.class);
//                intent.putExtra()
                startActivity(intent);
            }
        });
    }

    private class CateAdapter extends BaseAdapter {

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
            if (convertView==null){
                mHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_item,parent,false);
                mHolder.textView = (TextView)convertView.findViewById(R.id.txt);
                convertView.setTag(mHolder);
            }else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.textView.setText(dataList.get(position));
            return convertView;
        }
    }

    public static class ViewHolder{
        TextView textView;
    }

}
