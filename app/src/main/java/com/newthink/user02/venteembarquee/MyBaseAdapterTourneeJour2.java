package com.newthink.user02.venteembarquee;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by User02 on 03/04/2016.
 */
public class MyBaseAdapterTourneeJour2 extends BaseAdapter {
    TextView idClient;
    ArrayList<Etape> myList = new ArrayList<Etape>();
    LayoutInflater inflater;
    //Le contexte dans lequel est présent notre adapter
    Context context;
    private int[] colors = new int[] { 0x24373737, 0x0BF4F4F4 };
    public MyBaseAdapterTourneeJour2(Context context, ArrayList<Etape> myList) {
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

    public Etape getItem(int position) {
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
        Button btntour;


        if(convertView==null)

        {
            convertView = inflater.inflate(R.layout.style_list_tournee, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }

        else

        {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        /*btntour = (Button) convertView.findViewById(R.id.btntour);
        btntour.setTag(position);
        btntour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int position = (Integer) arg0.getTag();
            }

        });*/
        Etape currentListData = getItem(position);

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);
        mViewHolder.client.setText(currentListData.nomClient+" "+currentListData.prenomClient);
        mViewHolder.adresse.setText(currentListData.adresseClient);
        mViewHolder.motif.setText(currentListData.motifVisite);
        mViewHolder.statut.setText(currentListData.statutEtape);
       // mViewHolder.idLiv.setText("0");
        return convertView;

    }

    private class MyViewHolder {
        TextView client, adresse, motif, statut, idLiv;

        public MyViewHolder(View item) {
            client = (TextView) item.findViewById(R.id.Client);
            adresse = (TextView) item.findViewById(R.id.Adresse);
            motif = (TextView) item.findViewById(R.id.MotifVisite);
            statut = (TextView) item.findViewById(R.id.Statut);
            //idLiv = (TextView) item.findViewById(R.id.IdLivraison);
        }
    }
}



