package com.example.fzeih.telefonbuch;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "friend_database";
    private FriendDatabase friendDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCreateFriend = findViewById(R.id.button_create_friend);
        Button buttonFindFriend = findViewById(R.id.button_find_friend);

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

        friendDatabase = Room.databaseBuilder(this, FriendDatabase.class, DATABASE_NAME).build();
    }

    private void enterNewContact() {
        EditText textboxInputName = findViewById(R.id.textbox_input_name);
        EditText textboxInputNumber = findViewById(R.id.textbox_input_number);

        String inputName = textboxInputName.getText().toString();
        int inputNumber = Integer.parseInt(textboxInputNumber.getText().toString());

        Friend friend = new Friend();
        friend.setFriendName(inputName);
        friend.setPhoneNumber(inputNumber);

        new DatabaseInsertTask(friendDatabase, textboxInputName, textboxInputNumber).execute(friend);
    }

    private void findFriend() {
        EditText textboxOutputName = findViewById(R.id.textbox_output_name);
        String outputName = textboxOutputName.getText().toString();
        TextView textViewOutputName = findViewById(R.id.textview_output_name);
        TextView textViewOutputNumber = findViewById(R.id.textview_output_number);

        new DatabaseQueryTask(friendDatabase, textViewOutputName, textViewOutputNumber).execute(outputName);
    }

    private class DatabaseInsertTask extends AsyncTask<Friend, Void, Void> {
        private FriendDatabase friendDatabase;
        private EditText textboxInputName;
        private EditText textboxInputNumber;

        DatabaseInsertTask(FriendDatabase friendDatabase, EditText textboxInputName, EditText textboxInputNumber) {
            this.friendDatabase = friendDatabase;
            this.textboxInputName = textboxInputName;
            this.textboxInputNumber = textboxInputNumber;
        }

        @Override
        protected Void doInBackground(Friend... friend) {
            friendDatabase.daoAccess().insertOnlySingleFriend(friend[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            textboxInputName.setText("");
            textboxInputNumber.setText("");
        }
    }

    private class DatabaseQueryTask extends AsyncTask<String, Void, Friend> {
        private FriendDatabase friendDatabase;
        private TextView textViewOutputName;
        private TextView textViewOutputNumber;

        DatabaseQueryTask(FriendDatabase friendDatabase, TextView textViewOutputName, TextView textViewOutputNumber) {
            this.friendDatabase = friendDatabase;
            this.textViewOutputName = textViewOutputName;
            this.textViewOutputNumber = textViewOutputNumber;
        }

        @Override
        protected Friend doInBackground(String... friendName) {
            return friendDatabase.daoAccess().fetchOneFriendbyFriendName(friendName[0]);
        }

        @Override
        protected void onPostExecute(Friend friend) {
            textViewOutputName.setText(friend.getFriendName());
            textViewOutputNumber.setText(String.valueOf(friend.getPhoneNumber()));
        }
    }
}
