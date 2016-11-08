package com.newthink.user02.venteembarquee;

/**
 * Created by Salim on 16/10/2016.
 */
public class Paiement {



    int idPaiement,idTournee,idCommande,idFacture,idClient, idAdresse, idTransporteur;
    String typeEncaissement, modeDePaiement,datePaiementPrevue,datePaiementReel,statut;
    float montantAPayer,montantEncaisse;

    public Paiement(){

    }

    /*Getters*/

    public int getIdPaiement() {
        return idPaiement;
    }
    public int getIdTrounee() {
        return idTournee;
    }
    public int getIdCommande() {
        return idCommande;
    }
    public int getIdFacture() {
        return idFacture;
    }
    public int getIdClient() {

        return idClient;
    }
    public int getIdAdresse() {

        return idAdresse;
    }
    public int getIdTransporteur() {

        return idTransporteur;
    }
    public String getTypeEncaissement() {

        return typeEncaissement;
    }
    public String getModeDePaiement() {

        return modeDePaiement;
    }
    public String getDatePaiementPrevue()
    {
        return datePaiementPrevue;
    }
    public String getDatePaiementReel() {
        return datePaiementReel;
    }
    public String getStatut() {

        return statut;
    }
    public float getMontantAPayer() {

        return montantAPayer;
    }
    public float getMontantEncaisse() {

        return montantEncaisse;
    }




    /*Setters*/

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }


    public void setIdTrounee(int idTournee) {
        this.idTournee = idTournee;
    }
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
    public void setIdAdresse(int idAdresse) {
        this.idAdresse = idAdresse;
    }
    public void setIdTransporteur(int idTransporteur) {
        this.idTransporteur = idTransporteur;
    }
    public void setTypeEncaissement(String typeEncaissement) {
        this.typeEncaissement = typeEncaissement;
    }
    public void setModeDePaiement(String modeDePaiement) {
        this.modeDePaiement = modeDePaiement;
    }
    public void setDatePaiementPrevue(String datePaiementPrevue)
    {
        this.datePaiementPrevue = datePaiementPrevue;
    }
    public void setDatePaiementReel(String datePaiementReel) {
        this.datePaiementReel = datePaiementReel;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setMontantAPayer(float montantAPayer) {
        this.montantAPayer = montantAPayer;
    }
    public void setMontantEncaisse(float montantEncaisse) {
        this.montantEncaisse = montantEncaisse;
    }


}
