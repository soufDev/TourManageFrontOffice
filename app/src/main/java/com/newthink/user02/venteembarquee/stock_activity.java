package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by User02 on 16/03/2016.
 */
public class stock_activity extends Activity {
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    ListView list;
    Context context = stock_activity.this;
    Button btnAcceuil;
    Button btndeconnexion;
    public static String n,Designation ;
    public static int i,j, StockInitR, StockInitiE ;
    EditText qteEstimeEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_layout);
        list=(ListView) findViewById(R.id.listStock);
        /*Affichage de l'agent connecté*/
        /*****************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        i=b.getInt("id_agent");
        Log.d("idididid", "id = " + i);
        loginDisplay.setText(n);
        /****************/
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stock_activity.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",n);
                intent.putExtra("id_agent",i);
                startActivity(intent);
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(stock_activity.this,MainActivity.class);
                intent3.putExtra("loginselectionne",n);
                startActivity(intent3);
            }});
        DB.open();
        //Cursor c = DB.getIdAgent(n);
        //if(c.moveToFirst()){
          //  i = c.getInt(c.getColumnIndex(DatabaseManager.AGENT_KEY));
        //}
        Cursor c2=DB.getIdTournee(i);
        if(c2.moveToFirst()){
            j = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
        }

       refreshListStock();
        //UpdateItem();
        DB.close();

    }
    public void refreshListStock() {
        int SE=0,SRI=0;
        myList.clear();
        Cursor c = DB.getStockEncours(j);
        if (c.moveToFirst()) {
            do {
                  Designation = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                  SE  = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_EN_COURS_STOCK_TOURNEE));
                  SRI = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                getDataInList(myList, Designation,SRI , SE);
            } while(c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterStock(context, myList));

    }
    private void getDataInList(ArrayList<Produit> li, String p, int a,int b) {
        // Create a new object for each list item
        Produit ld = new Produit();
        ld.setDesignation(p);
        ld.setStockInitialeReel(b);
        ld.setStockEncours(a);
        // Add this object into the ArrayList myList
        li.add(ld);
    }
    public void UpdateItem() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                final Produit a = myList.get(position);
                final int qi = a.getStockInitialeReel();
                final int qe = a.getStockInitialeEstime();
                final int idp = a.getIdProduit();
                if (qi != qe) {
                    int motifdecart = qi - qe;
                    Toast.makeText(context, "le motif d'ecart :" + motifdecart, Toast.LENGTH_LONG).show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = stock_activity.this.getLayoutInflater();
                builder.setTitle("Item");
                builder.setMessage(" Vous pouvez changer la quantite ");
                final View vw = inflater.inflate(R.layout.dialogue_layout, null);
                builder.setView(vw);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        qteEstimeEditText = (EditText) vw.findViewById(R.id.quantite);
                        final String QUANTITE = qteEstimeEditText.getText().toString();
                        int QTE = Integer.parseInt(QUANTITE);
                        DB.open();
                        if (QTE > qi) {
                            Toast.makeText(context, "Désolé ! Veuillez ressaisir la quantité  :", Toast.LENGTH_LONG).show();
                        } else {
                            DB.UpdateSTE(QTE, idp);
                            Toast.makeText(context, "quantité Estimee change :" + QTE, Toast.LENGTH_LONG).show();
                        }
                        refreshListStock();
                        if (qi != QTE) {
                            int motifdecart = qi - QTE;
                            Toast.makeText(context, "le motif d'ecart :" + motifdecart, Toast.LENGTH_LONG).show();
                        }
                        refreshListStock();
                        DB.close();
                    }
                });
                builder.setNegativeButton("Non", null);
                builder.show();
            }
        });
    }

}
