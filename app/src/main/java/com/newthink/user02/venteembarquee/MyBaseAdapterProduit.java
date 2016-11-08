package com.newthink.user02.venteembarquee;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User02 on 28/03/2016.
 */
public  class MyBaseAdapterProduit extends BaseAdapter {

    ArrayList<Produit> myList = new ArrayList<Produit>();
    LayoutInflater inflater;
    Context context;
    private int[] colors = new int[] { 0x24373737, 0x0BF4F4F4 };


    public MyBaseAdapterProduit(Context context, ArrayList<Produit> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public int getCount() {
        return myList.size();
    }

    public Produit getItem(int position) {
        return myList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.style_list_pr_init, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Produit currentListData = getItem(position);

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        mViewHolder.id.setText(String.valueOf(currentListData.getIdProduit()));
        mViewHolder.design.setText(currentListData.getDesignation());
        mViewHolder.qtereel.setText(String.valueOf(currentListData.getStockInitialeReel()));
        mViewHolder.qtestime.setText(String.valueOf(currentListData.getStockInitialeEstime()));
        return convertView;
    }

    private class MyViewHolder {
        TextView id, design,qtestime,qtereel;
        public MyViewHolder(View item) {
            id = (TextView) item.findViewById(R.id.Idproduit);
            design = (TextView) item.findViewById(R.id.Designation);
            qtestime = (TextView) item.findViewById(R.id.qtestime);
            qtereel = (TextView) item.findViewById(R.id.qtereel);
        }
    }
}

