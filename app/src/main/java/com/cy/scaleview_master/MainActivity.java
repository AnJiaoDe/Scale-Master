package com.cy.scaleview_master;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startAppcompatActivity(ScaleViewActivity.class);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
