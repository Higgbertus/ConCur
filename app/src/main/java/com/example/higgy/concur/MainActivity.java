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

public class MainActivity extends AppCompatActivity {

    private ExchangeRateDatabase exchangeRateDB;
    private CurrencyItemAdapter currencyItemAdapter;
    private Spinner spinnerFrom,spinnerTo;
    private EditText editText;
    private TextView textView;
    private Toolbar toolbar;

    private Intent intent;
    ShareActionProvider shareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exchangeRateDB = new ExchangeRateDatabase();
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
        if (intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null){
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }

        shareActionProvider.setShareIntent(shareIntent);
    }



    public void onClick(View view){
        textView.setText(String.format("%.2f", exchangeRateDB.convert(Double.parseDouble(editText.getText().toString()),spinnerFrom.getSelectedItem().toString(),spinnerTo.getSelectedItem().toString())));
        setShareText((String) textView.getText());
    }

}
