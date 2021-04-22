package com.example.com_gift;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.com_gift.bean.TypeSelect;
import com.example.com_gift.popview.CustomOperationPopWindow;
import com.example.com_gift.view.HeartLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HeartLayout heartLayout;
    private TextView tv_cz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_animal);

        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        findViewById(R.id.member_send_good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                heartLayout.addFavor();

            }
        });

        tv_cz = findViewById(R.id.tv_cz);
        tv_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TypeSelect> typeSelects = new ArrayList<>();
                typeSelects.add(new TypeSelect("ssss"));
                typeSelects.add(new TypeSelect("aaaa"));
                typeSelects.add(new TypeSelect("zzzz"));
                CustomOperationPopWindow customOperationPopWindow = new CustomOperationPopWindow(MainActivity.this, typeSelects);
                customOperationPopWindow.setOnItemMyListener(new CustomOperationPopWindow.OnItemListener() {
                    @Override
                    public void OnItemListener(int position, TypeSelect typeSelect) {
                        //此处实现列表点击所要进行的操作
                    }
                });
            }
        });


//        mButDz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }
}
