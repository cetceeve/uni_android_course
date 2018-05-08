package com.example.fzeih.telefonbuch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCreateFriend = (Button) findViewById(R.id.button_create_friend);
        Button buttonFindFriend = (Button) findViewById(R.id.button_find_friend);

        buttonCreateFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterNewContact();
            }
        });

        buttonFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFriend();
            }
        });
    }

    private void enterNewContact() {
        EditText textboxInputName = (EditText) findViewById(R.id.textbox_input_name);
        EditText textboxInputNumber = (EditText) findViewById(R.id.textbox_input_number);

        String inputName = textboxInputName.getText().toString();
        int inputNumber = Integer.parseInt(textboxInputNumber.getText().toString());
    }

    private void findFriend() {
        EditText textboxOutputName = (EditText) findViewById(R.id.textbox_output_name);
    }
}
