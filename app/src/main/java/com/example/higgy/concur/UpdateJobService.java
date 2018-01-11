package com.example.higgy.concur;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Higgy on 27.12.2017.
 */

public class UpdateJobService extends JobService {
    UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(this,         this.exchangeRateDB);

    public void setDB(ExchangeRateDatabase exchangeRateDB){
        this.exchangeRateDB = exchangeRateDB;
    }


    private ExchangeRateDatabase exchangeRateDB;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private static class UpdateAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {

        private final JobService jobService;
        private ExchangeRateDatabase exchangeRateDB;

        public UpdateAsyncTask(JobService service, ExchangeRateDatabase exchangeRateDB) {
            this.jobService = service;
            this.exchangeRateDB = exchangeRateDB;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            Log.i("Start", "Update started");
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
            //notifier.removeNotification();
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobService.jobFinished(jobParameters, false);
        }
    }
}
