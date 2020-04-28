package com.example.nayunsuhw_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button mybutton;
    private Button mybutton2;
    private EditText myInch;
    private EditText myCm;

    public boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mybutton = findViewById(R.id.button);
        mybutton2 = findViewById(R.id.button2);
        myInch = findViewById(R.id.editINCH);
        myCm = findViewById(R.id.editCM);

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                String InchToCm = myInch.getText().toString() ;
                if(InchToCm.length() == 0){
                    Toast.makeText(getApplicationContext(),"값을 입력해주세요",Toast.LENGTH_SHORT).show();
                    myCm.setText("");
                }
                else if(!isNumeric(InchToCm)){
                    Toast.makeText(getApplicationContext(),"숫자를 입력해주세요",Toast.LENGTH_SHORT).show();
                    myCm.setText("");
                }
                else {
                    Double cm = Double.valueOf(InchToCm) * 2.54;
                    if (cm < 0) {
                        Toast.makeText(getApplicationContext(), "양수값을 입력해주세요", Toast.LENGTH_SHORT).show();
                        myCm.setText("");
                    }
                    else {
                        myCm.setText(String.valueOf(cm));
                    }
                }
            }
        });


        mybutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CmToInch = myCm.getText().toString();

                if (CmToInch.length() == 0) {
                    Toast.makeText(getApplicationContext(), "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    myCm.setText("");
                } else if (!isNumeric(CmToInch)) {
                    Toast.makeText(getApplicationContext(), "숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    myCm.setText("");
                } else {
                    Double inch = Double.valueOf(CmToInch) / 2.54;
                    if (inch < 0) {
                        Toast.makeText(getApplicationContext(), "양수값을 입력해주세요", Toast.LENGTH_SHORT).show();
                        myInch.setText("");
                    }
                    else {
                        myInch.setText(String.valueOf(inch));
                    }
                }
            }
        });

    }
}
