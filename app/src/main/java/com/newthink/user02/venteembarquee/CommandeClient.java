package com.newthink.user02.venteembarquee;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User02 on 30/03/2016.
 */
public class CommandeClient {
    int idClient;
    String NomClient;
    String PrenomClient;
    String Segement;
    String Email;
    String Telephone;
    String DateCreation;

    public CommandeClient() { }

    public void setIdClient(int idClient) { this.idClient = idClient; }

    public void setNomClient(String NomClient) { this.NomClient = NomClient; }

    public void setPrenomClient(String PrenomClient) { this.PrenomClient = PrenomClient; }

    public void setSegementClient(String Segement) { this.Segement = Segement; }

    public void setEmailClient(String Email) { this.Email = Email; }

    public void setTelephoneClient(String Telephone) { this.Telephone = Telephone; }

    public void setDateCreationClient(String DateCreation) { this.DateCreation = DateCreation; }

    public int getIdClient() { return idClient; }

    public String getNomClient() {return NomClient; }

    public String getPrenomClient() {return PrenomClient; }

    public String getTelephoneClient() {return Telephone;}


    int idCommande;
    String DateCreationC;
    double TotalPrixHT;
    double TotalPrixTTC;
    String Statut;
    String EtatCommande;

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public static String getDateCreationC() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String DateCreationC = sdfDate.format(now);
        return DateCreationC;
    }

    public String getEtatCommande() {
        return EtatCommande;
    }

    public void setEtatCommande(String etatCommande) {
        EtatCommande = etatCommande;
    }

    public void setStatut(String statut) {
        Statut = statut;
    }
    public String getStatut() {
        return Statut;
    }
    public void setDateCreationC(String dateCreationC) {
        DateCreationC = dateCreationC;
    }
    /* etaape tournee */
    int IdEtape;
    String MotifVisite;
    String adresse;
    int NumEtape;

    public int getIdEtape() {
        return IdEtape;
    }

    public void setIdEtape(int IdEtape) {
        this.IdEtape = IdEtape;
    }

    public int getNumEtape() {
        return NumEtape;
    }

    public void setNumEtape(int NumEtape) {
        this.NumEtape = NumEtape;
    }
    public String getMotifVisite() {
        return MotifVisite;
    }

    public void setMotifVisite(String motifVisite) {
        MotifVisite = motifVisite;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    /*livraison*/
    int IdLivraison;
    String DateLivraison;

    String EtatLi;

    public String getEtatLi() {
        return EtatLi;
    }

    public void setEtatLi(String etatli) {
        EtatLi = etatli;
    }


    public int getIdLivraison() {return IdLivraison;}

    public void setIdLivraison(int idLivraison) {
        IdLivraison = idLivraison;
    }
    public static String getDateLivraison() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String DateLivraison = sdfDate.format(now);
        return DateLivraison;
    }
    public void setDateLivraison(String dateLivraison) {
        DateLivraison = dateLivraison;
    }

    int idPointLivr;

    public int getIdPointLivr() {
        return idPointLivr;
    }

    public void setIdPointLivr(int idPointLivr) {
        this.idPointLivr = idPointLivr;
    }
}

