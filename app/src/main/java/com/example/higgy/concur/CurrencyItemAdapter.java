package com.example.higgy.concur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Fabi on 13.11.2017.
 */

public class CurrencyItemAdapter extends BaseAdapter {
    private ExchangeRateDatabase data;

    public CurrencyItemAdapter(ExchangeRateDatabase data) {
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
        String value = String.valueOf(data.getExchangeRate(data.getCurrencies()[i]));
        int imageID = context.getResources().getIdentifier("flag_"+entry.toLowerCase(),"drawable", context.getPackageName());

        if (view == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(R.layout.listview_image_values,null,false);
        }
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(imageID);

        TextView textView = view.findViewById(R.id.textView);
        textView.setText(entry);

        TextView textView2 = view.findViewById(R.id.textView2);
        textView2.setText(value);

        return view;
    }
}
