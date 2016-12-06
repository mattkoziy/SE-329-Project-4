package com.example.koziy.partyzone;


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.koziy.partyzone.addressListAdapter;

public class addressListAdapter extends ArrayAdapter<String>{

    private Context context;
    private int layoutResourceId;
    private List<String> data = null;

    public addressListAdapter(Context context, int layoutResourceId, List<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView tv = new TextView(context);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            tv = (TextView) row.findViewById(R.id.addressHolder);

            row.setTag(tv);
        } else {
            tv = (TextView) row.getTag();
        }

        tv.setText(showAddressList.ADDRESS);

        return row;
    }

}
