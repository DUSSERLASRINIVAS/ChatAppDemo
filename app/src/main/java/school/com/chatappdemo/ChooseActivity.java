package school.com.chatappdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import school.com.chatappdemo.Chat.Users;

public class ChooseActivity extends AppCompatActivity {

    Button bt_chat, bt_edit;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        bt_edit = (Button) findViewById(R.id.bt_edit);
        bt_chat = (Button) findViewById(R.id.bt_chat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userKey = extras.getString("userKey");
        }

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, EditProfileActivity.class);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
            }
        });

        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ChooseActivity.this, Chat_Activity.class);
                Intent intent = new Intent(ChooseActivity.this, Users.class);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
            }
        });

    }
}
