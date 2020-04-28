package com.example.musicplayer5;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1, button2;
    private TextView text1, text2;


    private final int REQ = 2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int i = 0;
    int like1 = 0;
    int play1 = 0;
    int like2 = 0;
    int play2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);



        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        SharedPreferences sp =getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor edit  = sp.edit();

        like1 = sp.getInt("like1",0);
        play1 = sp.getInt("play1",0);
        like2 = sp.getInt("like2",0);
        play2 = sp.getInt("play2",0);

        text1.setText("Play: "+play1 +"   Likes:  "+like1);
        text2.setText("Play: "+play2 +"   Likes:  "+like2);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:

                i = 0;

                Intent intent1 = new Intent(getApplicationContext(), Second.class);

                intent1.putExtra("i",i);
                intent1.putExtra("play1",play1);
                intent1.putExtra("like1",like1);
                startActivityForResult(intent1,REQ);
                break;


            case R.id.button2:

                i = 1;

                Intent intent2 = new Intent(getApplicationContext(), Second.class);

                intent2.putExtra("i",i);
                intent2.putExtra("play2",play2);
                intent2.putExtra("like2",like2);
                startActivityForResult(intent2,REQ);
                break;

        }
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ){
            if(resultCode == 0){
                int i;
                i = data.getIntExtra("i",-1);
                if(i == 0){
                    play1 = data.getIntExtra("play",-1);
                    like1 = data.getIntExtra("like",-1);
                    text1.setText("Play: "+play1 +"   Likes:  "+like1);
                }
                if(i == 1){
                    play2 = data.getIntExtra("play",-1);
                    like2 = data.getIntExtra("like",-1);
                    text2.setText("Play: "+play2 +"   Likes:  "+like2);
                }
            }
        }
    }
}