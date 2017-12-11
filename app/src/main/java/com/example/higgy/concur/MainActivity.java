package com.example.higgy.concur;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
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
//                        // a potentially  time consuming task
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
