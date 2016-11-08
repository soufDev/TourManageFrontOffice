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
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by User02 on 09/03/2016.
 */
public class tournee_du_jour_activity extends Activity {

    ArrayList<CommandeClient> myList = new ArrayList<CommandeClient>();
    ArrayList<Etape> myListEtape = new ArrayList<Etape>();
    public DatabaseManager DB = new DatabaseManager(this);
    ListView list;
    Context context = tournee_du_jour_activity.this;
    Button btnAcceuil, btndeconnexion;
    Button btntour;
 public static int i,j;
    public static String n;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //on associe notre activit au fichier xml cree */
        setContentView(R.layout.tournee_du_jour_layout);
        /*L'utilisateur connecte */
        /***************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n = b.getString("loginselectionne");
        i=b.getInt("id_agent");
        loginDisplay.setText(n);
        /****************/
        list=(ListView) findViewById(R.id.listTournee);
        UpdateItem();

        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tournee_du_jour_activity.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",n);
                intent.putExtra("id_agent",i);
                startActivity(intent);
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(tournee_du_jour_activity.this, MainActivity.class);
                intent3.putExtra("loginselectionne",n);
                intent3.putExtra("id_agent",i);
                startActivity(intent3);
            }
        });
        DB.open();
        //Cursor c = DB.getIdAgent(n);
       // if(c.moveToFirst()){
            //i = c.getInt(c.getColumnIndex(DatabaseManager.AGENT_KEY));
      //  }

        Cursor c2=DB.getIdTournee(i);
        if(c2.moveToLast()){
            j = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));

        }
        myList.clear();
        refreshListTournee();
        DB.close();
    }
   public void refreshListTournee() {
       myListEtape.clear();

        Cursor c344 = DB.getTournee2(i, j);
       if(c344.moveToFirst()){
           Log.d("tailleListe","requete Not Null ");
       }
       else{
           Log.d("tailleListe","requete  Null ");
       }
       /*
        if (c344.moveToFirst()) {
            do {
                btntour= (Button)findViewById(R.id.btntour);
                int id = c344.getInt(0);
                String m = c344.getString(1);
                String n = c344.getString(2);
                int b= c344.getInt(3);
                int v = c344.getInt(4);

                getDataInList(myList, id, m, n, b, v);
            }
            while(c344.moveToNext());
            c344.close();
        }
        */

       if (c344.moveToFirst()) {
           do {
              // btntour= (Button)findViewById(R.id.btntour);
               int idEtape = c344.getInt(0);
               int idCommande= c344.getInt(1);
               int idLivraison= c344.getInt(2);
               String nom = c344.getString(3);
               String prenom = c344.getString(4);
               String adresse = c344.getString(5);
               String motif = c344.getString(6);
               String statut = c344.getString(7);
               getEtapeInList(myListEtape,idEtape,idCommande,idLivraison,nom,prenom,adresse,motif,statut);
               //getDataInList(myList, id, m, n, b, v);
           }
           while(c344.moveToNext());
           c344.close();
       }
       if(myListEtape.size()>0){
           Log.d("tailleListe","size = "+myListEtape.size() );
       }
       else{
           Log.d("tailleListe","size = 0" );
       }
       list.setAdapter(new MyBaseAdapterTourneeJour2(context, myListEtape));

       //list.setAdapter(new MyBaseAdapterTourneeJour(context, myList));
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

    private void getDataInList(ArrayList<CommandeClient> li, int p, String a, String b,int c,int d) {
        // Create a new object for each list item
        CommandeClient ld = new CommandeClient();
        ld.setIdClient(p);
        ld.setNomClient(a);
        ld.setMotifVisite(b);
        ld.setIdCommande(c);
        ld.setIdLivraison(d);

        // Add this object into the ArrayList myList
        li.add(ld);
    }


     public void onClickbtnTournee(View v) {
         //list=(ListView) findViewById(R.id.listTournee);
         View parentRow = (View) v.getParent();
         parentRow.getParent();
         String kk = null;
         final int position = list.getPositionForView(parentRow);
         CommandeClient a = myList.get(position);
         final int cmd = a.getIdCommande();
         final int liv = a.getIdLivraison();
         final String client= a.getNomClient();
         DB.open();
         Cursor c2= DB.getDateLivraisonTournee(liv);
         if(c2.moveToFirst()){
             kk = c2.getString(c2.getColumnIndex(DatabaseManager.KEY_DATE_LIVRAISON_PREVUE));
         }
         final String Date=kk;
         DB.close();
         final boolean condition = true;
         Intent intent = new Intent(tournee_du_jour_activity.this, planifier_livraison_activity.class);
         intent.putExtra("loginselectionne",n);
         intent.putExtra("id_agent",i);
         intent.putExtra("Client1",client);
         intent.putExtra("Commande1",cmd);
         intent.putExtra("Livraison1",liv);
         intent.putExtra("DateLivraison1",Date);
         intent.putExtra("Condition1",condition);
         startActivity(intent);
        }


    public void UpdateItem() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                final Etape etape = myListEtape.get(position);
                final int cmd = etape.idCommande;
                final int liv = etape.idLivraison;
                Log.d("IDLIVRAISON","ID_LIVRAISON   : "+liv);
                final String client= etape.nomClient+" "+etape.prenomClient;
                String temp = null;
                DB.open();
                Cursor c2= DB.getDateLivraisonTournee(liv);
                if(c2.moveToFirst()){
                    temp = c2.getString(c2.getColumnIndex(DatabaseManager.KEY_DATE_LIVRAISON_PREVUE));
                }
                String Date =temp;
                DB.close();
                final boolean condition = true;
                Intent intent = new Intent(tournee_du_jour_activity.this, planifier_livraison_activity.class);
                intent.putExtra("loginselectionne",n);
                intent.putExtra("id_agent",i);
                intent.putExtra("Client1", client);
                intent.putExtra("Commande1", cmd);
                intent.putExtra("Livraison1", liv);
                intent.putExtra("DateLivraison1", Date);
                intent.putExtra("Condition1", condition);
                startActivity(intent);
            }
        });

    }

}
