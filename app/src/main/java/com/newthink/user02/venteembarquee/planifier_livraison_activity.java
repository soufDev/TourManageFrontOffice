package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

import static com.newthink.user02.venteembarquee.R.layout.dialogue_layout;
import static com.newthink.user02.venteembarquee.R.layout.plannifier_livraison_layout;

/**
 * Created by User02 on 13/03/2016.
 */
public class planifier_livraison_activity extends Activity {
    /*Entrer l'URL*/
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    Button btnAcceuil,btndeconnexion;
    Button buttonAnnuler,buttonImpr;
    int ll,l ,commande,cc;
     boolean con1,con2;
    EditText qtelivreEditText,qteestimeeEditText;
    public static String n;
    int idAg,idTou;
     public int StockFinalEstime,StockFinalReel,SES,SER;
    TextView clientText;

    ArrayList<Produit> myList = new ArrayList<Produit>();
    ArrayList<Produit> myList_tmp = new ArrayList<Produit>();
    ListView list;
    Context context = planifier_livraison_activity.this;
     public DatabaseManager DB= new DatabaseManager(this);
    EditText cmdEditText,livrEditText,dateLivraisonEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.plannifier_livraison_layout);
          /*Affichage de l'agent connecté*/
        /*****************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        idAg= b.getInt("id_agent");
        loginDisplay.setText(n);
        /****************/
        clientText = (TextView) findViewById(R.id.clients);
      cmdEditText = (EditText) findViewById(R.id.CmdEditText);
      livrEditText = (EditText) findViewById(R.id.livrEditText);
      dateLivraisonEditText = (EditText) findViewById(R.id.DateLivEditText);
      /* afficher la liste des clients */
      list = (ListView) findViewById(R.id.listProduitsL);
      /**********************************le header*************************/
      btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
      btnAcceuil.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(planifier_livraison_activity.this, acceiul_activity.class);
              intent.putExtra("loginselectionne",n);
              intent.putExtra("id_agent",idAg);
              startActivity(intent);
          }
      });
      btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
      btndeconnexion.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent3 = new Intent(planifier_livraison_activity.this, MainActivity.class);
              intent3.putExtra("loginselectionne",n);
              intent3.putExtra("id_agent",idAg);
              startActivity(intent3);

          }
      });
      buttonAnnuler = (Button) findViewById(R.id.buttonAnnuler);
      buttonAnnuler.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent3 = new Intent(planifier_livraison_activity.this, tournee_du_jour_activity.class);
              intent3.putExtra("loginselectionne",n);
              intent3.putExtra("id_agent",idAg);
              startActivity(intent3);

          }
      });
        DB.open();
        Cursor c2=DB.getIdTournee(idAg);
        if(c2.moveToFirst()){
            idTou = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
        }

        DB.close();
      /*********************************************************************/

        //Afficher Information livraison venant de la classe tournee_du_jour_activity
        Intent i= this.getIntent();
        Bundle bb = i.getExtras();
        final String nn = bb.getString("Client1");
         cc = bb.getInt("Commande1");
        ll = bb.getInt("Livraison1");
        final String dd = bb.getString("DateLivraison1");
       con1=bb.getBoolean("Condition1");
       //  Afficher Information livraison venant de la classe livraison_activity
        Intent intent = this.getIntent();
        Bundle bu = intent.getExtras();
        final String Client = bu.getString("Client2");
        commande = bu.getInt("Commande2");
         l = bu.getInt("Livraison2");
        final String d = bu.getString("DateLivraison2");
         con2= bu.getBoolean("Condition2");
        if(con1){
        clientText.setText(nn);
        cmdEditText.setText(String.valueOf(cc));
        livrEditText.setText(String.valueOf(ll));
        dateLivraisonEditText.setText(dd);
            DB.open();
            refreshListProd(ll, cc);
            DB.close();
            UpdateItem(ll, cc);

        }else if (con2) {
            clientText.setText(Client);
            cmdEditText.setText(String.valueOf(commande));
            livrEditText.setText(String.valueOf(l));
            dateLivraisonEditText.setText(d);
            DB.open();
            refreshListProd(l, commande);
            DB.close();
            UpdateItem(l, commande);

        }
        cloneList();

  }
    public void refreshListProd(int liv,int com) {
        myList.clear();
        Cursor c = DB.getProdLiv(liv,com);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                String n = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                int i = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE));
                int v =c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_LIVREE_LIGNE_COMMANDE));
                getDataInList(myList, id,n,i,v,com);
            } while (c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterProduitLivr(context, myList));
    }

    public void refreshListProd2() {

        list.setAdapter(new MyBaseAdapterProduitLivr(context, myList));
    }

    public void cloneList() {

        for (int i=0;i<myList.size();i++){
            Produit p= new Produit();
            p.setStockEncours(myList.get(i).getStockEncours());
            p.setDesignation(myList.get(i).getDesignation());
            p.setQuantiteCmd(myList.get(i).getQuantiteCmd());
            p.setQuantiteLivree(myList.get(i).getQuantiteLivree());
            myList_tmp.add(p);
        }
    }



    private void getDataInList(ArrayList<Produit> li, int p, String a, int b,int  c, int com) {
        // Create a new object for each list item
        Produit ld = new Produit();
        ld.setIdProduit(p);
        ld.setDesignation(a);
        ld.setQuantiteCmd(b);
        ld.setQuantiteLivree(c);
        DB.open();
        ld.check=DB.getCheck(p,com);
        DB.close();
        // Add this object into the ArrayList myList
        li.add(ld);
    }
    public void UpdateItem(final int liv , final int com) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {


                for (int i=0;i<myList_tmp.size();i++){
                    Log.d("liste_tmp","disg = "+myList_tmp.get(i).getDesignation()+" QTTC = "+myList_tmp.get(i).getQuantiteCmd()+" QTTL = "+myList_tmp.get(i).getQuantiteLivree());
                }

                final int index=position;
                final Produit a = myList_tmp.get(index);
                final int qc = a.getQuantiteCmd();
                final int ql = a.getQuantiteLivree();
                final int idp = a.getIdProduit();
                if (qc != ql) {
                    int motifdecart = qc - ql;
                    Toast.makeText(context, "le motif d'ecart :" + motifdecart, Toast.LENGTH_LONG).show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = planifier_livraison_activity.this.getLayoutInflater();
                builder.setTitle("Quantité livrée");
                builder.setMessage(" Introduit la quantité livrée ");
                final View vw = inflater.inflate(R.layout.dialogue_layout, null);
                builder.setView(vw);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int QTE=0;
                        int qteReel=0;
                        qtelivreEditText = (EditText) vw.findViewById(R.id.quantite);
                        final String QUANTITE = qtelivreEditText.getText().toString();
                        if (QUANTITE.equals("")) {
                            Toast.makeText(context, "Désolé ! Veuillez saisir la quantitéé ", Toast.LENGTH_LONG).show();
                        } else {
                            QTE = Integer.parseInt(QUANTITE);
                            DB.open();
                            qteReel = QTE + ql;
                            if (qteReel > qc) {

                                Toast.makeText(context, "Désolé !  La quantité saisie est superieure à la quantité commandé " +
                                        "Veuillez ressaisir la quantité !!  ", Toast.LENGTH_LONG).show();
                            } else {
                             //   DB.UpdateQTE(qteReel, idp, com);
                                myList.get(index).check="0";
                                //DB.updateCheck(idp,com);
                                myList.get(index).setQuantiteLivree(qteReel);

                                Toast.makeText(context, "quantité livrée changé :" + QTE, Toast.LENGTH_LONG).show();
                            }
                        }
                        refreshListProd2();

                        DB.close();
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.show();
            }
        });
    }

    public void OnClickValider(View args){

        boolean existe=false;
        int i=0;
        while (existe== false && i<myList.size())
        {
            //Log.d("mylist_chuck","idP : "+ myList.get(i).getIdProduit()+" designation : "+ myList.get(i).getDesignation()+ " chuck : "+myList.get(i).chuck);
            if(myList.get(i).check.equals("-1"))
            {
                existe= true;
            }
            else{
                i++;
            }
        }
        if(existe == false)
        {
            DB.open();
            if(con1){

                for(int ii=0;ii<myList.size();ii++) {
                    Cursor c = DB.getStockIn(myList.get(ii).getIdProduit(), idTou);
                    if (c.moveToFirst()) {
                        SER = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                        SES = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE));
                    }
                    int qteReel=myList.get(ii).getQuantiteLivree();
                    int qc=myList.get(ii).getQuantiteLivree();

                    StockFinalReel = (SER - qteReel);
                    StockFinalEstime = (SES - qc);

                    Log.d("updateStock","S-I-R = "+SER+" S-I-E = "+SES+"/ S-F-R = "+StockFinalReel+" S-F-E = "+StockFinalEstime);

                    DB.UpdateLigneCommande(myList.get(ii).getIdProduit(), qteReel, cc);
                    DB.UpadteStock(myList.get(ii).getIdProduit(), StockFinalEstime, StockFinalReel, idTou);
                }


                DB.UpdateEtatCommande("Livree", cc);
                DB.UpdateEtatEtape("Terminee", cc);

                Cursor x=DB.getAllLigne(DB.TABLE_LIGNE_COMMANDE);
                x.moveToFirst();
                do{
                    Log.d("lignecommandeLivraison","id = "+x.getString(x.getColumnIndex("id"))+" QuantiteCmd = "+x.getString(x.getColumnIndex("QuantiteCmd"))+" quantiteLivree = "+x.getString(x.getColumnIndex("quantiteLivree"))+" quantite_livree_reel = "+x.getString(x.getColumnIndex("quantite_livree_reel")));
                }while(x.moveToNext());

            }else if (con2){

                for(int ii=0;ii<myList.size();ii++) {
                    Cursor c = DB.getStockIn(myList.get(ii).getIdProduit(), idTou);
                    if (c.moveToFirst()) {
                        SER = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                        SES = c.getInt(c.getColumnIndex(DatabaseManager.KEY_STOCK_INITIAL_ESTIME_STOCK_TOURNEE));
                    }
                    int qteReel=myList.get(ii).getQuantiteLivree();
                    int qc=myList.get(ii).getQuantiteLivree();

                    StockFinalReel = (SER - qteReel);
                    StockFinalEstime = (SES - qc);

                    Log.d("updateStock","S-I-R = "+SER+" S-I-E = "+SES+"/ S-F-R = "+StockFinalReel+" S-F-E = "+StockFinalEstime);

                    DB.UpdateLigneCommande(myList.get(ii).getIdProduit(), qteReel, commande);
                    DB.UpadteStock(myList.get(ii).getIdProduit(), StockFinalEstime, StockFinalReel, idTou);
                }


                DB.UpdateEtatCommande("Livree", commande);
                DB.UpdateEtatEtape("Terminee", l);

                Cursor x=DB.getAllLigne(DB.TABLE_LIGNE_COMMANDE);
                x.moveToFirst();
                do{
                    Log.d("lignecommandeLivraison","id = "+x.getString(x.getColumnIndex("id"))+" QuantiteCmd = "+x.getString(x.getColumnIndex("QuantiteCmd"))+" quantiteLivree = "+x.getString(x.getColumnIndex("quantiteLivree"))+" quantite_livree_reel = "+x.getString(x.getColumnIndex("quantite_livree_reel")));
                }while(x.moveToNext());
            }



            Toast.makeText(context, "N°"+commande, Toast.LENGTH_LONG).show();
            // DB.UpdateEtat("Livree",l);
            DB.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Bon de livraison");
            builder.setMessage(" Voulez-vous imprimmer le bon de livraison ?\n");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {


                    try {

                        findBT();
                        openBT();
                        sendData();
                        closeBT();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context, "Livraison effectuée", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(planifier_livraison_activity.this,livraison_activity.class);
                    intent.putExtra("loginselectionne",n);
                    intent.putExtra("id_agent",idAg);
                    Log.d("idAgentPlanifi", " id = " + idAg);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton("Non", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {


                    Toast.makeText(context, "Livraison effectuée", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(planifier_livraison_activity.this,livraison_activity.class);
                    intent.putExtra("loginselectionne",n);
                    intent.putExtra("id_agent",idAg);
                    Log.d("idAgentPlanifi", " id = " + idAg);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();


        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Item");
            builder.setMessage(" Introduit la quantité livrée du produit \n" + myList.get(i).getDesignation());
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.show();
        }
    }



    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel,loginDisplay,CmdTextView;

    // will enable user to enter any text to be printed
    EditText myTextbox,myTextbox2;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    public int com,liv;



    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // this will send text data to be printed by the bluetooth printer
    @TargetApi(Build.VERSION_CODES.FROYO)
    void sendData() throws IOException {

        try {


            String bon_livraison="";

            String msgClient = "            Client :  "+clientText.getText().toString();



            for(int i=msgClient.length();i<69;i++){
                msgClient+=" ";
            }
            msgClient+="\n\n";
            bon_livraison+=msgClient;


            String msgCommande = "            Commande :  "+cmdEditText.getText().toString();
            for(int i=msgCommande.length();i<69;i++){
                msgCommande+=" ";
            }
            msgCommande+="\n\n";

            bon_livraison+=msgCommande;

            String msgLivraison= "            Livraison :  "+livrEditText.getText().toString();
            for(int i=msgLivraison.length();i<69;i++){
                msgLivraison+=" ";
            }
            msgLivraison+="\n\n";

            bon_livraison+=msgLivraison;

            Calendar calendar = Calendar.getInstance();

            int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
            int Min=calendar.get(Calendar.MINUTE);

            String heur="";
            if(Min<10){
                heur="0"+Hr24;
            }else{
                heur=""+Hr24;
            }

            String minute="";
            if(Min<10){
                minute="0"+Min;
            }else{
                minute=""+Min;
            }

            String msgDate=  "            Date Livraison :  "+dateLivraisonEditText.getText().toString()+"   "+ heur+":"+minute;
            for(int i=msgDate.length();i<69;i++){
                msgDate+=" ";
            }
            msgDate+="\n\n";

            bon_livraison+=msgDate;


            String msgCommL="";
            for(int i=0;i<69;i++){
                msgCommL+="-";
            }
            msgCommL+="\n";
            bon_livraison+=msgCommL;


            String aff = "                Designation      |   QteComm   |   QteLiv";

            for(int i=aff.length();i<69;i++){
                aff+=" ";
            }
            aff+="\n";
            bon_livraison+=aff;
             msgCommL="";
            for(int i=0;i<69;i++){
                msgCommL+="-";
            }
            msgCommL+="\n\n";
            bon_livraison+=msgCommL;



            String prod;
            DB.open();
            String test;int i;
            Cursor c = DB.getProdLiv(Integer.parseInt(livrEditText.getText().toString()),Integer.parseInt(cmdEditText.getText().toString()));
            if (c.moveToFirst()) {
                do {
                    String Designn = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                    if(Designn.length()>16){
                        test=Designn.substring(0,15);
                    }else{
                        test=Designn;
                    }


                    prod ="               "+test;
                    for(int j=prod.length();j<37;j++){
                        prod+=" ";
                    }

                    int QteCommm = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE));
                    prod+=QteCommm;
                    for(int j=prod.length();j<52;j++){
                        prod+=" ";
                    }
                    int QteLivv = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_LIVREE_LIGNE_COMMANDE));

                    prod+=QteLivv;

                    for(int i8=prod.length();i8<69;i8++){
                        prod+=" ";
                    }
                    prod+="\n\n";
                    bon_livraison+=prod;

                } while (c.moveToNext());
                c.close();
            }
            DB.close();


            msgCommL="";
            for(int i2=0;i2<69;i2++){
                msgCommL+="-";
            }
            msgCommL+="\n\n\n";
            bon_livraison+=msgCommL;

            String sign= "               Signature";
            for(int i8=sign.length();i8<69;i8++){
                sign+=" ";
            }

            sign+="\n\n";
            bon_livraison+=sign;


            String blac="";
            for(int i8=0;i8<300;i8++){
                blac+=" ";
            }
            blac+="\n\n";
            bon_livraison+=blac;

            mmOutputStream.write(bon_livraison.getBytes());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // this will find a bluetooth printer device
    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null) {

                Toast.makeText(context, "Aucun adaptateur Bluetooth disponible.", Toast.LENGTH_SHORT).show();

            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    if (device.getName().equals("BT-SPP")) {
                        mmDevice = device;

                        Toast.makeText(context, "périphérique Bluetooth trouvé.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            Log.d("impressioOpen", "1er instruction");

            // Standard SerialPortService ID 00001101-0000-1000-8000-00805F9B34FB
            // UUID uuid = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //      UUID uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            Log.d("impressioOpen", "2er instruction");
            mmSocket.connect();
            Log.d("impressioOpen", "3er instruction");
            mmOutputStream = mmSocket.getOutputStream();
            Log.d("impressioOpen", "4er instruction");
            mmInputStream = mmSocket.getInputStream();
            Log.d("impressioOpen", "5er instruction");
            beginListenForData();
            Log.d("impressioOpen", "6er instruction");
            myLabel.setText("Bluetooth Opened");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}