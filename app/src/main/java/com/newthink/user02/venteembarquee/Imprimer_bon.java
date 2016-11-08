package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by User02 on 24/04/2016.
 */
public class Imprimer_bon  extends Activity {
    EditText clientText,cmdEditText, livrEditText, dateLivraisonEditText, CommClient, CommLivreur;
    int idProd, QteComm,QteLiv;String Design;

    ArrayList<Produit> myList = new ArrayList<Produit>();
    ListView list;
    Context context = Imprimer_bon.this;
    public DatabaseManager DB = new DatabaseManager(this);

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maquette_livraison_layout);
        /*Affichage de l'agent connecte*/
        /*****************/
        //loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        /****************/
        // we are going to have three buttons for specific functions
        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);
        // text label and input box
        myLabel = (TextView) findViewById(R.id.label);
        clientText = (EditText) findViewById(R.id.Client);
        cmdEditText = (EditText) findViewById(R.id.Cmd);
        livrEditText = (EditText) findViewById(R.id.Livrais);
        dateLivraisonEditText = (EditText) findViewById(R.id.DateLiv);
        list = (ListView) findViewById(R.id.ListClI);
           /*  Afficher Information livraiosn */
        Intent intent = this.getIntent();
        Bundle bb = intent.getExtras();
        final String n= bb.getString("loginselectionne");
        final String Clie = bb.getString("NomClient");
        com = bb.getInt("Commande");
        liv = bb.getInt("Livraison");
        final String d = bb.getString("DateLivraison");
       // loginDisplay.setText(n);
        clientText.setText(Clie);
        cmdEditText.setText(String.valueOf(com));
        livrEditText.setText(String.valueOf(liv));
        dateLivraisonEditText.setText(d);
        CommClient = (EditText) findViewById(R.id.ComC);
        CommLivreur =(EditText) findViewById(R.id.Comm);
        // open bluetooth connection
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // send data typed by the user to be printed
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // close bluetooth connection
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        DB.open();
        refreshListProd();
        DB.close();
    }
    public void refreshListProd() {
        myList.clear();
        Cursor c = DB.getProdLiv(liv,com);
        if (c.moveToFirst()) {
            do {

                idProd= c.getInt(c.getColumnIndex(DatabaseManager.KEY_ID_PRODUIT));
                Design = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                QteComm = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE));
                QteLiv = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_LIVREE_LIGNE_COMMANDE));
                getDataInList(myList, idProd, Design, QteComm, QteLiv);
            } while (c.moveToNext());
            c.close();
        }
        list.setAdapter(new MyBaseAdapterProduitLivr(context, myList));
    }
    private void getDataInList(ArrayList<Produit> li, int p, String a, int b, int c) {
        // Create a new object for each list item
        Produit ld = new Produit();
        ld.setIdProduit(p);
        ld.setDesignation(a);
        ld.setQuantiteCmd(b);
        ld.setQuantiteLivree(c);
        // Add this object into the ArrayList myList
        li.add(ld);
    }
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
    void sendData() throws IOException {
        Log.d("impression","avant Try impression");
        try {
            Log.d("impression", "start impression");

            // the text typed by the user
            String msgClient = "Client :"+clientText.getText().toString();
            Log.d("impressioSend", "1er instruction : "+msgClient);
            msgClient+="\n";
            String msgCommande = "Commande :"+cmdEditText.getText().toString();
            Log.d("impressioSend", "2er instruction : "+ msgCommande);
            msgCommande+="\n";
            String msgLivraison= "Livraison :"+livrEditText.getText().toString();
            Log.d("impressioSend", "3er instruction : "+msgLivraison);
            msgLivraison+="\n";
            String msgDate=  "Date Livraison :"+dateLivraisonEditText.getText().toString();
            Log.d("impressioSend", "4er instruction : "+msgDate);
            msgDate+="\n";
            String msgCommC= "Commentaire Client :"+CommClient.getText().toString();
            Log.d("impressioSend", "5er instruction : "+msgCommC);
            msgCommC+="\n";
            String msgCommL= "Commentaire Livreur :"+CommLivreur.getText().toString();
            Log.d("impressioSend", "6er instruction :"+msgCommL);
            msgCommL+="\n";
            msgCommL+="------------------------------\n";
            String aff = "Designation    QteComm    QteLiv ";
            aff+="------------------------------\n";
            mmOutputStream.write(msgClient.getBytes());
            Log.d("impressioSend", "7er instruction :");

            mmOutputStream.write(msgCommande.getBytes());
            Log.d("impressioSend", "8er instruction :");
            mmOutputStream.write(msgLivraison.getBytes());
            Log.d("impressioSend", "9er instruction :");
            mmOutputStream.write(msgDate.getBytes());
            Log.d("impressioSend", "10er instruction :");
            mmOutputStream.write(msgCommC.getBytes());
            Log.d("impressioSend", "11er instruction :" );
            mmOutputStream.write(msgCommL.getBytes());
            Log.d("impressioSend", "12er instruction :" );
            mmOutputStream.write(aff.getBytes());

            Log.d("impressioSend", "13er instruction :" );

            String prod;
            DB.open();
            String test;int i;
            Cursor c = DB.getProdLiv(liv,com);
            if (c.moveToFirst()) {
                do {
                    String Designn = c.getString(c.getColumnIndex(DatabaseManager.KEY_DESIGNATION_PRODUIT));
                    if(Designn.length()>4){
                        test=Designn.substring(0,4);
                    }else{
                        test=Designn;
                    }
                    int QteCommm = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_COMMANDEE_LIGNE_COMMANDE));
                    int QteLivv = c.getInt(c.getColumnIndex(DatabaseManager.KEY_QUANTITE_LIVREE_LIGNE_COMMANDE));
                    prod =test + "          " + QteCommm + "         " + QteLivv+"\n";
                    mmOutputStream.write(prod.getBytes());
                } while (c.moveToNext());
                c.close();
            }
            DB.close();
            String sign= "Signature";
            sign+="\n";
            mmOutputStream.write(sign.getBytes());

            Log.d("impressioSend", "14er instruction :");
            //tell the user data were sent
            myLabel.setText("Data sent.");



        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("impression","Aprés Try impression");
    }

    // this will find a bluetooth printer device
    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("BT-SPP")) {
                        mmDevice = device;
                        myLabel.setText("Bluetooth device found.");
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
    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */

    /*
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

    */

    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
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
