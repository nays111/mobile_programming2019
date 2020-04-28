package com.example.chooseone;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int CODE = 2;
    ImageView img1;
    ImageView img2;
    TextView instagramlike;
    TextView instagramdislike;
    TextView facebooklike;
    TextView facebookdislike;
    final String[] name = {"인스타그램", "페이스북"};
    final String[] year ={"2010","2004"};
    final String[] developer = {"Kevin Systrom, Mike Krieberg","Mark Zuckerberg"};
    int insta_like=0;
    int insta_dislike = 0;
    int facebook_like = 0;
    int facebook_dislike = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView)findViewById(R.id.imageView3);
        img2 = (ImageView)findViewById(R.id.imageView4);
        instagramlike = (TextView)findViewById(R.id.textView8);
        instagramdislike = (TextView)findViewById(R.id.textView9);
        facebooklike = (TextView)findViewById(R.id.textView10);
        facebookdislike=(TextView)findViewById(R.id.textView11);


        instagramlike.setText(String.valueOf(insta_like));
        instagramdislike.setText(String.valueOf(insta_dislike));
        facebooklike.setText(String.valueOf(facebook_like));
        facebookdislike.setText(String.valueOf(facebook_dislike));


        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageView3:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name",name[0]);
                intent.putExtra("year",year[0]);
                intent.putExtra("developer",developer[0]);
                intent.putExtra("insta_like",insta_like);
                intent.putExtra("insta_dislike",insta_dislike);
                intent.putExtra("id",0);
                startActivityForResult(intent,CODE);
                break;

            case R.id.imageView4:
                Intent intent2 = new Intent(MainActivity.this, DetailActivity.class);
                intent2.putExtra("name",name[1]);
                intent2.putExtra("year",year[1]);
                intent2.putExtra("developer",developer[1]);
                intent2.putExtra("facebook_like",facebook_like);
                intent2.putExtra("facebook_dislike",facebook_dislike);
                intent2.putExtra("id",1);
                startActivityForResult(intent2,CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            if (resultCode == 1) {
                insta_like = data.getIntExtra("returnData", -1);
                instagramlike.setText(String.valueOf(insta_like));
            }
            if (resultCode == 2) {
                insta_dislike = data.getIntExtra("returnData", -1);
                instagramdislike.setText(String.valueOf(insta_dislike));
            }
            if (resultCode == 3) {
                facebook_like = data.getIntExtra("returnData", -1);
                facebooklike.setText(String.valueOf(facebook_like));
            }
            if (resultCode == 4) {
                facebook_dislike = data.getIntExtra("returnData", -1);
                facebookdislike.setText(String.valueOf(facebook_dislike));
            }
        }
    }
}
