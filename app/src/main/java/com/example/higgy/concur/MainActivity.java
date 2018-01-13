package com.example.higgy.concur;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    public ExchangeRateDatabase exchangeRateDB;
    private ConAdapter currencyItemAdapter;
    private Spinner spinnerFrom, spinnerTo;
    private EditText editText;
    private TextView textView;
    private Toolbar toolbar;
    private Intent intent;
    ShareActionProvider shareActionProvider;
    private ExchangeRateUpdateRunnable runnable;

    private static final int JOB_ID = 101;


private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exchangeRateDB = new ExchangeRateDatabase();
        currencyItemAdapter = new ConAdapter(exchangeRateDB);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerFrom = (Spinner) findViewById(R.id.spinner);
        spinnerFrom.setAdapter(currencyItemAdapter);
        spinnerTo = (Spinner) findViewById(R.id.spinner2);
        spinnerTo.setAdapter(currencyItemAdapter);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        runnable = new ExchangeRateUpdateRunnable(exchangeRateDB,this);
        thread = new Thread(runnable);

//        ComponentName serviceName = new ComponentName(this, UpdateJobService.class);
//        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                .setRequiresDeviceIdle(false)
//                .setRequiresCharging(false)
//                .setPeriodic(86400000).build();
//
//        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        int res = scheduler.schedule(jobInfo);
//        if (res == JobScheduler.RESULT_SUCCESS){
//            Log.d("JobService", "Job scheduled successfully");
//        }
    }

    private void updateViews(){
        currencyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refreshlist_menu:
//                new Thread(new Runnable() {
//                    public void run() {
//                        updateDB();
//                    }
//                }).start();
//                updateViews();
                if (!thread.isAlive()) {
                    thread = new Thread(runnable);
                    thread.start();
                }else{
                    Toast.makeText(this,"Update already in progress",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.curlist_menu:
                intent = new Intent(MainActivity.this, CurActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

//    private void updateDB() {
//        String query = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
//        try {
//            URL url = new URL(query);
//            URLConnection connection = url.openConnection();
//
//            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
//            parser.setInput(connection.getInputStream(), connection.getContentEncoding());
//
//            int eventType = parser.getEventType();
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_TAG) {
//                    if ("Cube".equals(parser.getName())) {
//                        if (parser.getAttributeValue(null, "currency") != null) {
//                            //,Double.parseDouble(parser.getAttributeValue(null, "rate"))
//                            exchangeRateDB.setExchangeRate(parser.getAttributeValue(null, "currency"), Double.parseDouble(parser.getAttributeValue(null, "rate")));
//
//                        }
//                    }
//                }
//                eventType = parser.next();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
DBHelper dbHelper = new DBHelper(this);
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("source", spinnerFrom.getSelectedItem().toString());
        editor.putString("target", spinnerTo.getSelectedItem().toString());
        editor.putString("value", editText.getText().toString());
        editor.apply();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < exchangeRateDB.getCurrencies().length; i++) {
            values.put(dbHelper.CUR_COL_TITLE,exchangeRateDB.getCurrencies()[i]);
            values.put(dbHelper.CUR_COL_VALUE,exchangeRateDB.getExchangeRate(exchangeRateDB.getCurrencies()[i]));
            db.insert(dbHelper.CUR_TABLE, null, values);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String target = sp.getString("target","");
        String source = sp.getString("source","");

        for (int i = 0; i < exchangeRateDB.getCurrencies().length; i++) {
            String temp = exchangeRateDB.getCurrencies()[i];
         if (temp.equals(target)){
             spinnerFrom.setSelection(i);
         }
        }
        for (int i = 0; i < exchangeRateDB.getCurrencies().length; i++) {
            String temp = exchangeRateDB.getCurrencies()[i];
            if (temp.equals(source)){
                spinnerTo.setSelection(i);
            }
        }
        editText.setText(sp.getString("value", ""));

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {BaseColumns._ID, dbHelper.CUR_COL_TITLE, dbHelper.CUR_COL_VALUE};
            String selection = dbHelper.CUR_COL_TITLE + " = ?";
            String[] selectionArgs =  {"AUD"};
            Cursor c = db.query(dbHelper.CUR_TABLE, projection, selection,selectionArgs,null,null,null);
            while (c.moveToNext()){
                exchangeRateDB.setExchangeRate(c.getString(c.getColumnIndexOrThrow(dbHelper.CUR_COL_TITLE)), Double.parseDouble(c.getString(c.getColumnIndexOrThrow(dbHelper.CUR_COL_VALUE))));
            }
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }



    }

    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        shareActionProvider.setShareIntent(shareIntent);
    }

    public void onClick(View view) {
        textView.setText(String.format("%.2f", exchangeRateDB.convert(Double.parseDouble(editText.getText().toString()), spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString())));
        setShareText((String) textView.getText());
    }

}
