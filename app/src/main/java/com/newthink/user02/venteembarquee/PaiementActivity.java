package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Salim on 16/10/2016.
 */
public class PaiementActivity extends Activity {

    public DatabaseManager DB= new DatabaseManager(this);
    ArrayList<Paiement> myList = new ArrayList<Paiement>();
    ListView listPaiement;
    Context context = PaiementActivity.this;
    Button btnAcceuil;
    Button btndeconnexion;
    int idAgent=0;
    int idTournee=0;
    public static String loginSelectionne;




    //    public void onCreate(Bundle savedInstanceState) {

    public void  onCreate(Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiement_layout);
        listPaiement = (ListView)findViewById(R.id.listviewpaiement);
        /*affichage de l'utilisateur connecté */
        /*****************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        loginSelectionne = b.getString("loginselectionne");
        idAgent = b.getInt("id_agent");
        Log.d("stststs", "id = " + idAgent);
        loginDisplay.setText(loginSelectionne);
        /***************/
        /*cette methode  sert a afficher un message lors du clique sur un element d'une liste*/
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaiementActivity.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",loginSelectionne);
                intent.putExtra("id_agent",idAgent);
                startActivity(intent);
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(PaiementActivity.this, MainActivity.class);
                intent3.putExtra("loginselectionne", loginSelectionne);
                startActivity(intent3);
            }
        });
        DB.open();
        ///////////////////////////////////////ICI ///////////////////////////////////////////////
        Cursor c2=DB.getIdTournee(idAgent);
        if(c2.moveToLast()){
            idTournee = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
        }
        refreshListLivraison();
        DB.close();
        //registerClickCallback();

    }


    public void refreshListLivraison() {

        myList.clear();
        //Cursor c = DB.getLivraison(kk,ss);
        Cursor cursor = DB.getPaiement(idAgent, idTournee);
        if (cursor.moveToFirst()) {
            do {

                int idPaiement = cursor.getInt(0);
                int idTournee = cursor.getInt(1);
                int idCommande = cursor.getInt(2);
                int idFacture = cursor.getInt(3);
                int idClient = cursor.getInt(4);
                int idAdresse = cursor.getInt(5);
                int idTransporteur = cursor.getInt(6);
                String typeEncaissement = cursor.getString(7);
                String modeDePaiement = cursor.getString(8);
                String datePaiementPrevue = cursor.getString(9);
                String datePaiementReele = cursor.getString(10);
                String statut = cursor.getString(11);
                float montantAPayer = cursor.getFloat(12);
                float montantEncaisse = cursor.getFloat(13);


                Log.d("reqrest","result : idcommande : "+idCommande );

                getPaiementInList(myList, idPaiement, idTournee, idCommande, idFacture, idClient, idAdresse, idTransporteur, typeEncaissement, modeDePaiement, datePaiementPrevue, datePaiementReele, statut, montantAPayer, montantEncaisse);
            }
            while(cursor.moveToNext());
            cursor.close();
        }
        //listPaiement.setAdapter(new MyBaseAdapterLivraison(context, myList));
    }

    private void getPaiementInList(ArrayList<Paiement> li, int idPaiement,int idTournee, int idCommande, int idFacture, int idClient, int idAdresse, int idTransporteur, String typeEncaissement, String modeDePaiement, String datePaiementPrevue, String datePaiementReele, String statut, float montantAPayer, float montantEncaisse){
        Paiement paiement = new Paiement();
        paiement.idPaiement = idPaiement;
        paiement.idTournee = idTournee;
        paiement.idCommande = idCommande;
        paiement.idFacture = idFacture;
        paiement.idClient = idClient;
        paiement.idAdresse = idAdresse;
        paiement.idTransporteur = idTransporteur;
        paiement.typeEncaissement = typeEncaissement;
        paiement.modeDePaiement = modeDePaiement;
        paiement.datePaiementPrevue = datePaiementPrevue;
        paiement.datePaiementReel = datePaiementReele;
        paiement.statut = statut;
        paiement.montantAPayer = montantAPayer;
        paiement.montantEncaisse = montantEncaisse;

        li.add(paiement);

    }


/*

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

                            Intent intent = new Intent(PaiementActivity.this, planifier_livraison_activity.class);
                            intent.putExtra("loginselectionne", loginSelectionne);
                            intent.putExtra("id_agent", idAgent);
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
    */


}
