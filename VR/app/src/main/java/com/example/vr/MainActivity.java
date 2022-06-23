package com.example.vr;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;



public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private long lastUpdate=0;

    AnimatedView animatedView = null;

    int  maxX, maxY, minMaxXY;
    float centerX, centerY,x,y,z;
    Paint pnt= new Paint();
    Obj obj = new Obj();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        animatedView = new AnimatedView(this);
        setContentView(animatedView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscope,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
       super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            long currentTime = System.currentTimeMillis();

            if((currentTime-lastUpdate)>60){
                lastUpdate= currentTime;
                x -= (int) event.values[0];
                y += (int) event.values[1];
                obj.theta=(float) maxX/x;
                obj.phi = (float) maxY/y;
                obj.rho=(obj.phi/obj.theta)*maxY;
                centerX=x/2;
                centerY=y/2;
            }


        }
    }

    public class AnimatedView extends ImageView {

        public AnimatedView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas c) {
            pnt.setColor(Color.BLACK);
            c.drawPaint(pnt);
            maxX=c.getWidth()/2;
            maxY=c.getHeight();
            pnt.setColor(Color.WHITE);
            minMaxXY=Math.min(maxX, maxY);
            centerX = maxX/2;
            centerY = maxY/2;
            obj.d = obj.rho*minMaxXY/obj.objSize;
            obj.eyeAndScreen();
            line(c, 0, 1); line(c, 1, 2); line(c, 2, 3); line(c, 3, 0); // aristas horizontales inferiores
            line(c, 4, 5); line(c, 5, 6); line(c, 6, 7); line(c, 7, 4); // aristas horizontales superiores
            line(c, 0, 4); line(c, 1, 5); line(c, 2, 6); line(c, 3, 7); // aristas verticales


            maxX=c.getWidth()+1000;
            maxY=c.getHeight();
            minMaxXY=Math.min(maxX, maxY);
            centerX = maxX/2;
            centerY = maxY/2;
            obj.d = obj.rho*minMaxXY/obj.objSize;
            obj.eyeAndScreen();
            line(c, 0, 1); line(c, 1, 2); line(c, 2, 3); line(c, 3, 0); // aristas horizontales inferiores
            line(c, 4, 5); line(c, 5, 6); line(c, 6, 7); line(c, 7, 4); // aristas horizontales superiores
            line(c, 0, 4); line(c, 1, 5); line(c, 2, 6); line(c, 3, 7); // aristas verticales
            invalidate();
        }
    }

    class Obj{ // Posee los datos del objeto 3D
        float rho, theta=0.3F, phi=1.3F, d, objSize, v11, v12, v13, v21, v22, v23, v32, v33, v43; // elementos de la matriz V
        Point3D [] w; // coordenadas universales
        Point2D [] vScr; // coordenadas de la pantalla
        Obj(){ // CAMBIAR LAS COORDENADAS X,Y,Z CON 0,1 PARA CONSTRUIR PRISMA, CILINDRO, PIRAMIDE, CONO Y ESFERA.
            w = new Point3D[8];
            vScr = new Point2D[8];

            w[0] = new Point3D(1, -1, -1); // desde la base
            w[1] = new Point3D(1, 1, -1);
            w[2] = new Point3D(-1, 1, -1);
            w[3] = new Point3D(-1, -1, -1);
            w[4] = new Point3D(1, -1, 1);
            w[5] = new Point3D(1, 1, 1);
            w[6] = new Point3D(-1, 1, 1);
            w[7] = new Point3D(-1, -1, 1);
            objSize = (float) Math.sqrt(16F); // = sqrt(2*2 + 2*2 + 2*2) es la distancia entre dos vertices opuestos
            rho = 5*objSize; // para cambiar la perspectiva
        }
        void initPersp(){
            float costh = (float)Math.cos(theta), sinth=(float)Math.sin(theta),
                    cosph=(float)Math.cos(phi), sinph=(float)Math.sin(phi);
            v11 = -sinth; v12 = -cosph*costh; v13 = sinph*costh;
            v21 = costh; v22 = -cosph*sinth; v23 = sinph*sinth;
            v32 = sinph; v33 = cosph; v43 = -rho;
        }
        void eyeAndScreen(){
            initPersp();
            for(int i=0; i<8; i++){
                Point3D p = w[i];
                float x = v11*p.x + v21*p.y, y = v12*p.x + v22*p.y + v32*p.z, z = v13*p.x + v23*p.y
                        + v33*p.z + v43;
                vScr[i] = new Point2D(-d*x/z, -d*y/z);
            }
        }
    }

    class Point2D{
        float x, y;
        Point2D(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
    class Point3D{
        float x, y, z;
        Point3D(double x, double y, double z){
            this.x = (float) x;
            this.y = (float) y;
            this.z = (float) z;
        }
    }
    int iX(float x){
        return Math.round(centerX + x);
    }
    int iY(float y){
        return Math.round(centerY - y);
    }
    void line(Canvas c, int i, int j){
        Point2D p = obj.vScr[i], q = obj.vScr[j];
        c.drawLine(iX(p.x), iY(p.y), iX(q.x), iY(q.y),pnt);
    }
}
