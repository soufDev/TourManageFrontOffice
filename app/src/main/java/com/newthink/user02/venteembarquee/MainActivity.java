package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity  implements View.OnClickListener {

    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    Button BtnConnexion;
    EditText username, password;
    int id_agent;
    DatabaseManager DB= new DatabaseManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        BtnConnexion = (Button) findViewById(R.id.BtnConnexion);
    }
    @Override
    public void onClick(View v) {
        DatabaseManager DB= new DatabaseManager(this);
        String EXTRA_LOGIN= username.getText().toString();
        // String hashed = md5(password.getText().toString());
        String EXTRA_PASS =md5(password.getText().toString());

        DB.open();
        if (EXTRA_LOGIN.equals("") || EXTRA_PASS.equals("")) {
            Toast.makeText(MainActivity.this,"Vous devez remplir les champs",Toast.LENGTH_SHORT).show();}
        else {
            agent a = new agent();
            a.setLogin(EXTRA_LOGIN);
            a.setPass(EXTRA_PASS);
            boolean i = testDB(EXTRA_LOGIN, EXTRA_PASS);
            if (i == true) {

                DB.open();
                Cursor c2=DB.getIdTournee(id_agent);
                if(c2.moveToFirst()){
                    int idTournee = c2.getInt(0);
                    boolean started = DB.getEtatTournee(idTournee);
                    if(started){
                        Intent intent = new Intent(MainActivity.this, acceiul_activity.class);
                        intent.putExtra("loginselectionne", username.getText().toString());
                        intent.putExtra("id_agent", id_agent);
                        startActivity(intent);
                        DB.close();
                        this.finish();
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, synchronisation_activity.class);
                        intent.putExtra("loginselectionne", username.getText().toString());
                        intent.putExtra("id_agent", id_agent);

                        startActivity(intent);
                        DB.close();
                        this.finish();
                    }

                }
                else{

                    Intent intent = new Intent(MainActivity.this, synchronisation_activity.class);
                    intent.putExtra("loginselectionne", username.getText().toString());
                    intent.putExtra("id_agent", id_agent);

                    startActivity(intent);
                    DB.close();
                    this.finish();

                }



            } else {
                Toast.makeText(MainActivity.this, "login ou mot de pass incorrecte veuillez resseyez", Toast.LENGTH_SHORT).show();
            }

            DB.close();
        }
    }
    public void CloseApp(View v){
        this.finish();
    }

    int m=0;
    String result1,loginn,result2,nom_agent;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public boolean testDB(String login,String Pass){
        agent a = new agent();
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, user, pass);
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select users.identifiant,users.password,users.nom,users.prenom,users.id,transporteur.id as idtransporteur from users users,transporteur transporteur where transporteur.idEmployee=users.id");
            while (rs.next()){
                result1= rs.getString("identifiant");
                result2 = rs.getString("password");
                if(result1.equals(login) && result2.equals(Pass)) {

                    nom_agent=rs.getString("nom")+" "+rs.getString("prenom");
                    id_agent=rs.getInt("idtransporteur");
                    DB.open();
                    boolean existe = DB.verifierEmployeConnecte(id_agent);
                    if(existe==false)
                    {
                        DB.addEmployeConnecte(id_agent,0,0,nom_agent,"connecte");
                    }
                    else
                    {
                        DB.updateEmployeConnecte(id_agent,"connecte");
                    }

                    DB.close();
                    Toast.makeText(MainActivity.this, "" + login, Toast.LENGTH_SHORT).show();
                    m = 1;
                    break;
                }
            }}catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Problème de connexion avec le serveur",Toast.LENGTH_LONG).show();
        }
        if(m==1){
          DB.open();
            a.setLogin(login);
            a.setPass(Pass);
            Cursor cc = DB.getAgents();
            if(cc.moveToFirst()) {
                do {
                    loginn = cc.getString(cc.getColumnIndex(DatabaseManager.EXTRA_LOGIN));
                } while (cc.moveToNext());
            }
            if(login.equals(loginn)) {
               // Toast.makeText(MainActivity.this, "ce login exit deja", Toast.LENGTH_SHORT).show();
            }
            else
            {
                DB.putInformation(a);
            }
            DB.close();
            return true ;
        }
        else{
            return false;
        }
    }

    public  String md5(String s)
    {
        String password = s;

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return (sb.toString());
    }
}
