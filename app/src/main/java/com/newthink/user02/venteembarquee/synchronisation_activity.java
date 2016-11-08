package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Created by User02 on 22/03/2016.
 */
public class synchronisation_activity extends Activity{


    DatabaseManager DB= new DatabaseManager(this);
    ImageButton btnsynchronisation;
    Context context = synchronisation_activity.this;
    /*Connexion avec MYSQL */
    /*Entrer l'URL*/
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
     public static String a;
    public static int id_agent;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.synchronisation_layout);


        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        a = b.getString("loginselectionne");
        id_agent = b.getInt("id_agent");



        new Do_Synchonistion().execute();

       // clearData();
       // testDB();
       // Intent intent2 = new Intent(synchronisation_activity.this, acceiul_activity.class);
       // intent2.putExtra("loginselectionne", a);
       // intent2.putExtra("id_agent", id_agent);
       // startActivity(intent2);


    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testDB(){
        int result1,result3,result6,result7,result9,result11,result12,result13,
                result14,result21,resultt,resulttt,result16,result17,result18,result19,result20,idA,idV,idT,result44,result15a,result45,r,r1;
        int result22,result33,result111;
        String result2,result5,result4,result8,result10,result15,DateD,resultatStatutEtape;
        int rslt,reslt;
        try {
            DB.open();
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");




            // Récupération des données de la table Client - Serveur
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from "+DatabaseManager.TABLE_CLIENT);
            con.close();

            //Remplissage Table Client - local
            while (rs.next()){
                CommandeClient c = new CommandeClient();
                int idClient = rs.getInt(DatabaseManager.KEY_ID_CLIENT);
                String nomClient = rs.getString(DatabaseManager.KEY_NOM_CLIENT);
                String prenomClient = rs.getString(DatabaseManager.KEY_PRENOM_CLIENT);
                String telephone=rs.getString(DatabaseManager.KEY_TELEPHONE_CLIENT);
                c.setIdClient(idClient);
                c.setNomClient(nomClient);
                c.setPrenomClient(prenomClient);
                c.setTelephoneClient(telephone);
                Log.d("Mon_Log", "Liste Client : idClient = " + c.getIdClient() + " NomClient = " + c.getNomClient() + " PrenomClient = " + c.getPrenomClient() + " TelephoneClient = " + c.getTelephoneClient());
                DB.saveClient(c);
            }





            // Récupération des données de la table Produit - Serveur
            Connection con1=DriverManager.getConnection(url, user, pass);
            Statement st1 = con1.createStatement();
            ResultSet rs1=st1.executeQuery("select * from "+DatabaseManager.TABLE_PRODUIT);
            con1.close();

            //Remplissage Table Produit - local
            while(rs1.next()){
                Produit p = new Produit();
                int idProduit =  rs1.getInt(DatabaseManager.KEY_ID_PRODUIT);
                String designationProduit = rs1.getString(DatabaseManager.KEY_DESIGNATION_PRODUIT);
                p.setIdProduit(idProduit);
                p.setDesignation(designationProduit);
             //   Log.d("Mon_Log","Liste Produit : idProduit = "+p.getIdProduit()+" Désignation = "+p.getDesignation());
                DB.putInformationProduit(p);

            }





            String req= "Select * from tournee " +
                    " where idTransporteur =  " +id_agent+
                    " and DateDebut='"+DatabaseManager.getTodaysDate()+"'" +
                    " and statut='EN COURS'";
            Log.d("tournerreq","requete = "+req);

            // Récupération des données  des tournée - Serveur
            Connection con2=DriverManager.getConnection(url, user, pass);
            Statement st2=con2.createStatement();
            ResultSet rs2 = st2.executeQuery("Select * from tournee " +
                    " where idTransporteur =  " +id_agent+
                    " and DateDebut='"+DatabaseManager.getTodaysDate()+"'" +
                    " and statut='EN COURS'");
            con2.close();




            //Remplissage Table Tournée - local
             while(rs2.next()){
                agent a = new agent();
                int idTournee = rs2.getInt(DatabaseManager.KEY_ID_TOURNEE);
                int idAgent=rs2.getInt(DatabaseManager.KEY_ID_AGENT_TOURNEE);
                String dateDebutTournee=rs2.getString(DatabaseManager.KEY_DATE_DEBUT_TORUNEE);
                 String dateFinTournee=rs2.getString(DatabaseManager.KEY_DATE_FIN_TOURNEE);
                int idVehiculeTournee=rs2.getInt(DatabaseManager.KEY_ID_VEHICULE_TOURNEE);
                 String statut=rs2.getString(DatabaseManager.KEY_STATUT_TOURNEE);
                a.setIdTournee(idTournee);
                a.setId(idAgent);
                a.setIdVehicule(idVehiculeTournee);
                a.setDateDebut(dateDebutTournee);
                 a.setDateFin(dateFinTournee);
                 a.setStatut(statut);
                Log.d("Mon_Log", "Liste Tournee : idTournee = " + a.getIdTournee() + " idAgent = " + a.getId() + " idVehicule = " + a.getIdVehicule());
                DB.saveTournee(a);
                 Log.d("salim", "apres le recuperation de la table Tournée");
            }



            Cursor x = DB.getAllLigne("tournee");
            if(x.moveToFirst()){
                Log.d("etatTournee","etat = "+x.getString(5));
            }





            // Récupération des données  des traceStockTournee - Serveur
            Connection con3=DriverManager.getConnection(url, user, pass);
            Statement st3 = con3.createStatement();
            ResultSet rs3=st3.executeQuery(" select * from tracestocktournee ");
            con3.close();

            //Remplissage Table TraceStockTournee - local
            while(rs3.next()){
                agent a = new agent();
                Produit p = new Produit();
                int idStockTournee = rs3.getInt(DatabaseManager.KEY_ID_STOCK_TOURNEE);
                int stockInitinalEstime = rs3.getInt(DatabaseManager.KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE);
                int stockInitialReel = rs3.getInt(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE);
                int stockEncours= rs3.getInt(DatabaseManager.KEY_STOCK_EN_COURS_STOCK_TOURNEE);
                int idProduit= rs3.getInt(DatabaseManager.KEY_ID_PRODUIT_STOCK_TOURNEE);
                int idTournee = rs3.getInt(DatabaseManager.KEY_ID_TOURNEE_STOCK_TOURNEE);
                int stockFinalEs=rs3.getInt(DatabaseManager.KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE);
                int stockFinalRe=rs3.getInt(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE);
                p.setIdStock(idStockTournee);
                p.setStockInitialeEstime(stockInitinalEstime);
                p.setStockInitialeReel(stockInitialReel);
                p.setStockEncours(stockEncours);
                p.setIdProduit(idProduit);
                a.setIdTournee(idTournee);
                p.setStockFinalEstime(stockFinalEs);
                p.setStockFinalReel(stockFinalRe);
                DB.putInformationStock(p,a);

            }







            // Récupération des données  des Livraison - Serveur
            Connection con4=DriverManager.getConnection(url, user, pass);
            Statement st4 = con4.createStatement();
            ResultSet rs4= st4.executeQuery("select * from livraison");
            con4.close();

            //Remplissage Table Livraison - local
            while (rs4.next()){
                CommandeClient c = new CommandeClient();
                agent a2 = new agent();
                int idLivraison = rs4.getInt(DatabaseManager.KEY_ID_LIVRAISON);
                String dateLivraison = rs4.getString(DatabaseManager.KEY_DATE_LIVRAISON_PREVUE);
                int idTournee= rs4.getInt(DatabaseManager.KEY_ID_TOURNEE_LIVRAISON);
                int idAgent=rs4.getInt(DatabaseManager.KEY_ID_AGENT_LIVRAISON);
                String statutLivraison = rs4.getString(DatabaseManager.KEY_STATUT_LIVRAISON);
                c.setIdLivraison(idLivraison);
                c.setDateLivraison(dateLivraison);
                c.setEtatLi(statutLivraison);
                a2.setIdTournee(idTournee);
                a2.setId(idAgent);
                Log.d("Mon_Log", "Liste Tournee : idLivraison = " + idLivraison + " idTournee = " + idTournee + " date = " + dateLivraison);
                DB.saveLivraison(c,a2);
            }






            // Récupération des données  des Commandes - Serveur
            Connection con5=DriverManager.getConnection(url, user, pass);
            Statement st5 = con5.createStatement();
            ResultSet rs5 = st5.executeQuery(" select * from commandeclient ");
            con5.close();

            //Remplissage Table Commandes - local
            while (rs5.next()){
                CommandeClient c = new CommandeClient();
                result6 = rs5.getInt(DatabaseManager.KEY_ID_COMMANDE);
                result7 = rs5.getInt(DatabaseManager.KEY_ID_LIVRAISON_COMMANDE);
                result8 = rs5.getString(DatabaseManager.KEY_DATE_CREATION_COMMANDE);
                result9 = rs5.getInt(DatabaseManager.KEY_ID_CLIENT_COMMANDE);
                result10 = rs5.getString(DatabaseManager.KEY_STATUT_COMMANDE);
                c.setIdCommande(result6);
                c.setIdLivraison(result7);
                c.setDateCreationC(result8);
                c.setIdClient(result9);
                c.setStatut(result10);
                Log.d("salim", "ecuperation de la table Commandes");
                DB.saveCommande(c);
            }






            // Récupération des données  des Etapes Tournée - Serveur
            Connection con6=DriverManager.getConnection(url, user, pass);
            Statement st6 = con6.createStatement();
            ResultSet rs6 = st6.executeQuery(" select * from etapetournee e, livraison l where e.idLivraison=l.id and l.DateLivraisonPrev='"+DatabaseManager.getTodaysDate()+"'");
            con6.close();

            //Remplissage Table Etapes Tournée - local
//            while (rs6.next()){
//
//                CommandeClient c = new CommandeClient();
//               // Etape c = new Etape();
//                int idEtape = rs6.getInt(DatabaseManager.KEY_ID_ETAPE_TOURNEE);
//                int idLivraison= rs6.getInt(DatabaseManager.KEY_ID_LIVRAISON_ETAPE_TOURNEE);
//                int idCommande= rs6.getInt(DatabaseManager.KEY_ID_COMMANDE_ETAPE_TOURNEE);
//                int idClient = rs6.getInt(DatabaseManager.KEY_ID_CLIENT_ETAPE_TOURNEE);
//                String motifvisit = rs6.getString(DatabaseManager.KEY_MOTIF_VISITE_ETAPE_TOURNEE);
//                int numEtape=rs6.getInt(DatabaseManager.KEY_NUM_ETAPE_TOURNEE);
//                resultatStatutEtape=rs6.getString(DatabaseManager.KEY_STATUT_ETAPE_TOURNEE);
//                c.setIdEtape(idEtape);
//                Log.d("testing", "col 0 =" + c.getIdEtape());
//                c.setIdCommande(idCommande);
//                c.setIdLivraison(idLivraison);
//                c.setIdClient(idClient);
//                c.setMotifVisite(motifvisit);
//                c.setNumEtape(numEtape);
//                DB.saveEtape(c,resultatStatutEtape);
//
//
//            }

            while (rs6.next()){

               // CommandeClient c = new CommandeClient();
                Etape e = new Etape();
                e.idEtape = rs6.getInt(DatabaseManager.KEY_ID_ETAPE_TOURNEE);
                e.idLivraison= rs6.getInt(DatabaseManager.KEY_ID_LIVRAISON_ETAPE_TOURNEE);
                e.idCommande= rs6.getInt(DatabaseManager.KEY_ID_COMMANDE_ETAPE_TOURNEE);
                e.idClient = rs6.getInt(DatabaseManager.KEY_ID_CLIENT_ETAPE_TOURNEE);
                e.motifVisite = rs6.getString(DatabaseManager.KEY_MOTIF_VISITE_ETAPE_TOURNEE);
                e.idTournee = rs6.getInt(DatabaseManager.KEY_ID_TOURNEE_ETAPE_TOURNEE);
                //int numEtape=rs6.getInt(DatabaseManager.KEY_NUM_ETAPE_TOURNEE);
                e.statutEtape=rs6.getString(DatabaseManager.KEY_STATUT_ETAPE_TOURNEE);
                e.adresseClient=rs6.getString(DatabaseManager.KEY_ADRESSE_ETAPE_TOURNEE);

                DB.saveEtape2(e);


            }





            // Récupération des données   Point Livraison - Serveur
            Connection con7 = DriverManager.getConnection(url, user, pass);
            Statement st7 = con7.createStatement();
            ResultSet rs7 = st7.executeQuery(" select * from pointlivraison ");
            con7.close();

            //Remplissage Table Point Livraison - local
            while (rs7.next()) {
                CommandeClient c = new CommandeClient();
                r = rs7.getInt(DatabaseManager.KEY_ID_POINT_LIVRAISON);
                r1 = rs7.getInt(DatabaseManager.KEY_ID_CLIENT_POINT_LIVRAISON);
                c.setIdPointLivr(r);
                c.setIdClient(r1);

                DB.savePoint(c);
            }







            // Récupération des données   lignecommande - Serveur
            Connection con8 = DriverManager.getConnection(url, user, pass);
            Statement st8 = con8.createStatement();
            ResultSet rs8 = st8.executeQuery(" select * from lignecommande ");
            con8.close();

            //Remplissage Table lignecommande - local
            while (rs8.next()) {
                CommandeClient c = new CommandeClient();
                Produit p = new Produit();
                result16 = rs8.getInt("id");
                result17 = rs8.getInt("idProduit");
                result18 = rs8.getInt("idCommande");
                result19 = rs8.getInt("QuantiteCmd");
                result20 = rs8.getInt("quantiteLivree");
                p.setIdLigneCommande(result16);
                p.setIdProduit(result17);
                c.setIdCommande(result18);
                p.setQuantiteCmd(result19);
                p.setQuantiteLivree(result20);

                DB.putInformationLigneCmd(c, p);
            }


            // Récupération des données de la table Paiement - Serveur
            Connection conPaiement = DriverManager.getConnection(url, user, pass);
            Statement stPaiement = conPaiement.createStatement();
            ResultSet rsPaiement = stPaiement.executeQuery("select * from "+DatabaseManager.TABLE_PAIEMENT);
            con1.close();

            //Remplissage Table Produit - local
            while(rsPaiement.next()){
                Paiement paiement = new Paiement();
                int idPaiement =  rsPaiement.getInt(DatabaseManager.KEY_ID_PAIEMENT);
                int idTournee = rsPaiement.getInt((DatabaseManager.KEY_ID_TOURNEE_PAIEMENT));
                int idCommande = rsPaiement.getInt((DatabaseManager.KEY_ID_COMMANDE_PAIEMENT));
                int idFacture = rsPaiement.getInt((DatabaseManager.KEY_ID_FACTURE_PAIEMENT));
                int idClient = rsPaiement.getInt((DatabaseManager.KEY_ID_CLIENT_PAIEMENT));
                int idAdresse = rsPaiement.getInt((DatabaseManager.KEY_ID_ADRESSE_PAIEMENT));
                int idTransporteur = rsPaiement.getInt((DatabaseManager.KEY_ID_TRANSPORTEUR_PAIEMENT));
                String typeEncaissement = rsPaiement.getString((DatabaseManager.KEY_TYPE_ENCAISSEMENT_PAIEMENT));
                String modeDePaiement = rsPaiement.getString((DatabaseManager.KEY_MODE_DE_PAIEMENT_PAIEMENT));
                String dateDePaiementPrevue = rsPaiement.getString((DatabaseManager.KEY_DATE_DE_PAIEMENT_PREVUE_PAIEMENT));
                String dateDePaiementReele = rsPaiement.getString((DatabaseManager.KEY_DATE_DE_PAIEMENT_REELE_PAIEMENT));
                String statut = rsPaiement.getString((DatabaseManager.KEY_STATUT_PAIEMENT));
                float montantAPayer = rsPaiement.getFloat((DatabaseManager.KEY_MONTANT_A_PAYER_PAIEMENT));
                float montantEncaisse = rsPaiement.getFloat((DatabaseManager.KEY_MONTANT_ENCAISSE_PAIEMENT));

                paiement.setIdPaiement(idPaiement);
                paiement.setIdTrounee(idTournee);
                paiement.setIdCommande(idCommande);
                paiement.setIdFacture(idFacture);
                paiement.setIdClient(idClient);
                paiement.setIdAdresse(idAdresse);
                paiement.setIdTransporteur(idTransporteur);
                paiement.setTypeEncaissement(typeEncaissement);
                paiement.setModeDePaiement(modeDePaiement);
                paiement.setDatePaiementPrevue(dateDePaiementPrevue);
                paiement.setDatePaiementReel(dateDePaiementReele);
                paiement.setMontantAPayer(montantAPayer);
                paiement.setStatut(statut);
                paiement.setMontantEncaisse(montantEncaisse);
                Log.d("salim", "ecuperation de la table Paiement ");
                Log.d("Paiement", " "+paiement.idPaiement+" "+paiement.idTransporteur+" "+paiement.idCommande+" "+paiement.idFacture+" "+paiement.idClient+" "+paiement.idAdresse+" "+paiement.idTransporteur+" "+paiement.typeEncaissement+" "+paiement.modeDePaiement+" "+paiement.datePaiementPrevue+" "+paiement.datePaiementReel+" "+paiement.statut+" "+paiement.montantAPayer+" "+paiement.montantEncaisse );

                DB.savePaiement(paiement);


            }

            Log.d("esi.dz", "apres la recuperation de la table Paiement ");





//            Connection con9=DriverManager.getConnection(url, user, pass);
//            Statement st9= con9.createStatement();
//            ResultSet rs9=st9.executeQuery("select "+DatabaseManager.KEY_ID_VEHICULE+" , "+DatabaseManager.KEY_MATRICULE+" from  "+DatabaseManager.TABLE_VEHICULE+"");
//            con9.close();


//
//            while (rs.next()){
//                CommandeClient c = new CommandeClient();
//                result1 = rs.getInt("id");
//                result2 = rs.getString("nom");
//                c.setIdClient(result1);
//                c.setNomClient(result2);
//                DB.saveClient(c);
//            }


//
//            //remplissage de la table vehicule
//            while (rs9.next()){
//                agent a2 = new agent();
//                rslt = rs9.getInt(DatabaseManager.KEY_ID_VEHICULE);
//                reslt = rs9.getInt(DatabaseManager.KEY_MATRICULE);
//                a2.setIdVehicule(rslt);
//                a2.setMatricule(reslt);
//                DB.saveVehicule(a2);
//            }

            DB.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void clearData(){
        DB.open();
        DB.Clear();
        DB.close();
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public  void synchronisation_mobile_serveur(){
       DB.open();
        Cursor c=DB.getEtatCommande();
        if(c.moveToFirst()) {
            do {
                String sql="update commandeclient " +
                        "SET statut='Livreee'"+
                        "WHERE id ="+c.getInt(c.getColumnIndex("idCommande"))+"";
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
            do {
                String sql="update lignecommande " +
                        "SET quantiteLivree="+c.getInt(c.getColumnIndex("QuantiteLivree"))+" " +
                        ", quantite_livree_reel ="+c.getInt(c.getColumnIndex("QuantiteLivree"))+" " +
                        "WHERE id ="+c.getInt(c.getColumnIndex("idLigneCommande"))+"";
                Log.d("Requete","test : "+sql);
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
        c.close();
        /***************************************/

        c=DB.getEmployeConnecteGPS();
        if(c.moveToFirst()) {
            do {

                String requete_select="SELECT * FROM localisation WHERE employeID="+c.getInt(c.getColumnIndex("employeID"))+"";

                String requete_insert="INSERT INTO localisation VALUES (null,'"+c.getString(c.getColumnIndex("latitude"))+"' ,'"+c.getString(c.getColumnIndex("longitude"))+"','"+c.getString(c.getColumnIndex("nom_connecte"))+"',"+c.getInt(c.getColumnIndex("employeID"))+",'"+getTodaysDate()+"','"+getTodaysDate()+"')";

                String requete_update="UPDATE localisation SET latitude="+c.getString(c.getColumnIndex("latitude"))+" ,longitude="+c.getString(c.getColumnIndex("longitude"))+" ,updated_at='"+getTodaysDate()+"' WHERE employeID="+c.getInt(c.getColumnIndex("employeID"));
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd= DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    ResultSet resultat_select=st1.executeQuery(requete_select);
                    if(resultat_select!=null)
                    {
                        Log.d("localisation", requete_update);
                        st1.executeUpdate(requete_update);
                    }
                    else
                    {
                        Log.d("localisation", requete_insert);
                        st1.executeUpdate(requete_insert);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        c.close();

        DB.close();
    }


    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();

        String todaysDate =
                String.valueOf(
                            (c.get(Calendar.YEAR) ) +"-"+
                            (c.get(Calendar.MONTH)+1 ) ) +"-"+
                            (c.get(Calendar.DAY_OF_MONTH))+"T"+
                            (c.get(Calendar.HOUR_OF_DAY))+":"+
                            (c.get(Calendar.MINUTE))+":"+
                            (c.get(Calendar.SECOND)
                        );

        return(String.valueOf(todaysDate));

    }


    public class Do_Synchonistion extends AsyncTask<String, String, String> {



        public Do_Synchonistion()
        {


        }



        protected void onPreExecute() {


        }


        protected String doInBackground(String... arg0) {

            clearData();
            testDB();

            return "ok";
        }


        protected void onPostExecute(String strFromDoInBg) {

            if (strFromDoInBg.equals("ok")){
                Intent intent2 = new Intent(synchronisation_activity.this, acceiul_activity.class);
                intent2.putExtra("loginselectionne", a);
                intent2.putExtra("id_agent", id_agent);
                startActivity(intent2);
                synchronisation_activity.this.finish();
            }
            else {
                Toast.makeText(context,"Erreur de synchronisation",Toast.LENGTH_LONG).show();
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void clear (String table){
        String sql="delete from " +table;
        Log.d("Requete","test : "+sql);
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
    }


}
