package com.example.dekhoattendance.Adepter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dekhoattendance.Model.LeavetypeModel;
import com.example.dekhoattendance.R;

import java.util.ArrayList;

public class LeaveTypeAdepter extends BaseAdapter {


    private Context context;
    private ArrayList<LeavetypeModel> items;

    public LeaveTypeAdepter(Context context, ArrayList<LeavetypeModel> stateData) {
        this.context = context;
        this.items = stateData;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt, 0);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt, 1);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, int loc) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.drop_downitem, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.name);
        main_text.setText(items.get(position).getLeaveType());

        return mySpinner;
    }

    static class ViewHolder {
        public TextView name;
    }

}
