package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User02 on 13/04/2016.
 * */
public class Cloturer_Tournee extends Activity {
    public static final int SIGNATURE_ACTIVITY = 1;
    public static String n;
    ImageButton btnAcceuil,btndeconnexion, btndemarrer;
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
    Context context = Cloturer_Tournee.this;
    /* afficher le nom d'utilisateur connecte */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* on associe notre activite au fichier xml cree */
        setContentView(R.layout.cloturer_tournee);
        /*****************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        i= b.getInt("id_agent");
        loginDisplay.setText(n);
        /****************/
        list=(ListView) findViewById(R.id.ListPrduitClient);
        btnAcceuil = (ImageButton) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cloturer_Tournee.this, acceiul_activity.class);
                intent.putExtra("loginselectionne",n);
                intent.putExtra("id_agent",i);
                startActivity(intent);
            }
        });
        btndeconnexion = (ImageButton) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Cloturer_Tournee.this, MainActivity.class);
                startActivity(intent3);
            }
        });
     /*etapier*/
        tabHost= (TabHost)findViewById(R.id.tabHostC);
        tabHost.setup();
        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Controle cash et vehicule ");
        spec.setContent(R.id.etape11);
        // spec.setContent(R.id.btnsuivant);
        spec.setIndicator("Contrôle cash et véhicule");
        tabHost.addTab(spec);
        //Tab 2
        spec = tabHost.newTabSpec("Inventaire");
        spec.setContent(R.id.etape22);
        spec.setIndicator("Inventaire");
        tabHost.addTab(spec);
        //Tab Da
        spec = tabHost.newTabSpec("Confirmation et signature");
        spec.setContent(R.id.etape33);
        spec.setIndicator("Confirmation et signature");
        tabHost.addTab(spec);
        DB.open();
        /* Recuperer le kilometrage de debut de la tournee */
        //Cursor c = DB.getIdAgent(n);
        //if(c.moveToFirst()){
        //    i = c.getInt(c.getColumnIndex(DatabaseManager.AGENT_KEY));
        //}


        Cursor c2=DB.getIdTournee(i);

        if(c2.moveToFirst()){
            j = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
        }
        Toast.makeText(Cloturer_Tournee.this, "tournee :" +j, Toast.LENGTH_LONG).show();
        kilometragedebut =(EditText) findViewById(R.id.KMDebut);
        Date_Debut = (EditText)findViewById(R.id.DateDebut);
        /* Afficher la date de debut de tournee */
        Cursor c4 = DB.getInfoTour(j);
        c4.moveToFirst();
        if (c4.getCount() > 0) {
            kilometragedebut.setText(String.valueOf(c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))));
            Date_Debut.setText(c4.getString(c4.getColumnIndex(DatabaseManager.KEY_DATE_DEBUT_TORUNEE)));
            cashInit  = c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE));
        } else {
        //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c.getCount(), Toast.LENGTH_LONG).show();
        }
        refreshListInventaire();
        DB.close();
    }

        /*Afficher les donnees dans la table layout dans la 2 eme etape de l'iitialisation de la tournee */

    public void refreshListInventaire () {

        myList.clear();
        Cursor c = DB.geInventaire(j);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                String n = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                int is = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE));
                int vs =c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE));
                getDataInList(myList, id,n,is,vs);
                RapportDecart(id, is, vs, j);
            } while (c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterInvent(context, myList));
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
    public void suivant(View arg) {
    //ajouter les chmaps a la base de données
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
            DB.open();
            agent a = new agent();
            a.setKMFin(KMF);
            DB.saveVehicule(a);
            DB.UpdateCash(Cach,j);
            DB.close();
            tabHost.setCurrentTab(1);
        }
    }
    public  void annuler(View args){
        Intent intent = new Intent(Cloturer_Tournee.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent",i);
        startActivity(intent);
    }
    public void precedent(View args){
        tabHost.setCurrentTab(0);
       // testDBDebut();
    }
    public  void annuler2(View arg){
        Intent intent = new Intent(Cloturer_Tournee.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent",i);
        startActivity(intent);
    }
    public  void annuler3(View args){
        Intent intent = new Intent(Cloturer_Tournee.this,acceiul_activity.class);
        intent.putExtra("loginselectionne",n);
        intent.putExtra("id_agent",i);
        startActivity(intent);
    }
    public void precedent2(View args){
        tabHost.setCurrentTab(1);
    }
    public void suivant2(View args){
        tabHost.setCurrentTab(2);
        int somES=0,som1=0,som2 = 0;
        list=(ListView) findViewById(R.id.ListPrduitClient);
        DB.open();
        Cursor c2 = DB.getEcart(j);
        if (c2.moveToFirst()) {
            do {
                int i = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                int v =c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE));
                som1=som1+i;
                som2=som2+v;
            } while (c2.moveToNext());
            c2.close();
            somES=som2-som1;
        }
        EcartStock = (EditText)findViewById(R.id.EcartStock);
        EcartCaisse =(EditText)findViewById(R.id.EcartCaisse);
        String cashText= cashf.getText().toString();
        int CachF=Integer.parseInt(cashText);
        int Ecart = CachF-cashInit;
        EcartStock.setText(String.valueOf(somES));
        EcartCaisse.setText(String.valueOf(Ecart));
    }

    public void fermer(View args){
        //* enregistrer les données de la fin de la tournee
        DateFin = (EditText)findViewById(R.id.DateFin);
        if (DateFin.getText().toString().trim().length()<=0)
        {
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        }
        else
        {
            String DateFinT = DateFin.getText().toString();
            DB.open();
            agent a = new agent();
            a.setDateFin(DateFinT);
            DB.SaveDateFinTour(a, j);
            DB.UpdaeTournee("Terminé", j);
            DB.close();
            testDBDebut();
            synchronisation_mobile_serveur();
            testDB(j,KMF,DateFinT,"Terminee");
            Toast.makeText(context, "Fin Tournee", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Cloturer_Tournee.this, MainActivity.class);
            startActivity(intent);
        }}
    public void signer(View args){
        Intent intent = new Intent(Cloturer_Tournee.this, CaptureSignature.class);
        startActivityForResult(intent,SIGNATURE_ACTIVITY);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testDB(int i,int KMF,String DateF,String Statut){
        String sql="update tournee " +
                "SET KMFin  = "+KMF+", Datefin='"+DateF+"' , statut='"+Statut+"' " +
                "WHERE id="+i+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }


        String sq2="update livraison " +
                "SET statut  = 'LIVREE' " +
                "WHERE idTournee="+i+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sq2);
        }catch (Exception e){
            e.printStackTrace();
        }

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
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st2=con.createStatement();
            st2.executeUpdate(sql2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testDBDebut() {
        DB.open();
        Cursor c4 = DB.getInfoTournee(j);
        //Toast.makeText(Cloturer_Tournee.this, "Nombre de ligne " + c4.getCount(), Toast.LENGTH_LONG).show();
        if (c4.moveToFirst()) {
            do {
                String sql3 = "update tournee " +
                        "SET fondInitialrecu="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE))+" " +
                        ", KMDebut ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))+" " +
                        " WHERE id ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE))+"";
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_T = DriverManager.getConnection(url, user, pass);
                    Statement st_T = con_T.createStatement();
                    st_T.executeUpdate(sql3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c4.moveToNext());
        }
        //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c4.getCount(), Toast.LENGTH_LONG).show();
        c4.close();
        DB.close();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public  void synchronisation_mobile_serveur(){
        DB.open();
        Cursor c=DB.getEtatCommande();
        if(c.moveToFirst()) {
            do {
                String sql="update commandeclient " +
                        "SET statut='Livree'"+
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"))+"";
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection(url, user, pass);
                    Statement st1=con.createStatement();
                    st1.executeUpdate(sql);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("commandeLivree","Vide:");
        }
        c.close();

        c=DB.getLigneCmdLivree();
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql="update lignecommande " +
                        "SET quantiteLivree="+c.getInt(c.getColumnIndex("quantiteLivree"))+" " +
                        ", quantite_livree_reel ="+c.getInt(c.getColumnIndex("quantite_livree_reel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idCommande = "+c.getInt(c.getColumnIndex("idCommande"));
                Log.d("qsdfg","test : "+sql);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/


        c=DB.getAllLigne(DatabaseManager.TABLE_ETAPE_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql="update etapetournee " +
                        "SET statut='"+c.getString(c.getColumnIndex("statut"))+"' " +
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"));
                Log.d("qsdfg","test : "+sql);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/

        c=DB.getAllLigne(DatabaseManager.TABLE_STOCK_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql="update tracestocktournee " +
                        "SET stockinitialReel="+c.getInt(c.getColumnIndex("stockinitialReel"))+", " +
                        " stockEncours="+c.getInt(c.getColumnIndex("stockEncours"))+", " +
                        " stockFinalReel="+c.getInt(c.getColumnIndex("stockFinalReel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idTournee="+c.getInt(c.getColumnIndex("idTournee"));
                Log.d("qsdfg","test : "+sql);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/

        DB.close();
    }
}