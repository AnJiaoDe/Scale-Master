package com.cy.scaleview_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2018/11/20 0020.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void startAppcompatActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

}
