package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by User02 on 03/03/2016.
 */
public class acceiul_activity extends Activity implements LocationListener {

    Button initournee, livraison, stock, CloturerTournee, btndeconnexion;

    DatabaseManager obj;
    private LocationManager locationManager;
    EditText username;
    int id_tournee;
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulayout);

        obj = new DatabaseManager(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, this);

        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        final String n = b.getString("loginselectionne");
        final int id_agent = b.getInt("id_agent");
        obj.open();
        Cursor c = obj.getIdTournee(id_agent);
        id_tournee = 0;
        if (c.moveToFirst()) {
            id_tournee = c.getInt(0);
        }


        loginDisplay.setText(n);


        boolean etat_tournee = obj.getEtatTournee(id_tournee);

        obj.close();


         /* on recupere le bouton initournee  a partir de son identifiant*/
        initournee = (Button) findViewById(R.id.initournee);
        if (etat_tournee) {
            initournee.setEnabled(false);
            initournee.setClickable(false);
            initournee.setBackground(getResources().getDrawable(R.drawable.tournnee_jour_off_btn_accueil));
        } else {
            initournee.setEnabled(true);
            initournee.setClickable(true);
            initournee.setBackground(getResources().getDrawable(R.drawable.tournnee_jour_btn_accueil));

        }
        /* on ajoute un listener sur le boutton afin de realiser une action lorsqu'on clique dessus*/
        initournee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(acceiul_activity.this, Initialisation_1.class);
                intent.putExtra("loginselectionne", n);
                intent.putExtra("id_agent", id_agent);

                startActivity(intent);
            }
        });

        livraison = (Button) findViewById(R.id.livraison);

        if (etat_tournee) {
            livraison.setEnabled(true);
            livraison.setClickable(true);
            livraison.setBackground(getResources().getDrawable(R.drawable.livraison_btn_accueil));
        } else {
            livraison.setEnabled(false);
            livraison.setClickable(false);
            livraison.setBackground(getResources().getDrawable(R.drawable.livraison_off_btn_accueil));
        }

        livraison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(acceiul_activity.this, livraison_activity.class);
                intent2.putExtra("loginselectionne", n);
                intent2.putExtra("id_agent", id_agent);
                startActivity(intent2);
            }
        });

        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(acceiul_activity.this, MainActivity.class);
                startActivity(intent3);

            }
        });

        stock = (Button) findViewById(R.id.stock);
        if (etat_tournee) {
            stock.setEnabled(true);
            stock.setClickable(true);
            stock.setBackground(getResources().getDrawable(R.drawable.visualisation_du_stock_btn_accueil));
        } else {
            stock.setEnabled(false);
            stock.setClickable(false);
            stock.setBackground(getResources().getDrawable(R.drawable.visualisation_du_stock_off_btn_accueil));
        }
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(acceiul_activity.this, stock_activity.class);
                intent4.putExtra("loginselectionne", n);
                intent4.putExtra("id_agent", id_agent);
                startActivity(intent4);

            }
        });
        CloturerTournee = (Button) findViewById(R.id.CloturerTournee);
        if (etat_tournee) {
            CloturerTournee.setEnabled(true);
            CloturerTournee.setClickable(true);
            CloturerTournee.setBackground(getResources().getDrawable(R.drawable.cloture_btn_accueil));
        } else {
            CloturerTournee.setEnabled(false);
            CloturerTournee.setClickable(false);
            CloturerTournee.setBackground(getResources().getDrawable(R.drawable.cloture_off_btn_accueil));
        }
        CloturerTournee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(acceiul_activity.this, Cloture1.class);
                intent5.putExtra("loginselectionne", n);
                intent5.putExtra("id_agent", id_agent);
                startActivity(intent5);

            }
        });

        synchro();
    }

    @Override
    public void onLocationChanged(Location location) {

        String str = "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude();

       // Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
        obj.open();
        obj.updateGPSEmployeConecte(location.getLatitude(), location.getLongitude());
        obj.close();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        /******** Called when User on Gps  *********/

        Toast.makeText(getBaseContext(), "GPS allumé", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String s) {

        /******** Called when User off Gps *********/

        Toast.makeText(getBaseContext(), "Veuillez allumer votre GPS", Toast.LENGTH_SHORT).show();
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void localisation() {
        String url = DatabaseManager.KEY_URL;
        String user = DatabaseManager.KEY_USER;
        String pass = DatabaseManager.KEY_PASSWORD;
        obj.open();
        Cursor c;
        c = obj.getEmployeConnecteGPS();
        if (c.moveToFirst()) {
            do {

                String requete_select = "SELECT * FROM localisation WHERE employeID=" + c.getInt(c.getColumnIndex("employeID")) + "";

                String requete_insert = "INSERT INTO localisation VALUES (null,'" + c.getString(c.getColumnIndex("latitude")) + "' ,'" + c.getString(c.getColumnIndex("longitude")) + "','" + c.getString(c.getColumnIndex("nom_connecte")) + "'," + c.getInt(c.getColumnIndex("employeID")) + ",'" + getTodaysDate() + "','" + getTodaysDate() + "')";

                String requete_update = "UPDATE localisation SET latitude=" + c.getString(c.getColumnIndex("latitude")) + " ,longitude=" + c.getString(c.getColumnIndex("longitude")) + " ,updated_at='" + getTodaysDate() + "' WHERE employeID=" + c.getInt(c.getColumnIndex("employeID"));
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd = DriverManager.getConnection(url, user, pass);
                    Statement st1 = con_cmd.createStatement();
                    ResultSet resultat_select = st1.executeQuery(requete_select);

                    Log.d("localisationIDSELECT", requete_select);


                    if (resultat_select.first()) {
                        Log.d("localisationIDS", requete_update);
                        Log.d("localisationIDS", requete_update);
                        st1.executeUpdate(requete_update);
                    } else {
                        Log.d("localisationIDS", requete_insert);
                        st1.executeUpdate(requete_insert);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }
        c.close();
        obj.close();

    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();

        String todaysDate =
                String.valueOf(
                        (c.get(Calendar.YEAR)) + "-" +
                                (c.get(Calendar.MONTH) + 1)) + "-" +
                        (c.get(Calendar.DAY_OF_MONTH)) + "T" +
                        (c.get(Calendar.HOUR_OF_DAY)) + ":" +
                        (c.get(Calendar.MINUTE)) + ":" +
                        (c.get(Calendar.SECOND)
                        );

        return (String.valueOf(todaysDate));

    }


    public class Do_Synchonistion extends AsyncTask<String, String, String> {


        public Do_Synchonistion() {


        }


        protected void onPreExecute() {


        }


        protected String doInBackground(String... arg0) {
            synchronisation_mobile_serveur();
            localisation();
            return "ok";
        }


        protected void onPostExecute(String strFromDoInBg) {


        }

    }




    public void synchro()
    {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        // String lien = null;

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        new Do_Synchonistion().execute();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000); //execute in every 2 min
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public  void synchronisation_mobile_serveur(){

        DatabaseManager obj = new DatabaseManager(getApplicationContext());
        obj.open();


        Cursor c4 = obj.getInfoTournee(id_tournee);
        //Toast.makeText(Cloturer_Tournee.this, "Nombre de ligne " + c4.getCount(), Toast.LENGTH_LONG).show();
        if (c4.moveToFirst()) {
            do {
                String sql3 = "update tournee " +
                        "SET fondInitialrecu="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE))+" " +
                        ", KMDebut ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))+" " +
                        ", statut='"+c4.getString(c4.getColumnIndex(DatabaseManager.KEY_STATUT_TOURNEE))+"'"+" " +
                        ", KMFin= "+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_FIN_TOURNEE))+" " +
                        ", Datefin = '"+c4.getString(c4.getColumnIndex(DatabaseManager.KEY_DATE_FIN_TOURNEE))+"' " +
                        " WHERE id ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE))+"";


                Log.d("synchro_Tournee","req = "+ sql3);

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


        /*

        String sql="update tournee " +
                "SET KMFin  = "+KMF+", Datefin='"+DateFinT+"' , statut='Terminée' " +
                "WHERE id="+j+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

*/
        Cursor c55 = obj.getAllLigne(DatabaseManager.TABLE_COMMANDE_CLIENT);
        if(c55.moveToFirst()){

            do{

                String sq2="update livraison " +
                        "SET statut  = '"+c55.getString(c55.getColumnIndex(DatabaseManager.KEY_STATUT_COMMANDE))+"' " +
                        "WHERE id="+c55.getString(c55.getColumnIndex(DatabaseManager.KEY_ID_LIVRAISON_COMMANDE))+"";
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

            }while (c55.moveToNext());




        }




/*

        c4 = DB.getInfoTournee(j);
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
     */


        Cursor c=obj.getEtatCommande();
        if(c.moveToFirst()) {
            do {
                String sql2="update commandeclient " +
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
                    st1.executeUpdate(sql2);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("commandeLivree","Vide:");
        }
        c.close();

        c=obj.getLigneCmdLivree();
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql3="update lignecommande " +
                        "SET quantiteLivree="+c.getInt(c.getColumnIndex("quantiteLivree"))+" " +
                        ", quantite_livree_reel ="+c.getInt(c.getColumnIndex("quantite_livree_reel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idCommande = "+c.getInt(c.getColumnIndex("idCommande"));
                Log.d("qsdfg","test : "+sql3);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql3);
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


        c=obj.getAllLigne(DatabaseManager.TABLE_ETAPE_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql4="update etapetournee " +
                        "SET statut='"+c.getString(c.getColumnIndex("statut"))+"' " +
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"));
                Log.d("qsdfg","test : "+sql4);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql4);
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

        c=obj.getAllLigne(DatabaseManager.TABLE_STOCK_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql5="update tracestocktournee " +
                        "SET stockinitialReel="+c.getInt(c.getColumnIndex("stockinitialReel"))+", " +
                        " stockEncours="+c.getInt(c.getColumnIndex("stockEncours"))+", " +
                        " stockFinalReel="+c.getInt(c.getColumnIndex("stockFinalReel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idTournee="+c.getInt(c.getColumnIndex("idTournee"));
                Log.d("qsdfg","test : "+sql5);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql5);
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

        obj.close();
    }




}


