package com.example.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

   private Socket socket;
  private   PrintWriter printWriter;
  private   EditText Nombre, Edad, Mensaje;
  Button buttonEniviar;
  int puerto =8080;
  String mensaje, nombre, edad;

 //   MultiAutoCompleteTextView textoMostrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nombre =  findViewById(R.id.editTextnombre);
        Edad =  findViewById(R.id.editTextEdad);
        Mensaje =  findViewById(R.id.editTextMensaje);
        buttonEniviar =  findViewById(R.id.buttonEnciar);
  //      textoMostrar = findViewById(R.id.MostrarTexto)

        buttonEniviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensaje =  Mensaje.getText().toString();
               nombre = Nombre.getText().toString();
               edad = Edad.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket =  new Socket("192.168.0.41",puerto);
                            printWriter =  new PrintWriter(socket.getOutputStream());
                            printWriter.write("nombre: " + nombre+ " " +"Edad: " + edad + " " + "mensaje: " + mensaje);

                            printWriter.flush();
                            printWriter.close();
                           // socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Nombre.setText(null);
                Edad.setText(null);
                Mensaje.setText(null);
            }
        });
    }
}
