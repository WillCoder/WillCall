package com.will.willcall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * @author Will
 *         create at 2013/3
 */
public class MainActivity extends Activity {

    private int Switch = -1;
    public static final int STATE_OPEN = 1;
    public static final int STATE_CLOSE = 0;
    private int FocusGalleryPosition = 0;
    private Typeface tf = null;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_layout);
        FocusGalleryPosition = getSharedPreferences(getPackageName(),
                MODE_PRIVATE).getInt("theme", FocusGalleryPosition);
        Switch = getSharedPreferences(getPackageName(), MODE_PRIVATE).getInt(
                "state", STATE_CLOSE);
        Intent service = new Intent(Intent.ACTION_RUN);
        service.setClass(this, CallService.class);
        startService(service);

        tf = Typeface.createFromAsset(getAssets(), "fonts/gulim.ttc");

        TextView versionText = (TextView) findViewById(R.id.about_version);
        ToggleButton mButton = (ToggleButton) findViewById(R.id.button);
        View mTestButton = findViewById(R.id.test_button);

        /**
         * Set fontfamily
         */
        versionText.setTypeface(tf);
        ((TextView) findViewById(R.id.about_version)).setTypeface(tf);
        ((TextView) findViewById(R.id.app_name)).setTypeface(tf);

        mTestButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),
                        MissingCallActivtiy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("time", Long.valueOf(3000));
                intent.putExtra("incomingNumber", "1999999999");
                v.getContext().startActivity(intent);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("time", Long.valueOf(8000));
                intent.putExtra("incomingNumber", "2999999999");
                v.getContext().startActivity(intent);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("time", Long.valueOf(23000));
                intent.putExtra("incomingNumber", "3999999999");
                v.getContext().startActivity(intent);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("time", Long.valueOf(55000));
                intent.putExtra("incomingNumber", "4999999999");
                v.getContext().startActivity(intent);
                overridePendingTransition(R.anim.evo_enter,
                        R.anim.evo_exit);
            }
        });

        if (Switch == STATE_CLOSE) {
            mButton.setChecked(false);
        } else if (Switch == STATE_OPEN) {
            mButton.setChecked(true);
        }
        mButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                setSwitchState(isChecked);
            }
        });
    }

    public void setSwitchState(boolean isChecked) {
        if (isChecked) {
            this.Switch = STATE_OPEN;
        } else {
            this.Switch = STATE_CLOSE;
        }
        SharedPreferences.Editor editor = getSharedPreferences(
                getPackageName(), MODE_PRIVATE).edit();
        editor.putInt("state", Switch);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
