
package com.teffy.setdefault;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.teffy.setdefault.set.PhoneTypeUtils;

public class TwoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("ccc");
        setContentView(tv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PhoneTypeUtils.gobackHome(this);
    }
}
