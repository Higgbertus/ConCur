package com.example.higgy.concur;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Fabi on 10.12.2017.
 */

public class ExchangeRateUpdateRunnable implements Runnable {
    private ExchangeRateDatabase exchangeRateDB;
    private Activity activity;
    private UpdateNotifier notifier;

    public ExchangeRateUpdateRunnable(ExchangeRateDatabase exchangeRateDB, Activity mainActivity) {
        this.exchangeRateDB = exchangeRateDB;
        activity = mainActivity;
        this.notifier = new UpdateNotifier(mainActivity);
    }

    @Override
    public void run() {
        synchronized (ExchangeRateUpdateRunnable.this) {
            updateDB();
            updateUI();
        }
    }

    private void updateUI() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity,"Database updated!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void updateDB() {
        Log.i("Start", "Update started");
        notifier.showNotification("Update started...");
        String query = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        try {
            URL url = new URL(query);
            URLConnection connection = url.openConnection();

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(connection.getInputStream(), connection.getContentEncoding());

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("Cube".equals(parser.getName())) {
                        if (parser.getAttributeValue(null, "currency") != null) {
                            //,Double.parseDouble(parser.getAttributeValue(null, "rate"))
                            exchangeRateDB.setExchangeRate(parser.getAttributeValue(null, "currency"), Double.parseDouble(parser.getAttributeValue(null, "rate")));
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Finished", "Update finished");
        notifier.showNotification("Update finished...");
        //notifier.removeNotification();
    }
}
