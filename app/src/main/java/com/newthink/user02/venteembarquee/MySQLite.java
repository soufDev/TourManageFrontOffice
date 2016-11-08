package com.newthink.user02.venteembarquee;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TandS.sqlite";
    private  String DATABASE_PATH ;
    private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "/data/data/com.newthink.user02.embarqueee/databases";

    public MySQLite(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Création de la base de données
        // on exécute ici les requêtes de création des tables

        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_ETAPE_TOURNEE);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_AGENT);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_VEHICULE);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_TOURNEE);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_PRODUIT);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_TRACE_STOCK_TOURNEE);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_CLIENT);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_COMMANDE_CLIENT);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_LIVRAISON);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_LIGNE_COMMANDE);
       // sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_SECTEUR);
        sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_POINT_LIVRAISON);
          sqLiteDatabase.execSQL(DatabaseManager.CREATE_TABLE_EMPLYE_CONNECTE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Mise à jour de la base de données
        // méthode appelée sur incrémentation de DATABASE_VERSION
        // on peut faire ce qu'on veut ici, comme recréer la base :
        onCreate(sqLiteDatabase);
    }

} // Class MySQLite