package com.newthink.user02.venteembarquee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User02 on 20/03/2016.
 */
public class agent {
    int id;
    String login;
    String pass;

  public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }



    public agent() {
    super();
    }

    public agent(String s, String soriya) {
        this.login = login;
       // this.id = id;
        this.pass = pass;
    }

    public int idTournee;
    public String DateDebut;
    public String DateFin;
    public String Statut;
    public int CachRecu;
    public int MotifEcartCaisse;
    public int MotifEcartStock;
    public double AutreMotifEcartCaisse;
    public double AutreMotifEcartStock;


    /* ********************************************setters**********************************/
    public void setIdTournee(int idTournee) {
        this.idTournee = idTournee;
    }

    public void setStatut(String statut) {
        Statut = statut;
    }

    public void setCachRecu(int cachRecu) {
        CachRecu = cachRecu;
    }

    /* ********************************************getters**********************************/
    public int getIdTournee() {
        return idTournee;
    }


    public static String getDateDebut() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String DateDebut = sdfDate.format(now);
        return DateDebut;
    }
    public void setDateDebut(String dateDebut) {
        DateDebut = dateDebut;
    }
    public static String getDateFin() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String DateFin = sdfDate.format(now);
        return DateFin;
    }
    public void setDateFin(String dateFin) {
        DateFin = dateFin;
    }
    public String getStatut() {
        return Statut;
    }

    public int getCachRecu() {
        return CachRecu;

    }
    int idVehicule;
    int Matricule;
    int KMDebut;
    int KMFin;
    int CapaciteStockage;

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getMatricule() {
        return Matricule;
    }

    public void setMatricule(int matricule) {
        Matricule = matricule;
    }

    public int getKMDebut() {
        return KMDebut;
    }

    public void setKMDebut(int KMDebut) {
        this.KMDebut = KMDebut;
    }

    public int getKMFin() {
        return KMFin;
    }

    public void setKMFin(int KMFin) {
        this.KMFin = KMFin;
    }

    public int getCapaciteStockage() {
        return CapaciteStockage;
    }

    public void setCapaciteStockage(int capaciteStockage) {
        CapaciteStockage = capaciteStockage;
    }
}
