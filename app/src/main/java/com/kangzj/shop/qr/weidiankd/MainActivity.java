package com.kangzj.shop.qr.weidiankd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kangzj.shop.qr.weidiankd.wdapi.WeidianAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeidianAPI.getInstance(MainActivity.this).getOrderList(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status_code = (Integer) ((JSONObject) response.get("status")).get("status_code");
                            WeidianAPI.refreshToken(status_code);
                            Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, CaptureActivity.class);
//                startActivity(intent);
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                String item = ((TextView) view).getText().toString();
//                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("num", "111111111111");
                Intent intent = new Intent();
                intent.putExtras(bundle);

            }
        });
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, refreshData()));


    }

    private List<String> refreshData() {

        List<String> data = new ArrayList<>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        return data;
    }
}
