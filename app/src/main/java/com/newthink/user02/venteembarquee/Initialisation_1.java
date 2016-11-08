package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Initialisation_1 extends Activity {

    /*Connexion avec MYSQL */
    /*Entrer l'URL*/
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    /********************************************/

    public static final int SIGNATURE_ACTIVITY = 1;
    Button btnAcceuil,btndeconnexion, btndemarrer;
    TabHost tabHost;
    // Context context = tournee_activity.this;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometrage,cashRecu, stockestimeEditText;
    Context context = Initialisation_1.this;
    public static String n;
    public static int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation_1);

        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        final Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        i=b.getInt("id_agent");
        loginDisplay.setText(n);

        /********************************Le header****************************************/
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Initialisation_1.this, acceiul_activity.class);
                intent.putExtra("loginselectionne", n);
                Log.d("idAgentMenu", "id = " + i);
                intent.putExtra("id_agent", i);
                startActivity(intent);
                finish();
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Initialisation_1.this, MainActivity.class);
                intent3.putExtra("loginselectionne", n);
                intent3.putExtra("id_agent",i);
                startActivity(intent3);
                finish();
            }
        });
        /*********************************************************************************/

        kilometrage =(EditText) findViewById(R.id.KMDebut);
        cashRecu = (EditText)findViewById(R.id.CashRecu);
        DB.open();
        Cursor c2=DB.getIdTournee(i);
        if(c2.moveToFirst()){
            j = c2.getInt(0);
            Log.d("MYLOG","idLivraison = "+j);
        }
        else{
            Log.d("MYLOG","idLivraison = last null");
        }
        DB.close();

    }



    public  void SaveInfo(int l){


        String cashText= cashRecu.getText().toString();
        String kilomtext= kilometrage.getText().toString();
        int KMDVar=Integer.parseInt(kilomtext);
        int Cach=Integer.parseInt(cashText);
        DB.open();
        agent a = new agent();
        a.setCachRecu(Cach);
        a.setKMDebut(KMDVar);
        // DB.saveCach(a, i);
        DB.SaveInitTournee(KMDVar, Cach, j);



        DB.close();

    }

    public void suivant(View arg) {


        if(kilometrage.getText().toString().equals("") || cashRecu.getText().toString().equals("")) {
            Toast.makeText(context,"Veuillez remplir tous les champs",Toast.LENGTH_SHORT).show();
        }
        else{
            SaveInfo(j);
            //testDB();
            Intent intent = new Intent(Initialisation_1.this, Initialisation_2.class);
            intent.putExtra("loginselectionne",n);
            Log.d("idAgentMenu", "id = " + i);
            intent.putExtra("id_agent", i);
            startActivity(intent);
            finish();
        }


    }
    public  void annuler(View args){
        Intent intent = new Intent(Initialisation_1.this,acceiul_activity.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();
    }


}
