package com.cy.scaleview_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.cy.scaleview.ScaleFrameLayout;
import com.cy.scaleview.ScreenUtils;

public class ScaleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_view);

        View layout = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        ScaleFrameLayout scaleFrameLayout = new ScaleFrameLayout(this);

        scaleFrameLayout.setLayoutParams(new RelativeLayout.LayoutParams(500,1300));
        scaleFrameLayout.addView(layout);
//        scaleFrameLayout.config(false, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this),
//                ScreenUtils.getScreenWidth(this)/2,ScreenUtils.getScreenHeight(this)/2);
        scaleFrameLayout.config(false, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this),0,0);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_scale_view);
        relativeLayout.addView(scaleFrameLayout);

    }
}
