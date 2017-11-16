package com.example.higgy.concur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CurActivity extends AppCompatActivity {


    private ConAdapter conAdapter;
    private ExchangeRateDatabase exchangeRateDB;
    private ListView listView;
    private CurrencyItemAdapter currencyItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur);
        listView = (ListView) findViewById(R.id.listView);

        exchangeRateDB = new ExchangeRateDatabase();
        currencyItemAdapter = new CurrencyItemAdapter(exchangeRateDB);
        conAdapter = new ConAdapter(exchangeRateDB);
        listView.setAdapter(currencyItemAdapter);
    }
}
