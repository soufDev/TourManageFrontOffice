package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Cloture1 extends Activity {
    public static final int SIGNATURE_ACTIVITY = 1;
    public static String n;
    Button btnAcceuil,btndeconnexion, btndemarrer;
    TabHost tabHost;
    int i=0,j=0;
    // Context context = tournee_activity.this;
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    public static int KMF;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometragedebut,kilometragefin,cashf;
    ListView list;
    int cashInit;
    EditText Date_Debut,DateFin, EcartStock, EcartCaisse;
    Context context = Cloture1.this;

    /* afficher le nom d'utilisateur connecte */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloture1);

        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n = b.getString("loginselectionne");
        i = b.getInt("id_agent");
        loginDisplay.setText(n);
        /****************/
        list = (ListView) findViewById(R.id.ListPrduitClient);
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cloture1.this, acceiul_activity.class);
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
                Intent intent3 = new Intent(Cloture1.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });


        DB.open();

        Cursor c2=DB.getIdTournee(i);

        if(c2.moveToFirst()){
            j = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));

            Log.d("synchro_Tournee","Cloture 1 j = "+j);
        }

        kilometragedebut =(EditText) findViewById(R.id.KMDebut);
       // Date_Debut = (EditText)findViewById(R.id.DateDebut);
        /* Afficher la date de debut de tournee */
        Cursor c4 = DB.getInfoTour(j);
        c4.moveToFirst();
        if (c4.getCount() > 0) {
            kilometragedebut.setText(String.valueOf(c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))));
           // Date_Debut.setText(c4.getString(c4.getColumnIndex(DatabaseManager.KEY_DATE_DEBUT_TORUNEE)));
           // cashInit  = c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE));
        } else {
            //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c.getCount(), Toast.LENGTH_LONG).show();
        }

        DB.close();

    }

    public void suivant(View arg) {

        cashf = (EditText)findViewById(R.id.CashFinal);
        kilometragefin = (EditText)findViewById(R.id.KMFin);
        if (kilometragefin.getText().toString().trim().length()<=0)
        {
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        }
        else
        {
            String cashText= cashf.getText().toString();
            String  kilomfintext= kilometragefin.getText().toString();
            int Cach=Integer.parseInt(cashText);
            KMF=Integer.parseInt(kilomfintext);
            //DB.open();
            //agent a = new agent();
            //a.setKMFin(KMF);
           // DB.saveVehicule(a);
         //   DB.UpdateCash(Cach,j);
           // DB.close();

            Intent intent = new Intent(Cloture1.this,Cloture2.class);
            intent.putExtra("loginselectionne", n);
            intent.putExtra("id_agent", i);
            intent.putExtra("km_f",KMF);
            intent.putExtra("cashf", cashText);
            startActivity(intent);
            finish();

        }

    }

    public  void annuler(View args){
        Intent intent = new Intent(Cloture1.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent",i);
        startActivity(intent);
        finish();
    }

}
