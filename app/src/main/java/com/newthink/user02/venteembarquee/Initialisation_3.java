package com.newthink.user02.venteembarquee;

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
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Initialisation_3 extends Activity {
    String url = DatabaseManager.KEY_URL;
    String user = DatabaseManager.KEY_USER;
    String pass = DatabaseManager.KEY_PASSWORD;
    /********************************************/

    public static final int SIGNATURE_ACTIVITY = 1;
    Button btnAcceuil,btndeconnexion, btndemarrer;
    TabHost tabHost;
    // Context context = tournee_activity.this;
    ArrayList<Produit> myList = new ArrayList<Produit>();
    public DatabaseManager DB = new DatabaseManager(this);
    EditText username;
    EditText kilometrage,cashRecu, stockestimeEditText;
    ListView list;Context context = Initialisation_3.this;
    public static String n;
    public static int i,j;



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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation_3);


        /********************************Affichage de l'agent connecté***************************/
        final TextView loginDisplay = (TextView) findViewById(R.id.login_dispalay);
        final Intent intent2 = this.getIntent();
        Bundle b = intent2.getExtras();
        n= b.getString("loginselectionne");
        i=b.getInt("id_agent");
        loginDisplay.setText(n);
        DB.open();
        Cursor c2=DB.getIdTournee(i);
        if(c2.moveToFirst()){
            j = c2.getInt(0);
            Log.d("MYLOG","idLivraison = "+j);
        }
        else{
            Log.d("MYLOG","idLivraison = last null");
        }
        DB.close();
        /********************************Le header****************************************/
        btnAcceuil = (Button) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Initialisation_3.this, acceiul_activity.class);
                intent.putExtra("loginselectionne", n);
                Log.d("idAgentMenu", "id = " + i);
                intent.putExtra("id_agent", i);
                startActivity(intent);
                finish();
            }
        });
        btndeconnexion = (Button) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Initialisation_3.this, MainActivity.class);
                intent3.putExtra("loginselectionne", n);
                intent3.putExtra("id_agent",i);
                startActivity(intent3);
                finish();
            }
        });

        btndemarrer = (Button) findViewById(R.id.btndemarrer);
        btndemarrer.setEnabled(false);
        btndemarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.open();
                DB.UpdateEtatTournee("DEMARREE",j);
                DB.close();

                mView.setDrawingCacheEnabled(true);
                mSignature.save(mView);
                Bundle b = new Bundle();
                b.putString("status", "done");
                b.putString("uniquecode", uniqueId);

                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK, intent);

                Intent intent2 = new Intent(Initialisation_3.this,tournee_du_jour_activity.class);
                intent2.putExtra("loginselectionne",n);
                intent2.putExtra("id_agent",i);
                startActivity(intent2);
                finish();
            }
        });







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

    public void precedent(View v){
        Intent intent2 = new Intent(Initialisation_3.this,Initialisation_2.class);
        intent2.putExtra("loginselectionne",n);
        intent2.putExtra("id_agent",i);
        startActivity(intent2);
        finish();
    }



    protected void onDestroy() {
        Log.w("GetSignature", "onDestory");
        super.onDestroy();
    }

    private boolean captureSignature() {

        boolean error = false;
        String errorMessage = "";


        if(yourName.getText().toString().equalsIgnoreCase("")){
            errorMessage = errorMessage + "Please enter your Name\n";
            error = true;
        }

        if(error){
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
        }

        return error;
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
            btndemarrer.setEnabled(true);

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
