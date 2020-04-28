package com.example.contactandsms3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1;
    private Button button2;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button);
        button2= (Button)findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                i=0;
                Intent intent1 =new Intent(getApplication(),Second.class);
                intent1.putExtra("i",i);
                startActivity(intent1);
                break;

            case R.id.button2:
                i=1;
                Intent intent2 = new Intent(getApplication(),Third.class);
                intent2.putExtra("i",i);
                startActivity(intent2);
                break;
        }
    }
}
