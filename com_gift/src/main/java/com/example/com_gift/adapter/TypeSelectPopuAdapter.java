package com.example.com_gift.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.com_gift.bean.TypeSelect;

import java.util.ArrayList;

public class TypeSelectPopuAdapter implements Adapter {
    private Context context;
    private ArrayList<TypeSelect> list;

    public TypeSelectPopuAdapter(Context context, ArrayList<TypeSelect> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TypeSelect typeSelect = list.get(position);
        String name = typeSelect.getName();
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
