package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Initialisation_2 extends Activity {

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
    ListView list;Context context = Initialisation_2.this;
    public static String n;
    public static int i,j;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation_2);


        /********************************Affichage de l'agent connecté***************************/
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
                Intent intent = new Intent(Initialisation_2.this, acceiul_activity.class);
                intent.putExtra("loginselectionne", n);
                Log.d("idAgentMenu", "id = " + i);
                intent.putExtra("id_agent", i);
                startActivity(intent);
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Initialisation_2.this, MainActivity.class);
                intent3.putExtra("loginselectionne", n);
                intent3.putExtra("id_agent",i);
                startActivity(intent3);
            }
        });

        list=(ListView) findViewById(R.id.listProd);
        UpdateItem();


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

    public void precedent(View args){
        Intent intent = new Intent(Initialisation_2.this, Initialisation_1.class);
        intent.putExtra("loginselectionne", n);
        Log.d("idAgentMenu", "id = " + i);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();

    }

    public  void annuler(View args){
        Intent intent = new Intent(Initialisation_2.this,acceiul_activity.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();
    }

    public void suivant(View args){



        Intent intent = new Intent(Initialisation_2.this,Initialisation_3.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();

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
                LayoutInflater inflater = Initialisation_2.this.getLayoutInflater();
                builder.setTitle("Item");
                builder.setMessage(" Vous pouvez changer la quantité ");
                final View vw = inflater.inflate(R.layout.dialogue_layout, null);
                builder.setView(vw);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stockestimeEditText = (EditText) vw.findViewById(R.id.quantite);
                        final String QUANTITE = stockestimeEditText.getText().toString();
                        int QTE = Integer.parseInt(QUANTITE);
                        Log.d("initio", "QTE a changée" + QTE + " idProduit = " + idp);
                        DB.open();
                        DB.UpdateSTE(QTE, idp);
                        Toast.makeText(context, "quantité changé :" + QTE, Toast.LENGTH_LONG).show();

                        Cursor x = DB.getAllLigne(DB.TABLE_STOCK_TOURNEE);
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



}
