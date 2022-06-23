package com.example.barras1;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity implements OnClickListener{
    ProgressBar jpb1, jpb2;
    Button jbn1;
    int i = 0;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        jpb1 = (ProgressBar) findViewById(R.id.xpb1);
        jpb2 = (ProgressBar) findViewById(R.id.xpb2);
        jbn1 = (Button) findViewById(R.id.xbn);
        jbn1.setOnClickListener(this);
    }

    public void onClick(View v){
        if (i == 0 || i == 10) {
            jpb1.setVisibility(View.VISIBLE);
            jpb1.setMax(200);
            jpb2.setVisibility(View.VISIBLE);
        }else if ( i< jpb1.getMax() ) {
            jpb1.setProgress(i);
            jpb1.setSecondaryProgress(i + 1);
        }else {
            jpb1.setProgress(0);
            jpb1.setSecondaryProgress(0);
            i = 0; jpb1.setVisibility(View.GONE);
            jpb2.setVisibility(View.GONE);
        }
        i = i + 10;
    }
}