package com.newthink.user02.venteembarquee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseManager {

//    public static final String KEY_URL = "jdbc:mysql://178.33.234.31:3306/eurequat_vente_embarque";
//    public static final String KEY_USER = "eurequat";
//    public static final String KEY_PASSWORD = "FM56HTR7";

    public static final String KEY_URL = "jdbc:mysql://192.168.1.3:3306/vente3";
    public static final String KEY_USER = "eurequat1";
    public static final String KEY_PASSWORD = "eurequat1";

    public DatabaseManager() {

    }

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        maBaseSQLite = new MySQLite(context);
    }


    /**
     * ****************************    Ouverture et fermeture de la base de données   ****************************************************************
     */

    public SQLiteDatabase open() {
        //on ouvre la table en lecture/-écriture
        db = maBaseSQLite.getWritableDatabase();
        return null;
    }

    public void close() {
        //on ferme l'accès à la BDD
        db.close();
    }


    /************************************************************Agent**********************************************************/
    public static final String AGENT_KEY = "id";
    public static final String EXTRA_LOGIN = "login";
    public static final String EXTRA_PASS = "motdepass";
    public static final String TABLE_NAME = "Agent";

    /* ceration de la table agent*/
    public static final String CREATE_TABLE_AGENT = "CREATE TABLE " + TABLE_NAME +
            " (" +
            " " + AGENT_KEY + " INTEGER PRIMARY KEY , " +
            " " + EXTRA_LOGIN + " text," +
            " " + EXTRA_PASS + " text" +
            ");";

    /* cette methode sert a verifier l'authentification de la 'agent*/
    public boolean verifierAgent(String login, String mdp) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE login='" + login + "' and motdepass='" + mdp + "'", null);
        if (c.moveToFirst()) {
            return  true;
        } else {
            return false;
        }
    }

    /* cette methode sert a enregister les champs de la table agent*/
    public long putInformation(agent a) {
        ContentValues cv = new ContentValues();
        cv.put(EXTRA_LOGIN, a.getLogin().toString());
        cv.put(EXTRA_PASS, a.getPass().toString());
        return db.insert(TABLE_NAME, null, cv);
    }

    public Cursor getAgents(){
        return db.rawQuery("SELECT login FROM Agent ",null);
    }


                                                          /* La table Vehicule*/
    /************************************************************Vehicule*************************************************/
    public static final String TABLE_VEHICULE = "Vehicule";
    public static final String KEY_ID_VEHICULE = "id";
    public static final String KEY_MATRICULE= "matricule";
    public static final String KEY_KM_VEHICULE = "KMDebut";
    //public static final String KM_FIN_VEHICULE = "KMFin";
    public static final String KM_CAPACITE_VEHICULE = "capaciteStockage";

    /* creation de la table vehicule*/
    public static final String CREATE_TABLE_VEHICULE = "CREATE TABLE " + TABLE_VEHICULE +
            " (" +
            " " + KEY_ID_VEHICULE + " INTEGER PRIMARY KEY , " +
            " " + KEY_MATRICULE + " INTEGER ," +
            " " + KEY_KM_VEHICULE + " INTEGER ," +
            " " + KM_CAPACITE_VEHICULE + " INTEGER " +
            " );";

    public  long saveVehicule(agent a){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_VEHICULE,a.getIdVehicule());
        cv.put(KEY_MATRICULE,a.getMatricule());
        return db.insert(DatabaseManager.TABLE_VEHICULE,null,cv);
    }

    public Long saveVehiculeT(agent a,int i) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_KM_VEHICULE,a.getKMDebut());
        return db.insert(TABLE_TOUNREE, "idTournee=" + i, cv);
    }
    /**********************************************************************************************************************/




    /************************************************************ Tournee **********************************************************/
    public static final String KEY_ID_TOURNEE = "id";
    public static final String KEY_ID_AGENT_TOURNEE = "idTransporteur";
    public static final String KEY_DATE_DEBUT_TORUNEE = "DateDebut";
    public static final String KEY_DATE_FIN_TOURNEE = "Datefin";
    public static final String KEY_KM_DEBUT_TOURNEE="KMDebut";
    public static final String KEY_KM_FIN_TOURNEE = "KMFin";
    public static final String KEY_FONT_INITIAL_RECU_TOURNEE = "fondInitialrecu";
    public static final String KEY_STATUT_TOURNEE = "statut";
    public static final String KEY_ID_VEHICULE_TOURNEE = "idvehicule";
    public static final String KEY_MOTIF_ECART_CAISSE_TOURNEE = "MotifEcartCaisse";
    public static final String KEY_MOTIF_ECART_STOCK_TOURNEE = "MotifEcartStock";
    public static final String KEY_AUTRE_MOTIF_ECART_CAISSE_TOURNEE = "AutreMotifEcartCaisse";
    public static final String KEY_AUTRE_MOTIF_ECART_STOCK_TOURNEE = "AutreMotifEcartStock";
    public static final String KEY_ETAT_TOURNEE= "etat";

    public static final String TABLE_TOUNREE = "tournee";

    public static final String CREATE_TABLE_TOURNEE = "CREATE TABLE " + TABLE_TOUNREE +
            " (" +
            " " + KEY_ID_TOURNEE + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_AGENT_TOURNEE + " integer," +
            " " + KEY_DATE_DEBUT_TORUNEE + " date," +
            " " + KEY_DATE_FIN_TOURNEE + " date," +
            " " + KEY_KM_DEBUT_TOURNEE +" integer,"+
            " " + KEY_KM_FIN_TOURNEE +" integer,"+
            " " + KEY_FONT_INITIAL_RECU_TOURNEE + " integer ," +
            " " + KEY_STATUT_TOURNEE + " text," +
            " " + KEY_ID_VEHICULE_TOURNEE + " integer," +
            " " + KEY_MOTIF_ECART_CAISSE_TOURNEE + " text ," +
            " " + KEY_MOTIF_ECART_STOCK_TOURNEE + " text," +
            " " + KEY_AUTRE_MOTIF_ECART_CAISSE_TOURNEE + " text," +
            " " + KEY_AUTRE_MOTIF_ECART_STOCK_TOURNEE + " text ," +
            " " + KEY_ETAT_TOURNEE + " text " +
            ")";

    public int saveCach(agent a, int l) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE, a.getCachRecu());
        cv.put(DatabaseManager.KEY_KM_DEBUT_TOURNEE,a.getKMDebut());
        return db.update(DatabaseManager.TABLE_TOUNREE, cv, "id="+l,null );

    }

    public int SaveInitTournee(int km_debut,int cash_init, int id) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE, cash_init);
        cv.put(DatabaseManager.KEY_KM_DEBUT_TOURNEE,km_debut);
        return db.update(DatabaseManager.TABLE_TOUNREE, cv, "id="+id,null );

    }

    public Cursor getIdAgent(String a){
        return db.rawQuery("select id from " + TABLE_NAME + " where login='" + a + "'", null);
    }
    public Cursor getIdTournee(int a){
        String today = getTodaysDate();
        String ss="select id from tournee where "+KEY_ID_AGENT_TOURNEE+"="+a;
        Log.d("testinggg","requette : "+ss);
        return db.rawQuery("select id from tournee where "+KEY_ID_AGENT_TOURNEE+"="+a+" and statut = 'EN COURS'",null);
    }

    public long saveTournee(agent a){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_ID_TOURNEE, a.getIdTournee());
        cv.put(DatabaseManager.KEY_ID_VEHICULE_TOURNEE,a.getIdVehicule());
        cv.put(DatabaseManager.KEY_ID_AGENT_TOURNEE,a.getId());
        cv.put(DatabaseManager.KEY_DATE_DEBUT_TORUNEE,a.getDateDebut());
        cv.put(DatabaseManager.KEY_DATE_FIN_TOURNEE,a.getDateFin());
        cv.put(DatabaseManager.KEY_STATUT_TOURNEE,a.getStatut());
        cv.put(DatabaseManager.KEY_ETAT_TOURNEE, "NON-DEMARRER");
    return db.insert(DatabaseManager.TABLE_TOUNREE, null, cv);
    }
    public Cursor getKilometrage(int id){
        Cursor a=null;
        a=db.rawQuery("SELECT KMDebut " +
                "FROM tournee   " +
                "WHERE id="+id+"",null);
        return a;
    }
    public Cursor getInfoTour(int i){
        Cursor b=null;
        b = db.rawQuery("SELECT KMDebut,DateDebut,fondInitialrecu  " +
                "FROM tournee WHERE id="+i+"",null);
        return b;
    }

    public int updateTournee(agent a, int i) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_DATE_FIN_TOURNEE, a.getDateFin());
        cv.put(DatabaseManager.KEY_ETAT_TOURNEE,"Terminée");
        cv.put(DatabaseManager.KEY_KM_FIN_TOURNEE,a.getKMFin());
        cv.put(DatabaseManager.KEY_MOTIF_ECART_CAISSE_TOURNEE,a.MotifEcartCaisse);
        cv.put(DatabaseManager.KEY_MOTIF_ECART_STOCK_TOURNEE,a.MotifEcartStock);
        return db.update(DatabaseManager.TABLE_TOUNREE, cv, "id=" + i, null);
    }


    public int SaveDateFinTour(agent a, int i) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_DATE_FIN_TOURNEE, a.getDateFin());
        return db.update(DatabaseManager.TABLE_TOUNREE, cv, "id=" + i, null);
    }

    public int UpdateCash(int cash, int j){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE, cash);
       return db.update(DatabaseManager.TABLE_TOUNREE, cv, "id=" + j, null);
    }



    public int UpdaeTournee(String statut,int j){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STATUT_TOURNEE,statut);
        return db.update(DatabaseManager.TABLE_TOUNREE, cv,"id="+j, null);
    }

    public int UpdateEtatTournee(String etat,int j){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_ETAT_TOURNEE,etat);
        return db.update(DatabaseManager.TABLE_TOUNREE, cv,"id="+j, null);
    }

    public boolean getEtatTournee(int j){
        Cursor c = db.rawQuery("select etat from tournee where id = "+j,null);

        if(c.moveToFirst() && c.getString(0).equals("DEMARREE")){
            return true;
        }
        return false;
    }


    /************************************************************ Table Produit **********************************************************/
    public static final String KEY_ID_PRODUIT = "id";
    public static final String KEY_DESIGNATION_PRODUIT = "Designation";
    public static final String KEY_ID_CATEGORIE_PRODUIT = "idCategorie";
    public static final String KEY_CODE_INDENTIFICATION_PRODUIT = "CodeIdentification";
    public static final String KEY_PRIX_ACHAT_PRODUIT = "PrixAchatAppliquee";
    public static final String KEY_CUMP_PRODUIT = "CUMP";
    public static final String KEY_REFERENCE_PRODUIT = "reference";
    public static final String KEY_CODE_MESURE_PRODUIT = "codeMesure";
    public static final String KEY_TVA_PRODUIT= "tauxTVA";
    public static final String TABLE_PRODUIT = "produit";

    public static final String CREATE_TABLE_PRODUIT = "CREATE TABLE " + TABLE_PRODUIT +
            " (" +
            " " + KEY_ID_PRODUIT + " INTEGER PRIMARY KEY , " +
            " " + KEY_DESIGNATION_PRODUIT + " varchar," +
            " " + KEY_ID_CATEGORIE_PRODUIT + " integer," +
            " " + KEY_CODE_INDENTIFICATION_PRODUIT + " text," +
            " " + KEY_PRIX_ACHAT_PRODUIT + " text" +
            " " + KEY_CUMP_PRODUIT + " text," +
            " " + KEY_REFERENCE_PRODUIT + " text," +
            " " + KEY_CODE_MESURE_PRODUIT + " text," +
            " " + KEY_TVA_PRODUIT + " integer" +
            ");";

    /****************************************************Methode produit*****************************************/
    public long putInformationProduit(Produit p) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_PRODUIT,p.getIdProduit());
        cv.put(KEY_DESIGNATION_PRODUIT,p.getDesignation());
        return db.insert(TABLE_PRODUIT, null, cv);
    }
    public int UpdateStock(int idP) {
        Produit a =new Produit();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE,a.getStockInitialeEstime());
        return db.update(DatabaseManager.TABLE_STOCK_TOURNEE, cv, "idProduit="+idP, null);
    }
    /*************************************************************************************************************/





    /************************************************************ Table Stock Tournee **********************************************************/
    public static final String KEY_ID_STOCK_TOURNEE = "id";
    public static final String KEY_ID_PRODUIT_STOCK_TOURNEE = "idProduit";
    public static final String KEY_ID_TOURNEE_STOCK_TOURNEE = "idTournee";
    public static final String KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE = "stockinitialestime";
    public static final String KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE = "stockinitialReel";
    public static final String KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE = "stockFinalEstime";
    public static final String KEY_STOCK_EN_COURS_STOCK_TOURNEE = "stockEncours";
    public static final String KEY_STOCK_FINAL_REEL_STOCK_TOURNEE = "stockFinalReel";


    public static final String TABLE_STOCK_TOURNEE = "tracestocktournee";

    public static final String CREATE_TABLE_TRACE_STOCK_TOURNEE = " CREATE TABLE " + TABLE_STOCK_TOURNEE +
            " (" +
            " " + KEY_ID_STOCK_TOURNEE + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_PRODUIT_STOCK_TOURNEE + " integer," +
            " " + KEY_ID_TOURNEE_STOCK_TOURNEE + " integer," +
            " " + KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE + " integer," +
            " " + KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE + " integer," +
            " " + KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE + " integer," +
            " " + KEY_STOCK_EN_COURS_STOCK_TOURNEE + " integer," +
            " " + KEY_STOCK_FINAL_REEL_STOCK_TOURNEE + " integer" +
            ");";
    /****************************************************Methode stock*****************************************/
    public long putInformationStock(Produit s,agent a) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_STOCK_TOURNEE,s.getIdStock());
        cv.put(KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE, s.getStockInitialeEstime());
        cv.put(KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE, s.getStockInitialeReel());
        cv.put(KEY_STOCK_EN_COURS_STOCK_TOURNEE, s.getStockEncours());
        cv.put(KEY_ID_PRODUIT_STOCK_TOURNEE,s.getIdProduit());
        cv.put(KEY_ID_TOURNEE_STOCK_TOURNEE,a.getIdTournee());
        cv.put(KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE, s.getStockFinalEstime());
        cv.put(KEY_STOCK_FINAL_REEL_STOCK_TOURNEE, s.getStockFinalReel());
        return db.insert(TABLE_STOCK_TOURNEE, null, cv);
    }
    /***************************************************Verification du stock***********************************/
      public Cursor getProd(int idTour) {

         String ss= "SELECT t1.id , t1.Designation, t2.stockinitialestime , t2.stockinitialReel " +
                  "FROM  Produit t1 " +
                  "inner join tracestocktournee t2 on t1.id= t2.idProduit " +
                  "WHERE t2.idTournee="+idTour;

          Log.d("ssss", "req = " + ss);



        return db.rawQuery("SELECT t1.id , t1.Designation, t2.stockinitialestime , t2.stockinitialReel " +
                "FROM  Produit t1 " +
                "inner join tracestocktournee t2 on t1.id= t2.idProduit " +
                "WHERE t2.idTournee=" + idTour + "", null);
    }

    public Cursor geInventaire(int idTour) {

        String ss="SELECT t1.id , t1.Designation, t2.stockFinalEstime , t2.stockFinalReel " +
        "FROM  Produit t1 " +
                "inner join tracestocktournee t2 on t1.id= t2.idProduit " +
                "Where t2.idTournee= "+idTour;
        Log.d("testing","req = "+ss);

        String req="SELECT t1.id , t1.Designation, t2.stockFinalEstime , t2.stockFinalReel " +
                "FROM  Produit t1 " +
                "inner join tracestocktournee t2 on t1.id= t2.idProduit " +
                "Where t2.idTournee= "+idTour;

        Log.d("mooooooose","req : "+req);

        return db.rawQuery("SELECT t1.id , t1.Designation, t2.stockFinalEstime , t2.stockFinalReel " +
                "FROM  Produit t1 " +
                "inner join tracestocktournee t2 on t1.id= t2.idProduit " +
                "Where t2.idTournee= "+idTour+"", null);
    }

    public Cursor getStockEncours(int idT){

        String ss="SELECT DISTINCT t2."+KEY_DESIGNATION_PRODUIT+" , t1."+KEY_STOCK_EN_COURS_STOCK_TOURNEE+" , t1."+KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE+" " +
                " FROM   tracestocktournee t1 " +
                "inner  join Produit t2 on t2.id = t1.idProduit " +
                "inner join livraison t3 on  t3.idTournee=t1.idTournee " +
                "WHERE t1.idTournee="+idT;

        Log.d("testing","req = "+ss);


        String req="SELECT DISTINCT t2."+KEY_DESIGNATION_PRODUIT+" , t1."+KEY_STOCK_EN_COURS_STOCK_TOURNEE+" , t1."+KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE+" " +
                " FROM   tracestocktournee t1 " +
                "inner  join Produit t2 on t2.id = t1.idProduit " +
                "inner join livraison t3 on  t3.idTournee=t1.id " +
                "WHERE t1.id="+idT;


        return db.rawQuery("SELECT DISTINCT t2."+KEY_DESIGNATION_PRODUIT+" , t1."+KEY_STOCK_EN_COURS_STOCK_TOURNEE+" , t1."+KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE+" " +
                        " FROM   tracestocktournee t1 " +
                        "inner  join Produit t2 on t2.id = t1.idProduit " +
                        "inner join livraison t3 on  t3.idTournee=t1.idTournee " +
                        "WHERE t1.idTournee="+idT+"", null);
    }
public  Cursor getEcart(int idT){
    return  db.rawQuery("SELECT t1."+KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE+" , t1."+KEY_STOCK_FINAL_REEL_STOCK_TOURNEE+" " +
            "FROM tracestocktournee t1 " +
            "inner  join Produit t2 on t2.id = t1.idProduit " +
            "WHERE t1.id="+idT+"",null);
}

    public  Cursor getEcartCaisse(int idT){
        return  db.rawQuery("SELECT t1."+KEY_FONT_INITIAL_RECU_TOURNEE+" " +
                "FROM "+TABLE_TOUNREE+" t1 " +
                "WHERE t1.id="+idT+"",null);
    }


    /************************************************************ Table Client **********************************************************/
    public static final String KEY_ID_CLIENT = "id";
    public static final String KEY_NOM_CLIENT = "Nom";
    public static final String KEY_PRENOM_CLIENT = "Prenom";
    public static final String KEY_DATE_CREATION_CLIENT = "DateCreation";
    public static final String KEY_SEGMENT_CLIENT = "SegmentClient";
    public static final String KEY_EMAIL_CLIENT = "Email";
    public static final String KEY_TELEPHONE_CLIENT = "telephone";

    public static final String TABLE_CLIENT = "client";

    public static final String CREATE_TABLE_CLIENT = "CREATE TABLE " + TABLE_CLIENT +
            " (" +
            " " + KEY_ID_CLIENT + " INTEGER PRIMARY KEY , " +
            " " + KEY_NOM_CLIENT + " text," +
            " " + KEY_PRENOM_CLIENT + " text," +
            " " + KEY_DATE_CREATION_CLIENT + " DATE," +
            " " + KEY_SEGMENT_CLIENT + " text," +
            " " + KEY_EMAIL_CLIENT + " text," +
            " " + KEY_TELEPHONE_CLIENT + " integer" +
            ");";
    /******************************************************Methode client **************************************************/

    /***************************Ajouter client***********************/
    public Long saveClient(CommandeClient c) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_CLIENT, c.getIdClient());
        cv.put(KEY_NOM_CLIENT, c.getNomClient());
        cv.put(KEY_PRENOM_CLIENT, c.getPrenomClient());
        cv.put(DatabaseManager.KEY_TELEPHONE_CLIENT,c.getTelephoneClient());
        return db.insert(DatabaseManager.TABLE_CLIENT, null, cv);
    }
    /**********************Retourner client **********************************************/
 public Cursor getClientTournee(){
     Cursor a=null;
     //  a= db.rawQuery("SELECT idCommande FROM "+TABLE_COMMANDE_CLIENT +" WHERE "+COMMANDE_KEY+"=1", null);
     a=db.rawQuery("SELECT   DISTINCT t1.Nom " +
             " FROM Client t1 " +
             "inner  join etapetournee t2 on t2.idClient = t1.idClient ",null);
     return a;
 }
 public List<String> getClient(){
     List<String> labels = new ArrayList<String>();
     // Select All Query
     String selectQuery = "SELECT  Nom FROM "+TABLE_CLIENT+"" ;
     // returning lables

     Cursor cursor = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
         do {
             labels.add(cursor.getString(cursor.getColumnIndex(KEY_NOM_CLIENT)));
         } while (cursor.moveToNext());
     }

     // closing connection
     cursor.close();
     db.close();
     return labels;
 }





    /************************************************************ Table Livraison **********************************************************/
    public static final String KEY_ID_LIVRAISON = "id";
    public static final String KEY_ID_TOURNEE_LIVRAISON = "idTournee";
    public static final String KEY_DATE_LIVRAISON_PREVUE = "DateLivraisonPrev";
    public static final String KEY_DATE_LIVRAISON_REELLE = "DateLivraisonReel";
    public static final String KEY_ID_AGENT_LIVRAISON = "idTransporteur";
    public static final String KEY_MOTIF_NON_LIVRAISON = "motifnonlivraison";
    //public static final String ETAT ="EtatLivraison";
    public static final String KEY_STATUT_LIVRAISON = "statut";

    public static final String TABLE_LIVRAISON = "Livraison";

    public static final String CREATE_TABLE_LIVRAISON = "CREATE TABLE " + TABLE_LIVRAISON +
            " (" +
            " " + KEY_ID_LIVRAISON + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_TOURNEE_LIVRAISON + " integer," +
            " " + KEY_DATE_LIVRAISON_PREVUE + " DATE," +
            " " + KEY_DATE_LIVRAISON_REELLE + " DATE," +
            " " + KEY_ID_AGENT_LIVRAISON + " integer," +
            " " + KEY_MOTIF_NON_LIVRAISON + " text," +
           // " " + ETAT + " text,"+
            " " + KEY_STATUT_LIVRAISON + " text" +
            ")";
    /********************************************************Methode Livraison*************************************************/

    public Long saveLivraison(CommandeClient c,agent a) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_LIVRAISON,c.getIdLivraison());
        cv.put(KEY_DATE_LIVRAISON_PREVUE,c.getDateLivraison());
        cv.put(KEY_STATUT_LIVRAISON,c.getEtatLi());
        cv.put(KEY_ID_AGENT_LIVRAISON,a.getId());
        cv.put(KEY_ID_TOURNEE_LIVRAISON,a.getIdTournee());
        return db.insert(DatabaseManager.TABLE_LIVRAISON, null, cv);
    }

    public Cursor getDateLiv(String condition){
        Cursor b=null;
        b = db.rawQuery("SELECT  DISTINCT t1.id , t1.DateLivraisonPrev" +
                " FROM Livraison t1 "+
                "inner join etapetournee t2 on t2.idLivraison = t1.id ",null);
        return b;
    }
    public int UpdateQTE(int QTE,int idp,int com) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_QUANTITE_LIVREE_LIGNE_COMMANDE, QTE);
        return db.update(DatabaseManager.TABLE_LIGNE_COMMANDE, cv,"idProduit="+idp+" and idCommande="+com+"", null);
    }
    public int UpdateSTE(int QTE,int idp) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE, QTE);
        cv.put(DatabaseManager.KEY_STOCK_EN_COURS_STOCK_TOURNEE, QTE);
        return db.update(DatabaseManager.TABLE_STOCK_TOURNEE, cv,"idProduit="+idp, null);
    }
    public long UpadteStock(int idpP,int SFE,int SFR,int idTou){
        ContentValues cv = new ContentValues();
        cv.put(KEY_STOCK_FINAL_ESTIME_STOCK_TOURNEE,SFE);
        cv.put(KEY_STOCK_FINAL_REEL_STOCK_TOURNEE,SFR);
        cv.put(KEY_STOCK_EN_COURS_STOCK_TOURNEE,SFR);
        return db.update(TABLE_STOCK_TOURNEE,cv,"idProduit="+idpP+" and idTournee="+idTou+"",null);
    }
   public int UpdateEtat(String EtatLiv,int idLiv) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STATUT_LIVRAISON, EtatLiv);
        return db.update(DatabaseManager.TABLE_LIVRAISON, cv,"idLivraison="+idLiv, null);
    }

    public Cursor getStockIn(int idP,int idT){
        return db.rawQuery("SELECT "+KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE+" , "+KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE+" FROM "+TABLE_STOCK_TOURNEE+"  " +
                "WHERE idProduit="+idP+" " +
                "and idTournee="+idT+"",null);
    }







    /*************************************************Table Commande*******************************************************/
    public static final String KEY_ID_COMMANDE = "id";
    public static final String KEY_ID_LIVRAISON_COMMANDE = "idLivraison";
    public static final String KEY_DATE_CREATION_COMMANDE = "DateCreation";
    public static final String KEY_TOTAL_PRIX_HT_COMMANDE = "TotalPrixHT";
    public static final String KEY_TOTAL_PRIX_TTC_COMMANDE = "totalPrixTTC";
    public static final String KEY_ID_CLIENT_COMMANDE = "idClient";
    public static final String KEY_ID_ADRESSE_COMMANDE = "idAdresseLivraison";
    public static final String KEY_STATUT_COMMANDE ="statut";
  //  public static final String ETAT_COMMANDE="EtatCommande";
    public static final String TABLE_COMMANDE_CLIENT = "commandeclient";

    public static final String CREATE_TABLE_COMMANDE_CLIENT = "CREATE TABLE " + TABLE_COMMANDE_CLIENT +
            " (" +
            " " + KEY_ID_COMMANDE + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_LIVRAISON_COMMANDE + " integer," +
            " " + KEY_DATE_CREATION_COMMANDE + " DATE," +
            " " + KEY_TOTAL_PRIX_HT_COMMANDE + " integer," +
            " " + KEY_TOTAL_PRIX_TTC_COMMANDE + " integer," +
            " " + KEY_ID_CLIENT_COMMANDE + " integer," +
            " " + KEY_ID_ADRESSE_COMMANDE + " integer," +
            " " + KEY_STATUT_COMMANDE + " text" +

            ");";
    /************************************************************Methode Commande**********************************************************/
    public Long saveCommande(CommandeClient c) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_COMMANDE,c.getIdCommande());
        cv.put(KEY_DATE_CREATION_COMMANDE,c.getDateCreationC());
        cv.put(KEY_ID_CLIENT_COMMANDE,c.getIdClient());
        cv.put(KEY_ID_LIVRAISON_COMMANDE,c.getIdLivraison());
        cv.put(KEY_STATUT_COMMANDE,c.getStatut());
        return db.insert(DatabaseManager.TABLE_COMMANDE_CLIENT,null,cv);
    }
    public Cursor getCommande(){
        Cursor a=null;
      a=db.rawQuery("SELECT   DISTINCT t1.id " +
                " FROM commandeclient t1 " +
                "inner  join etapetournee t2 on t2.idCommande = t1.id ",null);
        return a;}

    public int UpdateEtatCommande(String EtatLiv,int idCommande) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STATUT_COMMANDE, EtatLiv);
        return db.update(DatabaseManager.TABLE_COMMANDE_CLIENT, cv,"id="+idCommande, null);
    }

    public int UpdateEtatEtape(String EtatTour,int idLivraison) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.KEY_STATUT_ETAPE_TOURNEE, EtatTour);
        return db.update(DatabaseManager.TABLE_ETAPE_TOURNEE, cv,"idLivraison="+idLivraison, null);
    }

    /********************************************************Table ligne commande*************************************************/
    public static final String KEY_ID_LIGNE_COMMANDE = "id";
    public static final String KEY_ID_PRODUIT_LIGNE_COMMANDE = "idProduit";
    public static final String KEY_ID_COMMANDE_LIGNE_COMMANDE = "idCommande";
    public static final String KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE= "QuantiteCmd";
    public static final String KEY_QUANTITE_LIVREE_LIGNE_COMMANDE = "quantiteLivree";
    public static final String KEY_QUANTITE_LIVREE_REELLE_LIGNE_COMMANDE = "quantite_livree_reel";
    public static final String KEY_PRIX_HT_LIGNE_COMMANDE = "PrixHT";
    public static final String KEY_PRIX_TTC_LIGNE_COMMANDE = "PrixTTC";
    public static final String KEY_TOTAL_PRIX_TTC_LIGNE_COMMANDE = "totalPrixTTC";
    public static final String KEY_TAUX_REMISE_LIGNE_COMMANDE = "tauxRemise";
    public static final String KEY_MONTANT_REMISE_LIGNE_COMMANDE = "MontantRemise";
    public static final String KEY_COMMANTAIRE_LIGNE_COMMANDE = "commentaire";
    public static final String TABLE_LIGNE_COMMANDE = "lignecommande";
    public static final String CHECK_KEY = "Existe";



    public static final String CREATE_TABLE_LIGNE_COMMANDE = "CREATE TABLE " + TABLE_LIGNE_COMMANDE +
            " (" +
            " " + KEY_ID_LIGNE_COMMANDE + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_PRODUIT_LIGNE_COMMANDE + " integer," +
            " " + KEY_ID_COMMANDE_LIGNE_COMMANDE + " integer," +
            " " + KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE + " integer," +
            " " + KEY_QUANTITE_LIVREE_LIGNE_COMMANDE + " integer," +
            " " + KEY_QUANTITE_LIVREE_REELLE_LIGNE_COMMANDE + " integer," +
            " " + KEY_PRIX_HT_LIGNE_COMMANDE + " real," +
            " " + KEY_PRIX_TTC_LIGNE_COMMANDE + " real," +
            " " + KEY_TOTAL_PRIX_TTC_LIGNE_COMMANDE + " real," +
            " " + KEY_TAUX_REMISE_LIGNE_COMMANDE + " real," +
            " " + KEY_MONTANT_REMISE_LIGNE_COMMANDE + " real," +
            " " + KEY_COMMANTAIRE_LIGNE_COMMANDE + " text," +
            " "+ CHECK_KEY + " text" +

            ");";
    /************************************************************ Methode  **********************************************************/
    public long putInformationLigneCmd(CommandeClient cm,Produit s) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_LIGNE_COMMANDE,  s.getIdLigneCommande());
        cv.put(KEY_ID_COMMANDE_LIGNE_COMMANDE, cm.getIdCommande());
        cv.put(KEY_ID_PRODUIT_LIGNE_COMMANDE, s.getIdProduit());
        cv.put(KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE,s.getQuantiteCmd());
        cv.put(KEY_QUANTITE_LIVREE_LIGNE_COMMANDE,s.getQuantiteLivree());
        cv.put(CHECK_KEY,"-1");
        return db.insert(TABLE_LIGNE_COMMANDE, null, cv);
    }

    public Cursor getProdLiv(int idLiv,int idCom) {
        String req="SELECT DISTINCT t1.id , t1.Designation, t2.QuantiteCmd , t2.QuantiteLivree " +
                " FROM  Produit t1 " +
                " inner join lignecommande t2 on t1.id= t2.idProduit " +
                " inner join commandeclient t3 on t3.id = t2.idCommande " +
                " inner join livraison t5 on t5.id="+idLiv+" " +
                " where t5.id =t3.idLivraison " +
                " and t3.id = "+idCom+" ";

            Log.d("rerererere","req = "+req);

        return db.rawQuery(req, null);

    }

    /************************************************************ Table Etape Tournee **********************************************************/

    public static final String KEY_ID_ETAPE_TOURNEE= "id";
    public static final String KEY_ID_CLIENT_ETAPE_TOURNEE= "idClient";
    public static final String KEY_ID_TOURNEE_ETAPE_TOURNEE= "idTournee";
    public static final String KEY_ID_LIVRAISON_ETAPE_TOURNEE = "idLivraison";
    public static final String KEY_ADRESSE_ETAPE_TOURNEE = "adresse";
    public static final String KEY_ID_COMMANDE_ETAPE_TOURNEE = "idCommande";
    public static final String KEY_LONGITUDE_ETAPE_TOURNEE= "longitude";
    public static final String KEY_LATITUDE_ETAPE_TOURNEE= "latitude";
    public static final String KEY_ID_ENCAISSEMENT_ETAPE_TOURNEE= "idEncaissement";
    public static final String KEY_NUM_ETAPE_TOURNEE="numEtape";
    public static final String KEY_MOTIF_VISITE_ETAPE_TOURNEE = "motifvisit";
    public static final String KEY_STATUT_ETAPE_TOURNEE="statut";



    public static final String TABLE_ETAPE_TOURNEE = "etapetournee";

    public static final String CREATE_TABLE_ETAPE_TOURNEE = "CREATE TABLE " + TABLE_ETAPE_TOURNEE +
            " (" +
            " " + KEY_ID_ETAPE_TOURNEE + " integer PRIMARY KEY, " +
            " " + KEY_ID_CLIENT_ETAPE_TOURNEE + " integer," +
            " " + KEY_ID_TOURNEE_ETAPE_TOURNEE + " integer," +
            " " + KEY_ID_LIVRAISON_ETAPE_TOURNEE + " integer," +
            " " + KEY_ADRESSE_ETAPE_TOURNEE + " text," +
            " " + KEY_ID_COMMANDE_ETAPE_TOURNEE + " integer," +
            " " + KEY_LONGITUDE_ETAPE_TOURNEE + " text," +
            " " + KEY_LATITUDE_ETAPE_TOURNEE + " text," +
            " " + KEY_ID_ENCAISSEMENT_ETAPE_TOURNEE + " integer," +
            " " + KEY_NUM_ETAPE_TOURNEE + " integer," +
            " " + KEY_MOTIF_VISITE_ETAPE_TOURNEE + " text," +
            " " + KEY_STATUT_ETAPE_TOURNEE + " text" +
            ");";

    /***************************************methode **************************************************************/
    public Long saveEtape(CommandeClient cmm,String staut) {
        //Log.d("testing","id = "+cmm.getIdEtape());
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_ETAPE_TOURNEE, 1);
        cv.put(KEY_MOTIF_VISITE_ETAPE_TOURNEE, 2);
        cv.put(KEY_ID_CLIENT_ETAPE_TOURNEE, 3);
        cv.put(KEY_ID_COMMANDE_ETAPE_TOURNEE, 4);
        cv.put(KEY_ID_LIVRAISON_ETAPE_TOURNEE, 5);
        cv.put(KEY_NUM_ETAPE_TOURNEE,6);
        cv.put(KEY_STATUT_ETAPE_TOURNEE,7);


        Log.d("testingMOOSE55",KEY_ID_ETAPE_TOURNEE+" = "+cv.get(KEY_ID_ETAPE_TOURNEE));


        return db.insert(DatabaseManager.TABLE_ETAPE_TOURNEE, null, cv);
    }

    public Long saveEtape2(Etape etape) {
        //Log.d("testing","id = "+cmm.getIdEtape());
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_ETAPE_TOURNEE, etape.idEtape);
        cv.put(KEY_ID_CLIENT_ETAPE_TOURNEE, etape.idClient);
        cv.put(KEY_ID_TOURNEE_ETAPE_TOURNEE, etape.idTournee);
        cv.put(KEY_ID_LIVRAISON_ETAPE_TOURNEE, etape.idLivraison);
        cv.put(KEY_ADRESSE_ETAPE_TOURNEE, etape.adresseClient);
        cv.put(KEY_ID_COMMANDE_ETAPE_TOURNEE, etape.idCommande);
        cv.put(KEY_MOTIF_VISITE_ETAPE_TOURNEE, etape.motifVisite);
        cv.put(KEY_STATUT_ETAPE_TOURNEE,etape.statutEtape);
        cv.put(KEY_NUM_ETAPE_TOURNEE, 1);

        return db.insert(DatabaseManager.TABLE_ETAPE_TOURNEE, null, cv);
    }





    /*Table Categorie*/
    public static final String CATEGORIE_KEY = "idCategorie";
    public static final String NOM = "Nom";
    public static final String REMARQUE = "Remarque";
    public static final String TABLE_CATEGORIE = "Categorie";
    /* creation de la table categorie*/
    public static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE +
            " (" +
            " " + CATEGORIE_KEY + " INTEGER PRIMARY KEY , " +
            " " + NOM + " text," +
            " " + REMARQUE + " text" +
            ");";
    /**********************************************Table secteur*****************************************************/
    public static final String SECTEUR_KEY = "idSecteur";
    public static final String NOM_SECTEUR = "NomSecteur";
    public static final String TABLE_SECTEUR = "Secteur";

    public static final String CREATE_TABLE_SECTEUR = "CREATE TABLE" + TABLE_SECTEUR +
            " (" +
            " " + SECTEUR_KEY + " INTEGER PRIMARY KEY , " +
            " " + NOM_SECTEUR + " varchar" +
            ");";




    /*************************************************Table Point de livraison****************************************/
    public static final String KEY_ID_POINT_LIVRAISON = "id";
    public static final String KEY_NUMENO_POINT_LIVRAISON = "numero";
    public static final String KEY_RUE_POINT_LIVRAISON = "rue";
    public static final String KEY_COMMUNE_POINT_LIVRAISON = "commune";
    public static final String KEY_WILAYA_POINT_LIVRAISON = "wilaya";
    public static final String KEY_ID_CLIENT_POINT_LIVRAISON = "idClient";
    public static final String KEY_ID_SECTEUR_POINT_LIVRAISON = "idSecteur";
    public static final String KEY_LATITUDE_POINT_LIVRAISON = "latitude";
    public static final String KEY_LONGITUDE_POINT_LIVRAISON = "longitude";

    public static final String TABLE_POINT_LIVRAISON = "pointlivraison";

    public static final String CREATE_TABLE_POINT_LIVRAISON = "CREATE TABLE " + TABLE_POINT_LIVRAISON +
            " (" +
            " " + KEY_ID_POINT_LIVRAISON + " INTEGER PRIMARY KEY , " +
            " " + KEY_NUMENO_POINT_LIVRAISON + " integer," +
            " " + KEY_RUE_POINT_LIVRAISON + " varchar," +
            " " + KEY_COMMUNE_POINT_LIVRAISON + " text," +
            " " + KEY_WILAYA_POINT_LIVRAISON + " text," +
            " " + KEY_ID_CLIENT_POINT_LIVRAISON + " integer" +
            " " + KEY_ID_SECTEUR_POINT_LIVRAISON + " integer," +
            " " + KEY_LATITUDE_POINT_LIVRAISON + " text," +
            " " + KEY_LONGITUDE_POINT_LIVRAISON + " text" +
            ")";

    public Long savePoint(CommandeClient c) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_POINT_LIVRAISON,c.getIdPointLivr());
        cv.put(DatabaseManager.KEY_ID_CLIENT_POINT_LIVRAISON,c.getIdClient());
        return db.insert(DatabaseManager.TABLE_POINT_LIVRAISON, null, cv);
    }




    /************************************methode**********************************************/
    public Cursor getLivraison(int idTournee,int idAgent){

        String req="SELECT DISTINCT t1.id ,t1.statut ,t3.nom " +
                "FROM  commandeclient t1,livraison t2,client t3,etapetournee t4 " +
                "Where t3.id =t1.idClient " +
                "and t2.id=t1.idLivraison " +
                "and t4.idLivraison = t1.idLivraison " +
                "and t2.id=" + idAgent + " " +
                "and t2.idTournee=" + idTournee;
        Log.d("requeteLivraison", "requte : " + req);

        return db.rawQuery("SELECT DISTINCT t1.id ,t1.statut ,t3.nom " +
                "FROM  commandeclient t1,livraison t2,client t3,etapetournee t4 " +
                "Where t3.id =t1.idClient " +
                "and t2.id=t1.idLivraison " +
                "and t4.idLivraison = t1.idLivraison " +
                "and t2.id=" + idAgent + " " +
                "and t2.idTournee=" + idTournee + " ", null);
    }
/*******************************************Methode tournee*************************/
public Cursor getTournee(int idA,int idT) {
    String test = "SELECT DISTINCT t1.idLivraison ,t3.id as idClient,t3.nom ,t4.motifvisit,t1.id as idCommande" +
            "        FROM  commandeclient t1,Livraison t2,client t3,etapetournee t4 " +
            "        Where t3.id =t1.idClient " +
            "        and t2.id=t1.idLivraison " +
            "        and t4.idLivraison = t1.idLivraison " +
            "        and t2.idTransporteur=" + idA + " " +
            "        and t2.idTournee=" + idT;

    Log.d("abdouuuuu","Requette : "+test);



    return db.rawQuery("SELECT DISTINCT t1.idLivraison ,t3.id as idClient,t3.nom ,t4.motifvisit,t1.id as idCommande" +
            "        FROM  commandeclient t1,Livraison t2,client t3,etapetournee t4 " +
            "        Where t3.id =t1.idClient " +
            "        and t2.id=t1.idLivraison " +
            "        and t4.idLivraison = t1.idLivraison " +
            "        and t2.idTransporteur=" + idA + " " +
            "        and t2.idTournee=" + idT, null);
}





    public Cursor getTournee2(int idA,int idT) {


        String ss="SELECT e.id as idEtape, cm.id as idCommande, e.idLivraison, c.Nom, c.Prenom, e.adresse, e.motifvisit, e.statut" +
                " FROM  commandeclient cm,client c,etapetournee e " +
                " Where c.id =e.idClient " +
                " and cm.id = e.idCommande " +
                " and e.statut = 'EN COURS' " +
                "and e.idTournee = "+ idT+
                " GROUP BY e.id";
        Log.d("ressss","req = "+ss);


        return db.rawQuery("SELECT e.id as idEtape, cm.id as idCommande, e.idLivraison, c.Nom, c.Prenom, e.adresse, e.motifvisit, e.statut" +
                " FROM  commandeclient cm,client c,etapetournee e " +
                " Where c.id =e.idClient " +
                " and cm.id = e.idCommande " +
                " and e.statut = 'EN COURS' " +
                "and e.idTournee = "+ idT+
                " GROUP BY e.id", null);
    }


    public Cursor getLivraison2(int idA,int idT) {

        String reqs="SELECT e.id as idEtape, cm.id as idCommande, e.idLivraison, c.Nom, c.Prenom, e.adresse, e.motifvisit, e.statut" +
                " FROM  commandeclient cm,client c,etapetournee e " +
                " Where c.id =e.idClient " +
                " and cm.id = e.idCommande " +
                " and e.motifvisit = 'LIVRAISON CLIENT' " +
                "and e.idTournee = "+ idT+
                " GROUP BY e.id";

//        String reqs="SELECT e.id as idEtape, cm.id as idCommande, l.id as idLivraison, c.Nom, c.Prenom, e.adresse, e.motifvisit, e.statut" +
//        " FROM  commandeclient cm,Livraison l,client c,etapetournee e " +
//                " Where c.id =e.idClient " +
//                " and l.id=e.idLivraison " +
//                " and cm.id = e.idCommande " +
//                " and e.motifvisit = 'LIVRAISON CLIENT' " +
//                " and l.idTournee = "+idT +
//                " and l.idTransporteur=" + idA;

        Log.d("reqrest","req = "+reqs);



        return db.rawQuery(reqs, null);
    }


    public Cursor getCommandeLiv(int idL,String nom){
        return db.rawQuery("SELECT t1.id From commandeclient t1,Client t2 " +
                "WHERE t2.idClient =t1.idClient " +
                "and t1.idLivraison = "+idL+" " +
                "and t2.Nom ='" +nom+ "'",null);
    }
    public Cursor getDateLivraisonTournee(int idL) {
        return db.rawQuery("SELECT DISTINCT t1.DateLivraisonPrev FROM Livraison t1 " +
                "WHERE t1.id =" + idL + " ", null);
    }
    public  Cursor VerifierDB(){
        return db.rawQuery("Select idClient From Client ",null);
    }
  void   Clear(){
      db.execSQL("delete from etapetournee");
      db.execSQL("delete from vehicule");
      db.execSQL("delete from client");
      db.execSQL("delete from produit");
      db.execSQL("delete from livraison");
      db.execSQL("delete from commandeclient");
      db.execSQL("delete from pointlivraison");
      db.execSQL("delete from lignecommande ");
      db.execSQL("delete from tournee");
      db.execSQL("delete from tracestocktournee");
  }
    public Cursor getEtatCommande()  {
        return db.rawQuery("SELECT id,statut FROM commandeclient WHERE statut='Livree' ",null);

}


    public Cursor getStatutEtape()  {
        return db.rawQuery("SELECT idCommande,statut FROM etapetournee WHERE statut='Terminee' ",null);

    }

    public Cursor getLigneCmdLivree(){
        return db.rawQuery("SELECT t1.id,t1.quantiteLivree, t1.quantite_livree_reel, idProduit, idCommande FROM lignecommande t1,commandeclient t2 " +
                " WHERE t1.idCommande=t2.id " +
                "and t2.statut='Livree'",null);
    }
    public Cursor getInfoTournee(int id){

        String req = "SELECT "+KEY_ID_TOURNEE+" , "+KEY_KM_DEBUT_TOURNEE+" , "+KEY_FONT_INITIAL_RECU_TOURNEE+" FROM tournee WHERE "+KEY_ID_TOURNEE+" =" + id;

        Log.d("synchro_Tournee","req = "+req);

        return db.rawQuery(" SELECT * FROM tournee WHERE "+KEY_ID_TOURNEE+" =" + id + " ", null);
    }



    public int updateCheck(int id,int idC)
    {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.CHECK_KEY, 0);
        return db.update(DatabaseManager.TABLE_LIGNE_COMMANDE, cv, "idProduit=" + id + " and idCommande=" + idC, null);
    }

    public String  getCheck(int idP, int idC)
    {
        Cursor c= db.rawQuery("SELECT DISTINCT t1.Existe FROM lignecommande t1 " +
                "WHERE t1.idCommande ="+idC+" " +
                "AND t1.idProduit="+idP, null);

        if(c.moveToFirst())
        {
            return c.getString(c.getColumnIndex("Existe"));
        }
        else
        {
            return "2";
        }
    }


    /************************************************************ Table Localisation **********************************************************/

    public static final String TABLE_EMPLOYE_CONNECTE = "employe_connecte";
    public static final String KEY_ID_PK_EMPLOYE_CONNECTE = "idLocalisation";
    public static final String KEY_ID_EMPLOYE_CONNECTE = "employeID";
    public static final String KEY_NOM_EMPLOYE_CONNECTE = "nom_connecte";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ETAT = "etat";

    public static final String CREATE_TABLE_EMPLYE_CONNECTE = "CREATE TABLE " + TABLE_EMPLOYE_CONNECTE +
            " (" +
            " " + KEY_ID_PK_EMPLOYE_CONNECTE + " integer primary key ," +
            " " + KEY_ID_EMPLOYE_CONNECTE + " integer ," +
            " " + KEY_NOM_EMPLOYE_CONNECTE + " text ," +
            " " + KEY_LATITUDE + " text ," +
            " " + KEY_LONGITUDE + " text ," +
            " " + KEY_ETAT + " text " +
            ");";




    public boolean verifierEmployeConnecte(int idEmploye)
    {
        Cursor c= db.rawQuery("SELECT * FROM "+TABLE_EMPLOYE_CONNECTE+" WHERE "+ KEY_ID_EMPLOYE_CONNECTE+"="+idEmploye+"", null); //+" WHERE "+ KEY_EMAIL+"="+mail+" AND "+KEY_MOT_DE_PASSE+"="+mdp

        if (c.moveToFirst())
        {
            return true ;
        }
        else
        {
            return false ;
        }
    }


    public void addEmployeConnecte(int idEmploye, double latitude, double longitude,String nom, String etat)
    {
        ContentValues values2 = new ContentValues();
        values2.put(KEY_ETAT, "deconnecte");
        db.update(TABLE_EMPLOYE_CONNECTE, values2, KEY_ETAT + "='connecte'", null);


        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();

        //values.put(KEY_CODE_IDENTIFICATION, produit.getCodeProduit());

        values.put(KEY_ID_EMPLOYE_CONNECTE, idEmploye);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_NOM_EMPLOYE_CONNECTE, nom);
        values.put(KEY_ETAT, etat);

        // insert() retourne l'id du nouvel enregstrement inséré, ou -1 en cas d'erreur
        db.insert(TABLE_EMPLOYE_CONNECTE, null, values);

    }

    public int updateEmployeConnecte(int idEmploye, String etat)
    {
        ContentValues values2 = new ContentValues();
        values2.put(KEY_ETAT, "deconnecte");
        db.update(TABLE_EMPLOYE_CONNECTE, values2, KEY_ETAT + "='connecte'", null);

        ContentValues values = new ContentValues();
        values.put(KEY_ETAT, etat);

        return db.update(TABLE_EMPLOYE_CONNECTE, values, KEY_ID_EMPLOYE_CONNECTE+"="+idEmploye+"", null);
    }

    public int updateGPSEmployeConecte(double latitude, double longitude) {

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);

        return db.update(TABLE_EMPLOYE_CONNECTE, values, KEY_ETAT+"='connecte'", null);

    }

    public Cursor getEmployeConnecteGPS() {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYE_CONNECTE+" WHERE "+ KEY_ETAT+"='connecte'", null);
        if(c.moveToFirst())
        {
            return c;
        }

        // Cursor c1 = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_EMPLOYE_CONNECTE+"", null);

        return c;
    }



    //LES TESTS
    public Cursor getAllLigne(String table) {
        // sélection de tous les enregistrements de la table
        Cursor c = db.rawQuery("SELECT * FROM "+table , null);
        return c;
    }


    public static String getTodaysDate() {

         Calendar c = Calendar.getInstance();



        String todaysDate =
                String.valueOf((c.get(Calendar.YEAR)) + "-" +
                                (c.get(Calendar.MONTH) + 1) + "-" +
                                (c.get(Calendar.DAY_OF_MONTH))
                );

        return(String.valueOf(todaysDate));

    }


    public void UpdateLigneCommande(int idProduit,int qttLivree, int idCommande){


        String ss="SELECT "+KEY_QUANTITE_LIVREE_LIGNE_COMMANDE+" FROM "+TABLE_LIGNE_COMMANDE+" where idProduit="+idProduit+" and idCommande="+idCommande;
        Log.d("testttttg","req = "+ss);

        Cursor c = db.rawQuery("SELECT "+KEY_QUANTITE_LIVREE_LIGNE_COMMANDE+" FROM "+TABLE_LIGNE_COMMANDE+" where idProduit="+idProduit+" and idCommande="+idCommande,null);
        if(c.moveToFirst()){

            int qttDejaLivree = c.getInt(0);
            Log.d("testttttg","avant  = "+qttDejaLivree);
            qttDejaLivree = qttDejaLivree +qttLivree;
            Log.d("testttttg","aprés  = "+qttDejaLivree);
            Log.d("testttttg","----------------------");


            ContentValues cv = new ContentValues();
            cv.put(KEY_QUANTITE_LIVREE_REELLE_LIGNE_COMMANDE,qttLivree);
            cv.put(KEY_QUANTITE_LIVREE_LIGNE_COMMANDE,qttDejaLivree);
            db.update(TABLE_LIGNE_COMMANDE, cv, "idProduit=" + idProduit + " and idCommande=" + idCommande + "", null);
        }


    }





    /*************************************************Table Paiement****************************************/


    public static final String KEY_ID_PAIEMENT = "id";
    public static final String KEY_ID_TOURNEE_PAIEMENT= "idTournee";
    public static final String KEY_ID_COMMANDE_PAIEMENT = "idCommande";
    public static final String KEY_ID_FACTURE_PAIEMENT = "idFacture";
    public static final String KEY_ID_CLIENT_PAIEMENT = "idClient";
    public static final String KEY_ID_ADRESSE_PAIEMENT = "idAdresse";
    public static final String KEY_ID_TRANSPORTEUR_PAIEMENT = "idTransporteur";
    public static final String KEY_TYPE_ENCAISSEMENT_PAIEMENT = "typeEncaissement";
    public static final String KEY_MODE_DE_PAIEMENT_PAIEMENT = "modeDePaiement";
    public static final String KEY_DATE_DE_PAIEMENT_PREVUE_PAIEMENT = "datePaiementPrevue";
    public static final String KEY_DATE_DE_PAIEMENT_REELE_PAIEMENT = "datePaiementReele";
    public static final String KEY_STATUT_PAIEMENT = "statut";
    public static final String KEY_MONTANT_A_PAYER_PAIEMENT = "montantAPayer";
    public static final String KEY_MONTANT_ENCAISSE_PAIEMENT = "montantEncaisse";

    public static final String TABLE_PAIEMENT = "paiement";

    public static final String CREATE_TABLE_PAIEMENT = "CREATE TABLE " + TABLE_PAIEMENT +
            " (" +
            " " + KEY_ID_PAIEMENT + " INTEGER PRIMARY KEY , " +
            " " + KEY_ID_TOURNEE_PAIEMENT + " integer," +
            " " + KEY_ID_COMMANDE_PAIEMENT + " integer," +
            " " + KEY_ID_FACTURE_PAIEMENT + " integer," +
            " " + KEY_ID_CLIENT_PAIEMENT + " integer," +
            " " + KEY_ID_ADRESSE_PAIEMENT + " integer" +
            " " + KEY_ID_TRANSPORTEUR_PAIEMENT + " integer," +
            " " + KEY_TYPE_ENCAISSEMENT_PAIEMENT + " text," +
            " " + KEY_MODE_DE_PAIEMENT_PAIEMENT + " text" +
            " " + KEY_DATE_DE_PAIEMENT_PREVUE_PAIEMENT + " text" +
            " " + KEY_DATE_DE_PAIEMENT_REELE_PAIEMENT + " text" +
            " " + KEY_STATUT_PAIEMENT + " text" +
            " " + KEY_MONTANT_A_PAYER_PAIEMENT + " float" +
            " " + KEY_MONTANT_ENCAISSE_PAIEMENT + " float" +
            ")";


    public long savePaiement(Paiement paiement){

        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_PAIEMENT, paiement.getIdPaiement());
        cv.put(KEY_ID_TOURNEE_PAIEMENT, paiement.getIdTrounee());
        cv.put(KEY_ID_COMMANDE_PAIEMENT, paiement.getIdCommande());
        cv.put(KEY_ID_FACTURE_PAIEMENT, paiement.getIdFacture());
        cv.put(KEY_ID_CLIENT_PAIEMENT, paiement.getIdClient());
        cv.put(KEY_ID_ADRESSE_PAIEMENT, paiement.getIdAdresse());
        cv.put(KEY_ID_TRANSPORTEUR_PAIEMENT, paiement.getIdTransporteur());
        cv.put(KEY_TYPE_ENCAISSEMENT_PAIEMENT, paiement.getTypeEncaissement());
        cv.put(KEY_MODE_DE_PAIEMENT_PAIEMENT, paiement.getModeDePaiement());
        cv.put(KEY_DATE_DE_PAIEMENT_PREVUE_PAIEMENT, paiement.getDatePaiementPrevue());
        cv.put(KEY_DATE_DE_PAIEMENT_REELE_PAIEMENT, paiement.getDatePaiementReel());
        cv.put(KEY_STATUT_PAIEMENT, paiement.getStatut());
        cv.put(KEY_MONTANT_A_PAYER_PAIEMENT, paiement.getMontantAPayer());
        cv.put(KEY_MONTANT_ENCAISSE_PAIEMENT, paiement.getMontantEncaisse());

        return db.insert(DatabaseManager.TABLE_PAIEMENT, null, cv);
    }


    /************************************ Methode *************************************************/


    public Cursor getPaiement(int idA,int idT) {

        String reqs="SELECT e.id as idEtape, cm.id as idCommande, e.idLivraison, c.Nom, c.Prenom, e.adresse, e.motifvisit, e.statut" +
                " FROM  commandeclient cm,client c,etapetournee e " +
                " Where c.id =e.idClient " +
                " and cm.id = e.idCommande " +
                " and e.motifvisit = 'ENCAISSEMENT' " +
                "and e.idTournee = "+ idT+
                " GROUP BY e.id";

        Log.d("reqrest", "req = " + reqs);


        return db.rawQuery(reqs, null);
    }

/*
    public Long savePoint(CommandeClient c) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_POINT_LIVRAISON,c.getIdPointLivr());
        cv.put(DatabaseManager.KEY_ID_CLIENT_POINT_LIVRAISON,c.getIdClient());
        return db.insert(DatabaseManager.TABLE_POINT_LIVRAISON, null, cv);
    }

*/



}