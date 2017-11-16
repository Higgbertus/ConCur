package com.example.higgy.concur;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CurActivity extends AppCompatActivity {


    private ConAdapter conAdapter;
    private ExchangeRateDatabase exchangeRateDB;
    private ListView listView;
    private CurrencyItemAdapter currencyItemAdapter;
private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur);
        listView = (ListView) findViewById(R.id.listView);

        exchangeRateDB = new ExchangeRateDatabase();
        currencyItemAdapter = new CurrencyItemAdapter(exchangeRateDB);
        conAdapter = new ConAdapter(exchangeRateDB);
        listView.setAdapter(currencyItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0`?q="+exchangeRateDB.getCapital(exchangeRateDB.getCurrencies()[i])));

                if (intent.resolveActivity(getPackageManager())!= null){
                    startActivity(intent);
                }
            }
        });
    }


}
