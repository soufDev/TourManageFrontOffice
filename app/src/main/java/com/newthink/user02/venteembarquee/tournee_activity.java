package com.newthink.user02.venteembarquee;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Created by User02 on 03/03/2016.
 */
public class tournee_activity extends Activity  {
    /*Connexion avec MYSQL */
    /*Entrer l'URL*/
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
/********************************************/

    public static final int SIGNATURE_ACTIVITY = 1;
    ImageButton btnAcceuil,btndeconnexion, btndemarrer;
    TabHost tabHost;
   // Context context = tournee_activity.this;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometrage,cashRecu, stockestimeEditText;
    ListView list;Context context = tournee_activity.this;
    public static String n;
    public static int i,j;

    /* afficher le nom d'utilisateur connecte */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*on associe notre activite au fichier xml cree*/
        setContentView(R.layout.tournee_llayout);
        /********************************Affichage de l'agent connecté***************************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        final Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        i=b.getInt("id_agent");
        loginDisplay.setText(n);

        /********************************Le header****************************************/
        btnAcceuil = (ImageButton) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tournee_activity.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",n);
                Log.d("idAgentMenu","id = "+i);
                intent.putExtra("id_agent",i);
                startActivity(intent);
            }
        });
        btndeconnexion = (ImageButton) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(tournee_activity.this, MainActivity.class);
                intent3.putExtra("loginselectionne",n);
                intent3.putExtra("id_agent",i);
                startActivity(intent3);
            }
        });
        /*********************************************************************************/


        /***************etapier*********************/
        tabHost= (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Contrôle cash et vehicule");
        spec.setContent(R.id.etape1);
        spec.setIndicator("Contrôle cash et véhicule");
        tabHost.addTab(spec);
       //Tab 2
        spec = tabHost.newTabSpec("Contrôle du stock");
        spec.setContent(R.id.etape2);
        list=(ListView) findViewById(R.id.listProd);
        UpdateItem();
        spec.setIndicator("Contrôle du stock");
        tabHost.addTab(spec);
        //Tab 3
        spec = tabHost.newTabSpec("Confirmation et signature");
        spec.setContent(R.id.etape3);
        spec.setIndicator("Confirmation et signature");
        tabHost.addTab(spec);
        DB.open();

        /* Recuperer l'id de l'agent connecté et aussi l'id de la tournee affecté a cet agent*/
        Cursor c = DB.getIdAgent(n);
        if(c.moveToFirst()){
            //i = c.getInt(c.getColumnIndex(DatabaseManager.AGENT_KEY));
            Log.d("MYLOG","idAgent = "+i);
        }

        Cursor c2=DB.getIdTournee(i);
        if(c2.moveToFirst()){
            j = c2.getInt(0);
            Log.d("MYLOG","idLivraison = "+j);
        }
        else{
            Log.d("MYLOG","idLivraison = last null");
        }
        refreshList();
        DB.close();
     }

        /*Afficher les donnees dans la table layout dans la 2 eme etape de l'initialisation de la tournee */
    public void refreshList() {
        myList.clear();


        Cursor c = DB.getProd(j);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                String n = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                int i = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                int v =c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE));
                getDataInList(myList, id,n,i,v);
            } while (c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterProduit(context, myList));
    }
    private void getDataInList(ArrayList<Produit> li, int p, String a, int c,int b) {
        // Create a new object for each list item
        Produit ld = new Produit();
        ld.setIdProduit(p);
        ld.setDesignation(a);
        ld.setStockInitialeReel(c);
        ld.setStockInitialeEstime(b);
        // Add this object into the ArrayList myList
        li.add(ld);
    }

    /* Enregistrer les information qui ont été saisi*/
    public  void SaveInfo(int l){

        kilometrage =(EditText) findViewById(R.id.KMFin);
        cashRecu = (EditText)findViewById(R.id.CashRecu);
        String cashText= cashRecu.getText().toString();
        String kilomtext= kilometrage.getText().toString();
        int KMDVar=Integer.parseInt(kilomtext);
        int Cach=Integer.parseInt(cashText);
        Toast.makeText(context,""+KMDVar,Toast.LENGTH_LONG).show();
        Toast.makeText(context,""+Cach,Toast.LENGTH_LONG).show();
        DB.open();
        agent a = new agent();
        a.setCachRecu(Cach);
        a.setKMDebut(KMDVar);
       // DB.saveCach(a, i);
        DB.SaveInitTournee(KMDVar, Cach, j);



        DB.close();

}

    public void suivant(View arg) {
        SaveInfo(j);
        //testDB();
            tabHost.setCurrentTab(1);
    }
    public  void annuler(View args){
        Intent intent = new Intent(tournee_activity.this,acceiul_activity.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
    }
    public void precedent(View args){
        tabHost.setCurrentTab(0);
          }
    public  void annuler2(View arg){
        Intent intent = new Intent(tournee_activity.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
    }
    public  void annuler3(View args){
        Intent intent = new Intent(tournee_activity.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
    }
    public void precedent2(View args){
        tabHost.setCurrentTab(1);
    }
    public void suivant2(View args){
        tabHost.setCurrentTab(2);
    }
    public void demarrer(View args){

        DB.open();
        DB.UpdateEtatTournee("DEMARREE",j);
        DB.close();

        Intent intent2 = new Intent(tournee_activity.this,tournee_du_jour_activity.class);
        intent2.putExtra("loginselectionne",n);
        intent2.putExtra("id_agent",i);
        startActivity(intent2);
    }
    public void signer(View args){
        Intent intent = new Intent(tournee_activity.this, CaptureSignature.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent", i);
        startActivityForResult(intent, SIGNATURE_ACTIVITY);
    }
    public void UpdateItem() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                final Produit a = myList.get(position);
                final int si = a.getStockInitialeEstime();
                final int sr = a.getStockInitialeReel();
                final int idp = a.getIdProduit();
                if (si != sr) {
                    double motifdecart = sr - si;
                    Toast.makeText(context, "le motif d'ecart :" + motifdecart, Toast.LENGTH_LONG).show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = tournee_activity.this.getLayoutInflater();
                builder.setTitle("Item");
                builder.setMessage(" Vous pouvez changer la quantité ");
                final View vw = inflater.inflate(R.layout.dialogue_layout, null);
                builder.setView(vw);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stockestimeEditText = (EditText) vw.findViewById(R.id.quantite);
                        final String QUANTITE = stockestimeEditText.getText().toString();
                        int QTE = Integer.parseInt(QUANTITE);
                        Log.d("initio","QTE a changée"+ QTE +" idProduit = "+idp);
                        DB.open();
                        DB.UpdateSTE(QTE, idp);
                        Toast.makeText(context, "quantité changé :" + QTE, Toast.LENGTH_LONG).show();

                        Cursor x=DB.getAllLigne(DB.TABLE_STOCK_TOURNEE);
                        x.moveToFirst();
                        Log.d("initio", "idTrace = " + x.getString(0) + " QTE = " + x.getString(3) + " QTR = " + x.getString(4));

                        refreshList();
                        DB.close();
                    }
                });
                builder.setNegativeButton("Non", null);
                builder.show();
            }
        });

    }

    //***************************************Synchoniser les données du Sqlite vers MYSQl *************************/
    }