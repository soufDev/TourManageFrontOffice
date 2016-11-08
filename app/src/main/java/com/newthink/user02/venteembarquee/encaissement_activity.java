package com.newthink.user02.venteembarquee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by User02 on 17/03/2016.
 */
public class encaissement_activity extends Activity {

    ImageButton btnAcceuil;
    ImageButton btndeconnexion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encaisement_layout);
        btnAcceuil = (ImageButton) findViewById(R.id.btnAcceuil);
        btnAcceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(encaissement_activity.this, acceiul_activity.class);
                startActivity(intent);
            }
        });
        btndeconnexion = (ImageButton) findViewById(R.id.btndeconnexion);
        btndeconnexion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(encaissement_activity.this,MainActivity.class);
                startActivity(intent3);
            }});
    }
}
