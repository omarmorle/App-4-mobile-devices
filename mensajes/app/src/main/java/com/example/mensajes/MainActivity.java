package com.example.mensajes;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.*;
import android.widget.*;
public class MainActivity extends AppCompatActivity {
    Button jbn,jbnA;
    int i=0;
    EditText jet1,jet2,jet3;
    String numero,mensaje;
    SQLiteDatabase sqld;
    Spinner spinner1;
    String elnum;
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        jet1 = (EditText)findViewById(R.id.xet1);
        jet2 = (EditText)findViewById(R.id.xet2);
        jet3 = (EditText)findViewById(R.id.xet3);
        jbn = (Button)findViewById(R.id.xbn);
        jbnA = (Button)findViewById(R.id.xbn2);
        DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this, "DBContactos", null, 1);
        sqld = dsqlh.getWritableDatabase();
        spinner1=(Spinner) findViewById(R.id.spn);

        String[] contac = {"","","","",""};
        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,contac);
        spinner1.setAdapter(adapter);

        if(ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.SEND_SMS)!=
                PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)!=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.SEND_SMS,},1000);
        }
        jbn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String seleccion = spinner1.getSelectedItem().toString();
                //Buscar en la base de datos el numero de telefono del contacto
                Cursor c = sqld.rawQuery("SELECT numero FROM contactos WHERE nombre='"+seleccion+"'",null);
                if(c.moveToFirst()){
                    elnum = c.getString(0);
                }
                //Poner el numero en el campo de texto
                jet1.setText(elnum);
                //Poner el nombre en el campo de texto
                jet2.setText(seleccion);
                mensaje = jet2.getText().toString();
                enviarMensaje(elnum,mensaje);
            }
        });
        jbnA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String numero = jet1.getText().toString();
                String nombre = jet3.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put("numero", numero);
                cv.put("nombre", nombre);
                sqld.insert("Contactos", null, cv);
                contac[i] =nombre;
                i++;
                jet1.setText(""); jet3.setText("");
            }
        });
    }
    private void enviarMensaje (String n, String m){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(n,null, m,null,null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
        }
    }
}