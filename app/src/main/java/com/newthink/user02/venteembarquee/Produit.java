package com.newthink.user02.venteembarquee;

/**
 * Created by User02 on 24/03/2016.
 */
public class Produit {
     int idProduit;
     String Designation;
     String CodeIdentification;
     int PrixAchatAppliquee;
     int Cump;
     int Reference;
     String check;
//    String CodeMesure;
/*Sertters*/
    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public void setCodeIdentification(String codeIdentification) {
        CodeIdentification = codeIdentification;
    }

    public void setPrixAchatAppliquee(int prixAchatAppliquee) {
        PrixAchatAppliquee = prixAchatAppliquee;
    }

    public void setCump(int cump) {
        Cump = cump;
    }

    public void setReference(int reference) {
        Reference = reference;
    }

  /* public void setCodeMesure(String codeMesure) {
        CodeMesure = codeMesure;
    }*/

    public Produit() {

    }
/*Getters*/
    public int getIdProduit() {
        return idProduit;
    }

    public String getDesignation() {
        return Designation;
    }

    public String getCodeIdentification() {
        return CodeIdentification;
    }

    public int getPrixAchatAppliquee() {
        return PrixAchatAppliquee;
    }

    public int getCump() {
        return Cump;
    }

    public int getReference() {
        return Reference;
    }

  /*  public String getCodeMesure() {
        return CodeMesure;
    }*/

    public   int idStock;
    public   int StockInitialeEstime;
    public   int StockInitialeReel;
    public   int StockEncours;
    public   int StockFinalEstime;
    public   int StockFinalReel;

    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int idLivraison;



    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }


    public void setStockInitialeEstime(int stockInitialeEstime) {
        StockInitialeEstime= stockInitialeEstime;
    }

    public void setStockInitialeReel(int stockInitialeReel) {
        StockInitialeReel = stockInitialeReel;
    }


    public void setStockEncours(int stockEncours) {
        StockEncours = stockEncours;
    }

    public void setStockFinalEstime(int stockFinalEstime) {
        StockFinalEstime = stockFinalEstime;
    }

    public void setStockFinalReel(int stockFinalReel) {
        StockFinalReel = stockFinalReel;
    }

    public int getIdStock() {
        return idStock;
    }

    public int  getStockInitialeEstime() {
        return StockInitialeEstime;
    }

    public int getStockInitialeReel() {return StockInitialeReel;}

    public int getStockEncours() {
        return StockEncours;
    }

    public int getStockFinalEstime() {
        return StockFinalEstime;
    }

    public int getStockFinalReel() {
        return StockFinalReel;
    }

    int idLigneCommande;
    int QuantiteCmd;
    int  QuantiteLivree;
    int TauxRemise;
    int CodeMesure;
    public int getIdLigneCommande() {
        return idLigneCommande;
    }
    public void setIdLigneCommande(int idLigneCommande) {
        this.idLigneCommande = idLigneCommande;
    }

    public int getQuantiteCmd() {
        return QuantiteCmd;
    }
    public void setQuantiteCmd(int  quantiteCmd) {
        QuantiteCmd = quantiteCmd;
    }
    public int getQuantiteLivree() {
        return QuantiteLivree;
    }
    public void setQuantiteLivree(int quantiteLivree) {
        QuantiteLivree = quantiteLivree;
    }

}
