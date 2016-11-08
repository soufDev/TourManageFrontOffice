package com.newthink.user02.venteembarquee;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User02 on 12/04/2016.
 */
public class MyBaseAdapterStock extends BaseAdapter {
    ArrayList<Produit> myList = new ArrayList<Produit>();
    LayoutInflater inflater;
    //Le contexte dans lequel est présent notre adapter
    Context context;
    private int[] colors = new int[] { 0x24373737, 0x0BF4F4F4 };


    public MyBaseAdapterStock(Context context, ArrayList<Produit> myList) {
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
    /*Cette méthode permet de connaître le nombre d'items présent dans la liste*/
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
            convertView = inflater.inflate(R.layout.style_list_livr, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        Produit currentListData = getItem(position);
        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);
        mViewHolder.idLivraison.setText(String.valueOf(currentListData.getDesignation()));
        mViewHolder.QuantiteReel.setText(String.valueOf(currentListData.getStockEncours()));
        mViewHolder.QuantiteEsti.setText(String.valueOf(currentListData.getStockInitialeReel()));
        return convertView;
    }
    private class MyViewHolder {
        TextView idLivraison,QuantiteEsti,QuantiteReel;
        public MyViewHolder(View item) {
            idLivraison= (TextView) item.findViewById(R.id.IdLivra);
            QuantiteReel = (TextView) item.findViewById(R.id.NomCli);
            QuantiteEsti = (TextView) item.findViewById(R.id.etatLi);
        }
    }

}
