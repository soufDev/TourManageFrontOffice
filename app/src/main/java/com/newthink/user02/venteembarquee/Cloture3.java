package com.newthink.user02.venteembarquee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by moose on 06/10/2016.
 */
public class Cloture3  extends Activity {

    public static final int SIGNATURE_ACTIVITY = 1;
    public static String n;
    Button btnAcceuil,btndeconnexion, btncloturer;
    TabHost tabHost;
    int i=0,j=0;
    // Context context = tournee_activity.this;
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    public  int KMF;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometragedebut,kilometragefin;
    String cashf,DateFinT;
    ListView list;
    int cashInit=0;
    TextView Date_Debut,DateFin, EcartStock, EcartCaisse;
    Context context = Cloture3.this;

    int Ecare_caisse,Ecare_Stock;

    //Signature Outils

    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    public static String tempDir;
    public int count = 1;
    public String current = null;
    private Bitmap mBitmap;
    View mView;
    File mypath;

    private String uniqueId;
    private EditText yourName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloture3);


        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n = b.getString("loginselectionne");
        i = b.getInt("id_agent");
        cashf = b.getString("cashf");
        KMF =b.getInt("km_f");
        loginDisplay.setText(n);
        /****************/
        list = (ListView) findViewById(R.id.ListPrduitClient);
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cloture3.this, acceiul_activity.class);
                intent.putExtra("loginselectionne", n);
                intent.putExtra("id_agent", i);
                startActivity(intent);
                finish();
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Cloture3.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });


        DB.open();
        Cursor c22=DB.getIdTournee(i);
        if(c22.moveToFirst()){
            j = c22.getInt(0);
            Log.d("ecarecaisse","idLivraison = "+j);
        }

        btncloturer = (Button) findViewById(R.id.btncloturer);
        btncloturer.setEnabled(false);

        Date_Debut = (TextView) findViewById(R.id.DateDebutTournee);
        Date_Debut.setText("Date début : "+getTodaysDate());
        DateFin = (TextView) findViewById(R.id.DateFinTournee);
        DateFin.setText("Date fin : "+getTodaysDate());

        int som1= 0;
        int som2= 0;
        int somES=0;


        Cursor c2 = DB.getEcart(j);
        if (c2.moveToFirst()) {
            do {
                int i = c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_STOCK_INITIALE_REEL_STOCK_TOURNEE));
                int v =c2.getInt(c2.getColumnIndex(DatabaseManager.KEY_STOCK_FINAL_REEL_STOCK_TOURNEE));
                som1=som1+i;
                som2=som2+v;
            } while (c2.moveToNext());
            c2.close();
            somES=som2-som1;
        }

        Cursor c3 = DB.getEcartCaisse(j);
        if (c3.moveToFirst()) {

                 cashInit = c3.getInt(c3.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE));

            c3.close();

        }

        Log.d("ecarecaisse","value = "+cashInit);

        EcartStock = (TextView)findViewById(R.id.EcartStockFinal);
        EcartCaisse =(TextView)findViewById(R.id.EcartCaisseFinal);
        String cashText= cashf;
        int CachF=Integer.parseInt(cashText);
        int Ecart = CachF-cashInit;


        EcartStock.setText("Ecart stock : "+String.valueOf(somES));
        Ecare_Stock = somES;
        EcartCaisse.setText("Ecart caisse : "+String.valueOf(Ecart));
        Ecare_caisse = Ecart;




        Cursor c23=DB.getIdTournee(i);

        if(c23.moveToFirst()){
            j = c23.getInt(c23.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE));
            Log.d("synchro_Tournee","Cloture 3 j = "+j);
        }
        DB.close();



        //Signature

        tempDir = Environment.DIRECTORY_DOCUMENTS + "/" + "GetSiagnature" + "/";

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = new File(Environment.getExternalStorageDirectory() + "/Documents/");


        Toast.makeText(getApplicationContext(), Environment.getExternalStorageDirectory() + "/Documents/", Toast.LENGTH_SHORT).show();

        //File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
        // File directory = cw.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE);


        // Toast.makeText(getApplicationContext(), "kkkk"+tempDir+"fff", Toast.LENGTH_SHORT).show();

        prepareDirectory();
        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
        current = uniqueId + ".png";
        mypath= new File(directory,current);

        //Toast.makeText(getApplicationContext(), "mypath"+mypath+"dddd", Toast.LENGTH_SHORT).show();


        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.LTGRAY);
        mContent.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mView = mContent;



    }


    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:",String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:",String.valueOf(currentTime));
        return(String.valueOf(currentTime));

    }


    private boolean prepareDirectory()
    {
        try
        {
            if (makedirs())
            {
                return true;
            } else {
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean makedirs()
    {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory())
        {
            File[] files = tempdir.listFiles();
            for (File file : files)
            {
                if (!file.delete())
                {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }



    public void cloturer(View args){

            DateFinT = getTodaysDate();
            DB.open();
            agent a = new agent();
            a.setDateFin(DateFinT);
            a.MotifEcartCaisse = Ecare_caisse;
            a.setKMFin(KMF);
            a.MotifEcartStock = Ecare_Stock;
            DB.saveVehicule(a);
            DB.updateTournee(a,j);
            DB.SaveDateFinTour(a, j);
            DB.UpdaeTournee("Terminée", j);
            DB.close();
            //testDBDebut();
            //synchronisation_mobile_serveur();
           // testDB(j,KMF,DateFinT,"Terminee");
            Toast.makeText(context, "Fin Tournee", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Cloture3.this, MainActivity.class);
            startActivity(intent);
            finish();

    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testDB(int i,int KMF,String DateF,String Statut){
        String sql="update tournee " +
                "SET KMFin  = "+KMF+", Datefin='"+DateF+"' , statut='"+Statut+"' " +
                "WHERE id="+i+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }


        String sq2="update livraison " +
                "SET statut  = 'LIVREE' " +
                "WHERE idTournee="+i+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sq2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testDBDebut() {
        DB.open();
        Cursor c4 = DB.getInfoTournee(j);
        //Toast.makeText(Cloturer_Tournee.this, "Nombre de ligne " + c4.getCount(), Toast.LENGTH_LONG).show();
        if (c4.moveToFirst()) {
            do {
                String sql3 = "update tournee " +
                        "SET fondInitialrecu="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE))+" " +
                        ", KMDebut ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))+" " +
                        " WHERE id ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE))+"";


                Log.d("synchro_Tournee","req = "+ sql3);

                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_T = DriverManager.getConnection(url, user, pass);
                    Statement st_T = con_T.createStatement();
                    st_T.executeUpdate(sql3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c4.moveToNext());
        }
        //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c4.getCount(), Toast.LENGTH_LONG).show();
        c4.close();
        DB.close();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    public  void synchronisation_mobile_serveur(){

        DB.open();


        Cursor c4 = DB.getInfoTournee(j);
        //Toast.makeText(Cloturer_Tournee.this, "Nombre de ligne " + c4.getCount(), Toast.LENGTH_LONG).show();
        if (c4.moveToFirst()) {
            do {
                String sql3 = "update tournee " +
                        "SET fondInitialrecu="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE))+" " +
                        ", KMDebut ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))+" " +
                        ", statut='Terminée'"+" " +
                        ", KMFin= "+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_FIN_TOURNEE))+" " +
                        ", Datefin = '"+c4.getString(c4.getColumnIndex(DatabaseManager.KEY_DATE_FIN_TOURNEE))+"' " +
                        " WHERE id ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE))+"";


                Log.d("synchro_Tournee","req = "+ sql3);

                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_T = DriverManager.getConnection(url, user, pass);
                    Statement st_T = con_T.createStatement();
                    st_T.executeUpdate(sql3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c4.moveToNext());
        }
        //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c4.getCount(), Toast.LENGTH_LONG).show();
        c4.close();


        /*

        String sql="update tournee " +
                "SET KMFin  = "+KMF+", Datefin='"+DateFinT+"' , statut='Terminée' " +
                "WHERE id="+j+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

*/
        String sq2="update livraison " +
                "SET statut  = 'LIVREE' " +
                "WHERE idTournee="+j+"";
        try {
            //les droits d'acces et des permissions
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /////////////////////////////////
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(url, user, pass);
            Statement st1=con.createStatement();
            st1.executeUpdate(sq2);
        }catch (Exception e){
            e.printStackTrace();
        }

/*

        c4 = DB.getInfoTournee(j);
        //Toast.makeText(Cloturer_Tournee.this, "Nombre de ligne " + c4.getCount(), Toast.LENGTH_LONG).show();
        if (c4.moveToFirst()) {
            do {
                String sql3 = "update tournee " +
                        "SET fondInitialrecu="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_FONT_INITIAL_RECU_TOURNEE))+" " +
                        ", KMDebut ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_KM_DEBUT_TOURNEE))+" " +
                        " WHERE id ="+c4.getInt(c4.getColumnIndex(DatabaseManager.KEY_ID_TOURNEE))+"";
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_T = DriverManager.getConnection(url, user, pass);
                    Statement st_T = con_T.createStatement();
                    st_T.executeUpdate(sql3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c4.moveToNext());
        }
        //Toast.makeText(Cloturer_Tournee.this, "Erreur" + c4.getCount(), Toast.LENGTH_LONG).show();
        c4.close();
     */


        Cursor c=DB.getEtatCommande();
        if(c.moveToFirst()) {
            do {
                String sql2="update commandeclient " +
                        "SET statut='Livree'"+
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"))+"";
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection(url, user, pass);
                    Statement st1=con.createStatement();
                    st1.executeUpdate(sql2);
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
            Log.d("qsdfg","not null");
            do {
                String sql3="update lignecommande " +
                        "SET quantiteLivree="+c.getInt(c.getColumnIndex("quantiteLivree"))+" " +
                        ", quantite_livree_reel ="+c.getInt(c.getColumnIndex("quantite_livree_reel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idCommande = "+c.getInt(c.getColumnIndex("idCommande"));
                Log.d("qsdfg","test : "+sql3);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql3);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/


        c=DB.getAllLigne(DatabaseManager.TABLE_ETAPE_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql4="update etapetournee " +
                        "SET statut='"+c.getString(c.getColumnIndex("statut"))+"' " +
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"));
                Log.d("qsdfg","test : "+sql4);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql4);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/

        c=DB.getAllLigne(DatabaseManager.TABLE_STOCK_TOURNEE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql5="update tracestocktournee " +
                        "SET stockinitialReel="+c.getInt(c.getColumnIndex("stockinitialReel"))+", " +
                        " stockEncours="+c.getInt(c.getColumnIndex("stockEncours"))+", " +
                        " stockFinalReel="+c.getInt(c.getColumnIndex("stockFinalReel"))+" " +
                        "WHERE idProduit ="+c.getInt(c.getColumnIndex("idProduit"))+" and idTournee="+c.getInt(c.getColumnIndex("idTournee"));
                Log.d("qsdfg","test : "+sql5);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql5);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/



        c=DB.getAllLigne(DatabaseManager.TABLE_VEHICULE);
        if(c.moveToFirst()) {
            Log.d("qsdfg","not null");
            do {
                String sql5="update vehicule " +
                        "SET KMDebut="+c.getInt(c.getColumnIndex("KMDebut"))+" " +
                        "WHERE id ="+c.getInt(c.getColumnIndex("id"));
                Log.d("qsdfg","test : "+sql5);
                try {
                    //les droits d'acces et des permissions
                    StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    /////////////////////////////////
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con_cmd=DriverManager.getConnection(url, user, pass);
                    Statement st1=con_cmd.createStatement();
                    st1.executeUpdate(sql5);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        else{
            Log.d("qsdfg","null");
        }
        c.close();
        /***************************************/


        DB.close();
    }


    public void precedent(View args){

        Intent intent = new Intent(Cloture3.this, Cloture1.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();

    }

    public void annuler(View v){
        Intent intent = new Intent(Cloture3.this, acceiul_activity.class);
        intent.putExtra("loginselectionne", n);
        intent.putExtra("id_agent", i);
        startActivity(intent);
        finish();
    }





    public class signature extends View
    {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v)
        {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if(mBitmap == null)
            {
                mBitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);;
            }
            Canvas canvas = new Canvas(mBitmap);
            try
            {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();


                //String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                //Log.v("log_tag","url: " + url);


                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            }
            catch(Exception e)
            {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear()
        {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            float eventX = event.getX();
            float eventY = event.getY();
            btncloturer.setEnabled(true);

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++)
                    {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string){
        }

        private void expandDirtyRect(float historicalX, float historicalY)
        {
            if (historicalX < dirtyRect.left)
            {
                dirtyRect.left = historicalX;
            }
            else if (historicalX > dirtyRect.right)
            {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top)
            {
                dirtyRect.top = historicalY;
            }
            else if (historicalY > dirtyRect.bottom)
            {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY)
        {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

}
