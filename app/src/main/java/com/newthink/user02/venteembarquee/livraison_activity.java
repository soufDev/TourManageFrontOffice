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
import java.util.Random;

/**
 * Created by User02 on 09/03/2016.
 */
public class livraison_activity extends Activity  {

    ArrayList<Etape> myList = new ArrayList<Etape>();
    public DatabaseManager DB = new DatabaseManager(this);
    ListView list;
    Context context = livraison_activity.this;
    Button btnAcceuil;
    Button btndeconnexion;
    int ss=0,kk=0;
     public static String n;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livraison_layout);
        list=(ListView) findViewById(R.id.listLiv);
        /*affichage de l'utilisateur connecté */
        /*****************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
         n = b.getString("loginselectionne");
        ss = b.getInt("id_agent");
        Log.d("stststs", "id = " + ss);
        loginDisplay.setText(n);
        /***************/
        /*cette methode  sert a afficher un message lors du clique sur un element d'une liste*/

       btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(livraison_activity.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",n);
                intent.putExtra("id_agent",ss);
                startActivity(intent);
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(livraison_activity.this, MainActivity.class);
                intent3.putExtra("loginselectionne", n);
                startActivity(intent3);
            }
        });
        DB.open();
        //Cursor c3 = DB.getIdAgent(n);
      //  if(c3.moveToFirst()){
        //    ss = c3.getInt(c3.getColumnIndex(DatabaseManager.AGENT_KEY));
       // }
        Cursor c2=DB.getIdTournee(ss);
        if(c2.moveToLast()){
            kk = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
        }
        refreshListLivraison();
        DB.close();
        registerClickCallback();
    }
   public void refreshListLivraison() {

       myList.clear();
         //Cursor c = DB.getLivraison(kk,ss);
       Cursor c = DB.getLivraison2(ss, kk);
        if (c.moveToFirst()) {
            do {
                int idEtape = c.getInt(0);
                int idCommande= c.getInt(1);
                int idLivraison= c.getInt(2);
                String nom = c.getString(3);
                String prenom = c.getString(4);
                String adresse = c.getString(5);
                String motif = c.getString(6);
                String statut = c.getString(7);

                Log.d("reqrest","result : idcommande : "+idCommande );

                getEtapeInList(myList, idEtape, idCommande, idLivraison, nom, prenom, adresse, motif, statut);
            }
            while(c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterLivraison(context, myList));
    }


    private void getEtapeInList(ArrayList<Etape> li, int idEtape, int idCommande, int idLivraison, String nomClient, String prenomClient, String adresse, String motif, String statut) {
        // Create a new object for each list item
        Etape etape=new Etape();
        etape.idEtape=idEtape;
        etape.idCommande= idCommande;
        etape.idLivraison=idLivraison;
        etape.nomClient=nomClient;
        etape.prenomClient=prenomClient;
        etape.motifVisite=motif;
        etape.statutEtape=statut;
        etape.adresseClient=adresse;

        // Add this object into the ArrayList myList
        li.add(etape);
    }


    private void getDataInList(ArrayList<CommandeClient> li, int p, String a,String b) {
        // Create a new object for each list item
        CommandeClient ld = new CommandeClient();
        ld.setIdLivraison(p);
        ld.setNomClient(a);
        ld.setStatut(b);
        // Add this object into the ArrayList myList
        li.add(ld);
    }




    private void registerClickCallback() {

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                int Com = 0;
                String DD = null;
                Etape a = myList.get(position);
                final String etat = a.statutEtape;
                final int liv = a.idLivraison;
                final String client =a.nomClient+" "+a.prenomClient;
                //Toast.makeText(context, "le client:" +client, Toast.LENGTH_LONG).show();
                 DB.open();
                Cursor c22= DB.getDateLivraisonTournee(liv);
                if(c22.moveToFirst()){
                    DD = c22.getString(c22.getColumnIndex(DatabaseManager.KEY_DATE_LIVRAISON_PREVUE));
                    Toast.makeText(context, "la date de liv :" + DD, Toast.LENGTH_LONG).show();
                }
                //Cursor c2=DB.getCommandeLiv(liv,client);
               // if(c2.moveToFirst()){
                  //  Com = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_COMMANDE));
                    //Toast.makeText(context, "le num de com :" + Com, Toast.LENGTH_LONG).show();
              //  }
                DB.close();

                final boolean condition = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Item");
                if (etat.equals("EN COURS")) {

                    builder.setMessage(" Voulez vous effectuer cette livraison ? ");
                    final int finalCom = a.idCommande;
                    final String finalDD = DD;
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(livraison_activity.this, planifier_livraison_activity.class);
                            intent.putExtra("loginselectionne", n);
                            intent.putExtra("id_agent", ss);
                            intent.putExtra("Client2", client);
                            intent.putExtra("Commande2", finalCom);
                            intent.putExtra("Livraison2", liv);
                            intent.putExtra("DateLivraison2", finalDD);
                            intent.putExtra("Condition2", condition);
                            startActivity(intent);
                        }

                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                }
                else
                {
                    builder.setMessage(" Vous avez déjà effectué cette livraison ");
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                }
                builder.show();
            }
        });
    }
}
