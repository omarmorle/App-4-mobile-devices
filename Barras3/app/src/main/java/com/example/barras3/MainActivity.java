package com.example.barras3;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.RatingBar.*;
public class MainActivity extends Activity {
    RatingBar   jrb1;
    TextView    jtv3;
    Button      jbn1;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        addListenerOnRatingBar();
        addListenerOnButton();
    }

    public void addListenerOnRatingBar() {
        jrb1 = (RatingBar) findViewById(R.id.xrb1);
        jtv3 = (TextView) findViewById(R.id.xtv3);
        jrb1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar rb, float f, boolean bo) {
                jtv3.setText(String.valueOf(f));
            }
        });
    }
    public void addListenerOnButton() {
        jrb1 = (RatingBar) findViewById(R.id.xrb1);
        jbn1 = (Button) findViewById(R.id.xbn1);
        jbn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Evaluación: " + jrb1.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}