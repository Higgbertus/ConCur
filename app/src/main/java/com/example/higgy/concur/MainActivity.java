package com.example.higgy.concur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ExchangeRateDatabase exchangeRateDB;
    private ConAdapter conAdapter;
    private CurrencyItemAdapter currencyItemAdapter;
    private Spinner spinnerFrom,spinnerTo;
    private EditText editText;
    private TextView textView;
    private Toolbar toolbar;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exchangeRateDB = new ExchangeRateDatabase();
        conAdapter = new ConAdapter(exchangeRateDB);
        currencyItemAdapter = new CurrencyItemAdapter(exchangeRateDB);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerFrom = (Spinner) findViewById(R.id.spinner);
        spinnerFrom.setAdapter(currencyItemAdapter);
        spinnerTo = (Spinner) findViewById(R.id.spinner2);
        spinnerTo.setAdapter(currencyItemAdapter);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        intent = new Intent(MainActivity.this, CurActivity.class);
        
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public void onClick(View view){
        textView.setText(String.format("%.2f", exchangeRateDB.convert(Double.parseDouble(editText.getText().toString()),spinnerFrom.getSelectedItem().toString(),spinnerTo.getSelectedItem().toString())));
    }

}
