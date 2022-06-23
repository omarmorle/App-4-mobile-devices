package com.example.barrasqr;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
public class MainActivity extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
    }
    public void scanBarra(View v) {
        try { //Inicia escaneo desde: com.google.zxing.client.android.SCAN
            Intent i1 = new Intent(ACTION_SCAN);
            i1.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(i1, 0);
        } catch (ActivityNotFoundException anfe) { //Al obtenerlo, muestra diálogo de descarga
            showDialog(MainActivity.this, "No hay scanner", "Descargar scanner?", "Si", "No").show();
        }
    }
    public void scanQR(View v) { //Modo QR del producto
        try { //Inicia escaneo desde: com.google.zxing.client.android.SCAN
            Intent i2 = new Intent(ACTION_SCAN);
            i2.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(i2, 0);
        } catch (ActivityNotFoundException anfe) { //Al obtenerlo, muestra diálogo de descarga
            showDialog(MainActivity.this, "No hay scanner", "Descargar scanner?", "Si", "No").show();
        }
    }
    private static AlertDialog showDialog(final Activity a, CharSequence st, CharSequence mn,
                                          CharSequence bnSi, CharSequence bnNo) {
        AlertDialog.Builder adb = new AlertDialog.Builder(a); //Alert para descarga
        adb.setTitle(st);
        adb.setMessage(mn);
        adb.setPositiveButton(bnSi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    a.startActivity(in);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        adb.setNegativeButton(bnNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int i) {
            }
        });
        return adb.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent i) { //Resultado
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) { //Obtiene extras regresados por el intento
                String sr = i.getStringExtra("SCAN_RESULT");
                String sf = i.getStringExtra("SCAN_RESULT_FORMAT");
                Toast t = Toast.makeText(this, "Content:" + sr + " Format:" + sf, Toast.LENGTH_LONG);
                t.show();
            }
        }
    }
}