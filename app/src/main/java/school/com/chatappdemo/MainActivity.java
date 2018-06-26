package school.com.chatappdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import school.com.chatappdemo.Model.UserData;

public class MainActivity extends AppCompatActivity {
    Button login, register;
    EditText phoneNumber;
    private DatabaseReference mFirebaseDatabase;
    Map<String, String> userDetails = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneNumber.getText().toString();
                if (phone!=null && phone.length()==10) {
                    mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> userFound = verifyPhoneNumber(phone, dataSnapshot);
                            String userKey = userFound.get("userKey");
                            String userValid = userFound.get("userFound");

                            if(userValid!=null && "true".equalsIgnoreCase(userValid)){
                                Intent intent=new Intent(MainActivity.this,ChooseActivity.class);
                                intent.putExtra("userKey",userKey);
                                startActivity(intent);
                                finish();
                            } else{
                                Toast.makeText(MainActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else{
                    Toast.makeText(MainActivity.this, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,OTP_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private Map<String,String> verifyPhoneNumber(String phone, DataSnapshot dataSnapshot){
        final String phoneNum = phone;
        Map<String,String> map = new HashMap<String, String>();
        String userFound = "false";
        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
            String userKey = childDataSnapshot.getKey();
            UserData userDt = childDataSnapshot.getValue(UserData.class);
            String phoneNo = userDt.getPhone();
            if(phoneNum.equalsIgnoreCase(phoneNo)){
                map.put("userKey",userKey);
                map.put("userFound","true");
                break;
            }
        }
        return map;
    }
}
