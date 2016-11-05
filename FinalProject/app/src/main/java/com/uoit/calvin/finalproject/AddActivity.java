package com.uoit.calvin.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void clickSave(View v) {
        Intent returnIntent = new Intent();
        EditText input = (EditText) findViewById(R.id.dataInput);
        String result = input.getText().toString();
        returnIntent.putExtra("result",result);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
