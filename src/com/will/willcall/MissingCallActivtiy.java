package com.will.willcall;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * @author Will
 *         create at 2013/3
 */
public class MissingCallActivtiy extends Activity {

    private class PageAction {
        public final static int PAGE_UP = 0;
        public final static int PAGE_DOWN = 1;
        public final static int PAGE_NOTING = 1;
    }

    private int PageCount = 0;
    private ArrayList<MissingCallAdapter> mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
    private View ButtonToLeft = null;
    private View ButtonToRight = null;
    private int Theme = ThemeType.CLASSIC_THEME;

    private static Typeface tfGulim = null;
    private static Typeface tfClockopia = null;
//	private Typeface tfAndroidClock = null;

    public class ThemeType {
        public static final int CLASSIC_THEME = 0;
        public static final int WINDOWS_THEME = 1;
        public static final int ANDROID_THEME = 2;
        public static final int IPHONE_THEME = 3;
    }
//    public enum ThemeType{CLASSIC_THEME};

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.missing_call_dialog_android_theme);
        Theme = getSharedPreferences(getPackageName(), MODE_PRIVATE).getInt("theme", ThemeType.CLASSIC_THEME);
        tfGulim = Typeface.createFromAsset(getAssets(), "fonts/gulim.ttc");
        tfClockopia = Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf");
        long mCallTimer = getIntent().getLongExtra("time", -1);
        String incomingNumber = getIntent().getStringExtra("incomingNumber");
        mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
        setMissingInfo(mMissingCallAdapter.get(PageCount), PageAction.PAGE_NOTING);
    }

    /*
     * If this Activity is called twice, onNewIntent work(non-Javadoc)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        long mCallTimer = intent.getLongExtra("time", -1);
        String incomingNumber = intent.getStringExtra("incomingNumber");

        mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
        ButtonToLeft = (View) findViewById(R.id.btn_to_left);
        ButtonToRight = (View) findViewById(R.id.btn_to_right);
        ButtonToRight.setVisibility(View.VISIBLE);
        ButtonToRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PageCount++;
                ButtonToLeft.setVisibility(View.VISIBLE);
                if (PageCount == mMissingCallAdapter.size() - 1) {
                    if (Theme == ThemeType.CLASSIC_THEME) {
                        v.setVisibility(View.GONE);
                    } else if (Theme == ThemeType.WINDOWS_THEME) {
                        v.setVisibility(View.INVISIBLE);
                    } else if (Theme == ThemeType.ANDROID_THEME) {
                        findViewById(R.id.page_divider).setVisibility(View.GONE);
                        v.setVisibility(View.GONE);
                    } else if (Theme == ThemeType.IPHONE_THEME) {
                        v.setVisibility(View.GONE);
                    }
                } else {
                    if (Theme == ThemeType.ANDROID_THEME) {
                        findViewById(R.id.page_divider).setVisibility(View.VISIBLE);
                    }
                }
                setMissingInfo(mMissingCallAdapter.get(PageCount), PageAction.PAGE_DOWN);
            }
        });
//		ButtonToLeft.setVisibility(View.VISIBLE);
        ButtonToLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PageCount--;
                ButtonToRight.setVisibility(View.VISIBLE);
                if (PageCount == 0) {
                    if (Theme == ThemeType.CLASSIC_THEME) {
                        v.setVisibility(View.GONE);
                    } else if (Theme == ThemeType.WINDOWS_THEME) {
                        v.setVisibility(View.INVISIBLE);
                    } else if (Theme == ThemeType.ANDROID_THEME) {
                        findViewById(R.id.page_divider).setVisibility(View.GONE);
                        v.setVisibility(View.GONE);
                    } else if (Theme == ThemeType.IPHONE_THEME) {
                        v.setVisibility(View.GONE);
                    }
                } else {
                    if (Theme == ThemeType.ANDROID_THEME) {
                        findViewById(R.id.page_divider).setVisibility(View.VISIBLE);
                    }
                }
                setMissingInfo(mMissingCallAdapter.get(PageCount), PageAction.PAGE_UP);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.evo_enter, R.anim.evo_exit);
    }

    /**
     * Set layout info
     *
     * @param mMissingCallAdapter
     */
    public void setMissingInfo(MissingCallAdapter mMissingCallAdapter, int pageAction) {
        EasyTracker.getInstance(this).send(MapBuilder.createEvent("Theme", "ANDROID", null, Long.valueOf(Theme)).build());
        startAnimationThemeAndroid(pageAction);
        long mCallTimer = mMissingCallAdapter.getTime();
        long Second = mCallTimer / 1000;
        final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
        TextView mcdText_1 = (TextView) findViewById(R.id.mcd_name_1);
        TextView mcdText_2 = (TextView) findViewById(R.id.mcd_name_2);
        TextView timeText = (TextView) findViewById(R.id.mcd_time);

        View reCallButton = findViewById(R.id.recall_btn);
        ImageView ContactImageView = (ImageView) findViewById(R.id.contact_image);
        ImageView reCallIcon = (ImageView) findViewById(R.id.btn_recall_icon);

        String ContactsName = getContactsInfo(incomingNumber);
        Bitmap ContactsPhoto = getContactsPhoto(incomingNumber);

//			mcdText_2.setTypeface(tfGulim);
        timeText.setTypeface(tfClockopia);
//			titleText.setTypeface(tfGulim);
        if (ContactsName == null) {
            mcdText_1.setText(R.string.unknow_contact_name);
            mcdText_2.setText(incomingNumber);
        } else {
            mcdText_1.setText(ContactsName);
            mcdText_2.setText(incomingNumber);
        }
        if (ContactsPhoto != null) {
            ContactImageView.setImageBitmap(ContactsPhoto);
        } else {
            ContactImageView.setImageResource(R.drawable.contact_icon_win_theme);
        }
        reCallIcon.setImageResource(getReCallIcon(Second));
        timeText.setText(Long.toString(Second) + getString(R.string.second));
        timeText.setTextColor(getTimeColor(Second));

        reCallButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String phone = incomingNumber;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

    }

    /**
     * Android theme Layout animation
     * In
     */
    private void startAnimationThemeAndroid(int pageAction) {
        if (Theme != ThemeType.ANDROID_THEME) {
            return;
        }
        if (pageAction == PageAction.PAGE_DOWN) {
            TextView timeText = (TextView) findViewById(R.id.mcd_time);
            Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.theme_android_in_left_top);
            timeText.startAnimation(animation_4);
        } else if (pageAction == PageAction.PAGE_UP) {
            TextView timeText = (TextView) findViewById(R.id.mcd_time);
            Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.theme_android_in_left_bottom);
            timeText.startAnimation(animation_4);
        }


    }

    /**
     * Set the textView font Bold,but can be instead by xml
     *
     * @param text
     */
    public void setFakeBoldText(TextView text) {
        TextPaint tp = text.getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * Contacts name
     *
     * @param address
     * @return
     */
    public String getContactsInfo(String address) {
        String personName = null;
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
        Cursor phoneCursor = getContentResolver().query(
                personUri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null,
                null,
                null);
        if (phoneCursor.moveToFirst()) {
            personName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        phoneCursor.close();
        return personName;
    }

    /**
     * Contacts Photo
     *
     * @param address
     * @return
     */
    public Bitmap getContactsPhoto(String address) {
        Bitmap personName = null;
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
        Cursor phoneCursor = getContentResolver().query(
                personUri,
                new String[]{ContactsContract.PhoneLookup._ID},
                null,
                null,
                null);
        if (phoneCursor.moveToFirst()) {
            long contactId = phoneCursor.getLong(phoneCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);
            personName = BitmapFactory.decodeStream(input);
        }
        phoneCursor.close();
        return personName;
    }

    /**
     * @param time second
     * @return
     */
    public int getTimeColor(long time) {
        int ret = 0;
        if (time <= 5) {
            ret = Color.parseColor("#FF4444");
        } else if (time <= 10 && time > 5) {
            ret = Color.parseColor("#FFBB33");
        } else if (time > 10) {
            ret = Color.parseColor("#99CC00");
        }
        return ret;
    }

    /**
     * @param time second
     * @return
     */
    public int getReCallIcon(long time) {
        int ret = 0;
        if (time <= 5) {
            ret = R.drawable.recall_poor_win_theme;
        } else if (time <= 10 && time > 5) {
            ret = R.drawable.recall_warming_win_theme;
        } else if (time > 10) {
            ret = R.drawable.recall_good_win_theme;
        }
        return ret;
    }

    /**
     * @param time second
     * @return
     */
    public long getBreathTime(long time) {
        long ret = 0;
        if (time <= 5) {
            ret = 900;
        } else if (time <= 10 && time > 5) {
            ret = 1000;
        } else if (time > 10) {
            ret = 1100;
        }
        return ret;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
