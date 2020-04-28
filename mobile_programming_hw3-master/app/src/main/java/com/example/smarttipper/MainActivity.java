package com.example.smarttipper;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    double sale;
    double tip;
    int rate;
    double total;
    Random rnd = new Random();

    private RadioGroup rgroup;
    private SeekBar seekbar;
    private CheckBox checkbox;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

    private EditText editText4;

    private TextView textview1;
    private TextView textview2;
    private TextView textview3;
    private TextView textview4;
    private TextView textview7;
    private TextView textview8;
    private TextView textview11;
    private TextView textview12;
    private TextView textview13;

    private RadioButton randombutton;
    private RadioButton notipbutton;
    private RadioButton maxtipbutton;
    private RadioButton tipbybutton;

    private ConstraintLayout back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back= findViewById(R.id.constraintLayout);
        rgroup = findViewById(R.id.rgroup);
        checkbox = findViewById(R.id.id_darkmode);
        seekbar = findViewById(R.id.id_seekbar);

        editText1 = findViewById(R.id.edit_sales);
        editText2 = findViewById(R.id.edit_tip);
        editText3 = findViewById(R.id.edit_total);

        editText4 = findViewById(R.id.edit_rate);

        randombutton = findViewById(R.id.id_randtip);
        notipbutton = findViewById(R.id.id_notip);
        maxtipbutton = findViewById(R.id.id_maxtip);
        tipbybutton = findViewById(R.id.id_tipby);

        textview1 = findViewById(R.id.textView);
        textview2 = findViewById(R.id.textView2);
        textview3 = findViewById(R.id.textView3);
        textview4 = findViewById(R.id.textView4);
        textview7 = findViewById(R.id.textView7);
        textview8 = findViewById(R.id.textView8);
        textview11 = findViewById(R.id.textView11);
        textview12 = findViewById(R.id.textView12);
        textview13 = findViewById(R.id.textView13);





        //1. Sales를입력하면Tip과Total이자동으로계산됨(2) Tip = Sales*Rate/100//

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    sale = Double.valueOf(editText1.getText().toString().trim());
                }catch(Exception e){
                    sale = 0;
                }finally{
                    tip = sale*rate / 100;
                    total = tip + sale;
                    editText2.setText(String.valueOf(tip));
                    editText3.setText(String.valueOf(total));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //2. Rate SeekBar를움직이면해당Rate 값이업데이트됨(1)//
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rate = seekbar.getProgress();
                tip = sale * rate / 100;
                total = sale + tip;

                editText2.setText(String.valueOf(tip));
                editText3.setText(String.valueOf(total));
                editText4.setText(String.valueOf(i)+"%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //3. NO TIP, RAND TIP, TIP by %, MAX TIP중하나만가능(2)--> radiobutton,group 사용해서 해결//
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                switch(i){
                    //4. NO TIP: Tip=0, Rate=0%, SeekBar의Nob은0으로(1) SeekBar는Disable 됨(1)//
                    case R.id.id_notip:
                        tip = 0;
                        rate = 0;
                        editText2.setText(String.valueOf(tip));
                        editText4.setText(String.valueOf(rate));
                        seekbar.setProgress(0);
                        seekbar.setEnabled(false);
                        break;

                    case R.id.id_randtip:
//6. RAND TIP: 0~30%중랜덤하게TIP을결정함. 이에따라Tip, Rate, SeekBar가 수정됨. SeekBar가Disable되지않음(1) RANDTIPRadioButton을click할때마다다시Random 으로Tip을정함. (1)//
                        randombutton.setOnClickListener(new RadioButton.OnClickListener(){
                            public void onClick(View v){
                                int num = rnd.nextInt(31);
                                rate = num;
                                tip = sale*rate/100;
                                total = sale + tip;
                                editText2.setText(String.valueOf(tip));
                                editText3.setText(String.valueOf(total));
                                editText4.setText(String.valueOf(num));
                                seekbar.setProgress(rate);
                            }
                        });


                        seekbar.setEnabled(true);
                        break;


                    case R.id.id_tipby://5. TIP by %: 보통모드. (1)->xml에서해결완료//

                        tip = sale * rate/100;
                        editText2.setText(String.valueOf(tip));

                        seekbar.setEnabled(true);
                        break;


                    case R.id.id_maxtip:
//7. Max TIP: Tip Rate=30%, SeekBar도30, SeekBarDisable (1)//
                        tip = sale * rate/100;
                        rate=30;
                        editText2.setText(String.valueOf(tip));
                        editText4.setText(String.valueOf(rate));
                        seekbar.setProgress(30);
                        seekbar.setEnabled(false);
                        break;
                }
            }
        });

        checkbox.setOnClickListener(new CheckBox.OnClickListener(){
            public void onClick(View v){
                if(checkbox.isChecked()) {
                    back.setBackgroundColor(Color.BLACK);
                    editText1.setTextColor(Color.WHITE);
                    editText2.setTextColor(Color.WHITE);
                    editText3.setTextColor(Color.WHITE);
                    editText4.setTextColor(Color.WHITE);
                    rgroup.setBackgroundColor(Color.BLACK);
                    randombutton.setTextColor(Color.WHITE);
                    notipbutton.setTextColor(Color.WHITE);
                    maxtipbutton.setTextColor(Color.WHITE);
                    tipbybutton.setTextColor(Color.WHITE);
                    checkbox.setTextColor(Color.WHITE);
                    textview1.setTextColor(Color.WHITE);
                    textview2.setTextColor(Color.WHITE);
                    textview3.setTextColor(Color.WHITE);
                    textview4.setTextColor(Color.WHITE);
                    textview7.setTextColor(Color.WHITE);
                    textview8.setTextColor(Color.WHITE);
                    textview11.setTextColor(Color.WHITE);
                    textview12.setTextColor(Color.WHITE);
                    textview13.setTextColor(Color.WHITE);


                }
                else{
                    back.setBackgroundColor(Color.WHITE);
                    editText1.setTextColor(Color.BLACK);
                    editText2.setTextColor(Color.BLACK);
                    editText3.setTextColor(Color.BLACK);
                    editText4.setTextColor(Color.BLACK);
                    rgroup.setBackgroundColor(Color.WHITE);
                    randombutton.setTextColor(Color.BLACK);
                    notipbutton.setTextColor(Color.BLACK);
                    maxtipbutton.setTextColor(Color.BLACK);
                    tipbybutton.setTextColor(Color.BLACK);
                    checkbox.setTextColor(Color.BLACK);
                    textview1.setTextColor(Color.BLACK);
                    textview2.setTextColor(Color.BLACK);
                    textview3.setTextColor(Color.BLACK);
                    textview4.setTextColor(Color.BLACK);
                    textview7.setTextColor(Color.BLACK);
                    textview8.setTextColor(Color.BLACK);
                    textview11.setTextColor(Color.BLACK);
                    textview12.setTextColor(Color.BLACK);
                    textview13.setTextColor(Color.BLACK);
                }
            }
        });


    }

}
