package com.liangfu.read.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.liangfu.read.R;

public class TitleBarActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_bar);
    }

    @Override
    public void onClick(View view) {

    }
}
