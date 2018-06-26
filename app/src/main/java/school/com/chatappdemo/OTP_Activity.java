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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import school.com.chatappdemo.Chat.Login;
import school.com.chatappdemo.Chat.Users;
import school.com.chatappdemo.Model.UserData;

public class OTP_Activity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1, e2;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    String verification_code;
    private DatabaseReference mFirebaseDatabase;
    String phone;
    String userId;
    Button btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        e1 = (EditText) findViewById(R.id.phone);
        e2 = (EditText) findViewById(R.id.smscode);

        btn_verify = findViewById(R.id.button3);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.d("error", "error " + e.toString());
                Toast.makeText(OTP_Activity.this, "On Failure" + e.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verification_code = s;
                Toast.makeText(getApplicationContext(), "Code Sent to the number", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void send_sms(View view) {

        String number = e1.getText().toString();
        if(number.length() >= 10) {
            if (!number.startsWith("+91")) {
                number = "+91" + number;
            }
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number, 60, TimeUnit.SECONDS, this, mCallBack
            );
        } else {
            Toast.makeText(OTP_Activity.this,"Please enter 10 digit phone number",Toast.LENGTH_LONG).show();
        }
    }

    public void signInWithPhone(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "UserData Signed in Successfully", Toast.LENGTH_SHORT).show();

                    if (TextUtils.isEmpty(userId)) {
                        userId = mFirebaseDatabase.push().getKey();
                    }
                    //if (!TextUtils.isEmpty(phone))
                    phone = e1.getText().toString();
                    createUser("", "", phone, "");

                    SharedPreferences preferences = getSharedPreferences("chatApp",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("phone",phone);
                    editor.commit();
                    Intent i=new Intent(OTP_Activity.this,ChooseActivity.class);
                    i.putExtra("userKey",userId);
                    startActivity(i);
                    finish();
                    //startActivity(new Intent(OTP_Activity.this, Users.class));

                } else {
                    Toast.makeText(getApplicationContext(), "User Couldn't  Sign in Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void verify(View view) {
        String input_code = e2.getText().toString();
        //verifyPhoneNumber(verification_code, input_code);
        if (input_code!=null && input_code.length()>0 && !verification_code.equals("")) {
            verifyPhoneNumber(verification_code, input_code);
        } else {
            Toast.makeText(getApplicationContext(),"Please Enter a Valid Code", Toast.LENGTH_LONG).show();
        }
    }

    public void verifyPhoneNumber(String verifyCode, String input_code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, input_code);
        signInWithPhone(credential);
    }

    private void createUser(String name, String email, String phone, String image) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        UserData user = new UserData(name, email, phone, image);
        mFirebaseDatabase.child(userId).setValue(user);
    }
}
