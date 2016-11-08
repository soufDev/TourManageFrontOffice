package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;


public class Cloture2 extends Activity {

    public static final int SIGNATURE_ACTIVITY = 1;
    public static String n;
    Button btnAcceuil,btndeconnexion, btndemarrer;
    TabHost tabHost;
    int i=0,j=0;
    String cashText;
    // Context context = tournee_activity.this;
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    public  int KMF;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometragedebut,kilometragefin,cashf;
    ListView list;
    int cashInit;
    EditText Date_Debut,DateFin, EcartStock, EcartCaisse;
    Context context = Cloture2.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloture2);

        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n = b.getString("loginselectionne");
        i = b.getInt("id_agent");
        KMF = b.getInt("km_f");
        cashText = b.getString("cashf");
        loginDisplay.setText(n);
        /****************/
        list = (ListView) findViewById(R.id.ListPrduitClient);
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cloture2.this, acceiul_activity.class);
                intent.putExtra("loginselectionne", n);
                intent.putExtra("id_agent", i);
                startActivity(intent);
                finish();
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Cloture2.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });

        DB.open();

        Cursor c2=DB.getIdTournee(i);

        if(c2.moveToFirst()){
            j = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
            Log.d("synchro_Tournee","Cloture 2 j = "+j);
        }


        refreshListInventaire();

    }

    public void refreshListInventaire () {

        DB.open();

        myList.clear();
        Cursor c = DB.geInventaire(j);
        if (c.moveToFirst()) {
            do {

                Log.d("inventaire",c.getColumnName(0)+" = "+c.getString(0)+" "+c.getColumnName(1)+" = "+c.getString(1)+c.getColumnName(2)+" = "+c.getString(2)+" "+c.getColumnName(3)+" = "+c.getString(3));

                int id = c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                String n = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                int is = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE));
                int vs =c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE));
                getDataInList(myList, id, n, is, vs);

            } while (c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterInvent(context, myList));

        DB.close();
    }

    private void getDataInList(ArrayList<Produit> li, int p, String a, int b,int c) {
        // Create a new object for each list item
        Produit ld = new Produit();
        ld.setIdProduit(p);
        ld.setDesignation(a);
        ld.setStockFinalEstime(b);
        ld.setStockFinalReel(c);
        // Add this object into the ArrayList myList
        li.add(ld);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void RapportDecart(int i,int SFE,int SFR,int j){

        String sql2 ="update tracestocktournee " +
                "SET stockFinalEstime  = "+SFE+", stockFinalReel="+SFR+"  " +
                "WHERE idProduit="+i+" and idTournee="+j+"";

        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, user, pass);
            Statement st2=con.createStatement();
            st2.executeUpdate(sql2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void suivant(View args){

        DB.open();

        myList.clear();
        Cursor c = DB.geInventaire(j);
        if (c.moveToFirst()) {
            do {

                Log.d("inventaire",c.getColumnName(0)+" = "+c.getString(0)+" "+c.getColumnName(1)+" = "+c.getString(1)+c.getColumnName(2)+" = "+c.getString(2)+" "+c.getColumnName(3)+" = "+c.getString(3));

                int id = c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                String n = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                int is = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE));
                int vs =c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE));
                //getDataInList(myList, id, n, is, vs);
                RapportDecart(id, is, vs, j);
            } while (c.moveToNext());
            c.close();
        }
        //list.setAdapter(new MyBaseAdapterInvent(context, myList));

        DB.close();



        Intent intent = new Intent(Cloture2.this, Cloture3.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        intent.putExtra("cashf", cashText);
        intent.putExtra("km_f",KMF);
        startActivity(intent);
        finish();

    }

    public void precedent(View args){

        Intent intent = new Intent(Cloture2.this, Cloture1.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();

    }

    public void annuler(View v){
        Intent intent = new Intent(Cloture2.this, acceiul_activity.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();
    }


}
