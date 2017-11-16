package com.example.higgy.concur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Higgy on 01.11.2017.
 */

public class ConAdapter extends BaseAdapter {
    private ExchangeRateDatabase data;

    public ConAdapter(ExchangeRateDatabase data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getCurrencies().length;
    }

    @Override
    public Object getItem(int i) {
        return data.getCurrencies()[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        String entry = data.getCurrencies()[i];

        if (view == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.spinner_item,null,false);
        }
        TextView textView = view.findViewById(R.id.tv_cur);
        textView.setText(entry);

        TextView textView1 = view.findViewById(R.id.tv_factor);
        textView1.setText(String.format("%.4f", data.getExchangeRate(entry)));

        return view;
    }
}
