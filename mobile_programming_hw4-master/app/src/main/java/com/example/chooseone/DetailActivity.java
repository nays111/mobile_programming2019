package com.example.chooseone;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    String imageSrc="";
    String name="";
    String year="";
    String developer="";

    TextView Name;
    TextView Year;
    TextView Developer;
    ImageButton goodbutton;
    ImageButton badbutton;
    ImageView Image;

    int insta_like = 0;
    int insta_dislike = 0;
    int facebook_like = 0;
    int facebook_dislike = 0;

    private int count = 0;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Name = (TextView) findViewById(R.id.textView12);
        Year = (TextView) findViewById(R.id.textView13);
        Developer = (TextView) findViewById(R.id.textView14);
        goodbutton = (ImageButton) findViewById(R.id.imageButton);
        badbutton = (ImageButton) findViewById(R.id.imageButton2);
        Image = (ImageView) findViewById(R.id.imageView);

        goodbutton.setOnClickListener(this);
        badbutton.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();

        name = extras.getString("name");
        year = extras.getString("year");
        developer = extras.getString("developer");

        if (extras != null) {
            i=extras.getInt("id");
            if (i==0) {
                Image.setImageResource(R.drawable.instagram);
            } else {

                Image.setImageResource(R.drawable.facebook);
            }
        }
        Name.setText(name);
        Year.setText(year);
        Developer.setText(developer);



    }

    public void onClick(View v){
            Intent returnIntent = getIntent();
            switch(v.getId()){
                case R.id.imageButton:
                    if(i==0){
                        count = getIntent().getIntExtra("insta_like",0);
                        returnIntent.putExtra("returnData",count+1);
                        setResult(1,returnIntent);
                        finish();
                        break;
                    }
                    else{
                        count = getIntent().getIntExtra("facebook_like",0);
                        returnIntent.putExtra("returnData",count+1);
                        setResult(3,returnIntent);
                        finish();
                        break;
                    }
                case R.id.imageButton2:
                    if(i==0){


                        count = getIntent().getIntExtra("insta_dislike",0);
                        returnIntent.putExtra("returnData",count+1);
                        setResult(2,returnIntent);
                        finish();
                        break;
                    }
                    else{
                        count = getIntent().getIntExtra("facebook_dislike",0);
                        returnIntent.putExtra("returnData",count+1);
                        setResult(4,returnIntent);
                        finish();
                        break;
                    }

            }
    }
}
