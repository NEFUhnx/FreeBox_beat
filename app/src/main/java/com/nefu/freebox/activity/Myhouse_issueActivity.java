package com.nefu.freebox.activity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.nefu.freebox.bean.BaseActivity;
import com.nefu.freebox.R;

public class Myhouse_issueActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhouse_issue);
        Toolbar toolbar=findViewById(R.id.activity_myhouse_issue_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
           actionBar.setTitle("issue");
           actionBar.setDisplayHomeAsUpEnabled(true);
        }
        areaError();
        locationError();
        liveableError();
        describeError();
        contactsError();
        numberError();
        issue_button();


    }
    public void issue_button(){
        Button issueButton=findViewById(R.id.issueButton);
        final TextInputEditText contactsEditText=findViewById(R.id.issue_contacts_EditText);
        final TextInputEditText describeEditText=findViewById(R.id.issue_describe_EditText);
        final TextInputEditText liveableEditText=findViewById(R.id.issue_liveable_EditText);
        final TextInputEditText areaEditText=findViewById(R.id.issue_area_EditText);
        final TextInputEditText locationEditText=findViewById(R.id.issue_location_EditText);
        final TextInputEditText numberEditText=findViewById(R.id.issue_number_EditText);
        final TextInputEditText rentEditText=findViewById(R.id.issue_rent_EditText);
        final TextInputEditText shorestEditText=findViewById(R.id.issue_shorest_EditText);


            issueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (    contactsEditText.getText().toString().length()!=0
                            && describeEditText.getText().toString().length()!=0
                            && liveableEditText.getText().toString().length()!=0
                            && areaEditText.getText().toString().length()!=0
                            && locationEditText.getText().toString().length()!=0
                            && numberEditText.getText().toString().length()==11
                            && rentEditText.getText().toString().length()!=0
                            &&shorestEditText.getText().toString().length()!=0
                            ){

                        Toast.makeText(Myhouse_issueActivity.this,"click issue",Toast.LENGTH_LONG).show();
                              }
                              else{
                        Toast.makeText(Myhouse_issueActivity.this,"Please improve your information",Toast.LENGTH_LONG).show();
                    }

                }
            });




    }

    public void contactsError(){

      final TextInputLayout contactsLayout=findViewById(R.id.issue_contacts_inputText);
        final TextInputEditText contactsEditText=findViewById(R.id.issue_contacts_EditText);
        contactsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                contactsLayout.setError("Your name");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (contactsEditText.getText().toString().length()!=0){

                    contactsLayout.setErrorEnabled(false);
                }
            }
        });


    }
    public void describeError(){

        final TextInputLayout describeLayout=findViewById(R.id.issue_describe_inputText);
        final TextInputEditText describeEditText=findViewById(R.id.issue_describe_EditText);
        describeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                describeLayout.setError("Including the surrounding environment and other information");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (describeEditText.getText().toString().length()!=0){

                    describeLayout.setErrorEnabled(false);
                }
            }
        });


    }
    public void liveableError(){
        final TextInputLayout liveableLayout=findViewById(R.id.issue_liveable_inputText);
        final TextInputEditText liveableEditText=findViewById(R.id.issue_liveable_EditText);
        liveableEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                liveableLayout.setError("The number of people suitable for residence");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (liveableEditText.getText().toString().length()!=0){

                    liveableLayout.setErrorEnabled(false);
                }
            }
        });


    }
   public void areaError(){
       final TextInputLayout areaLayout=findViewById(R.id.issue_area_inputText);
       final TextInputEditText areaEditText=findViewById(R.id.issue_area_EditText);
          areaEditText.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                      areaLayout.setError("Your housing area");

              }

              @Override
              public void afterTextChanged(Editable editable) {
                  if (areaEditText.getText().toString().length()!=0){

                      areaLayout.setErrorEnabled(false);
                  }
              }
          });


   }

    public void locationError(){
        final TextInputLayout locationLayout=findViewById(R.id.issue_location_inputText);
        final TextInputEditText locationEditText=findViewById(R.id.issue_location_EditText);
        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                locationLayout.setError("The detailed address of your house");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (locationEditText.getText().toString().length()!=0){

                    locationLayout.setErrorEnabled(false);
                }
            }
        });


    }



    public void numberError(){

        final TextInputLayout numberLayout=findViewById(R.id.issue_number_inputText);
        final TextInputEditText numberEditText=findViewById(R.id.issue_number_EditText);

        numberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (numberEditText.getText().toString().length()>11){

                    numberLayout.setError("No more than 11 mobile phone numbers");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numberEditText.getText().toString().length()<11){
                    numberLayout.setError("The phone number can't be less than 11");
                }
                if (numberEditText.getText().toString().length()==11){
                    numberLayout.setErrorEnabled(false);
                }
                if (numberEditText.getText().toString().length()<11){
                    numberLayout.setError("The phone number can't be less than 11");
                }
                if (numberEditText.getText().toString().length()==0){
                    numberLayout.setError("Please insert");
                }

            }


        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }


}
