package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    Button btndangnhap;
    Button btnForgetPass;
    Button btndangki;
    EditText editEmail, editPass;
    FirebaseAuth mAuthencation;
    FirebaseUser user;
    String tem="null";
    String status = "null";
    DatabaseReference reference;
    String avatar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthencation = FirebaseAuth.getInstance();
        if(mAuthencation.getCurrentUser()!=null){
            Intent intent_to_dangnhap = new Intent(MainActivity.this,My_message_window.class);
            intent_to_dangnhap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent_to_dangnhap.putExtra("name",tem);
            startActivity(intent_to_dangnhap);
        }
        else {


            Anhxa();

            btndangnhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DangNhap();
                }
            });
            btndangki.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_to_dangki = new Intent(MainActivity.this, DangkiActivity.class);
                    startActivity(intent_to_dangki);
                }
            });
            btnForgetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo:
                    final String email = editEmail.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplication(), "Bạn phải nhập email ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Bạn có chắc muốn reset mật khẩu ?");
                    final CharSequence[] items={"Ok"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (items[i].equals("Ok")) {

                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(MainActivity.this, "Mật khẩu đã được gửi đến mail của bạn!", Toast.LENGTH_SHORT).show();


                                                } else {
                                                    Toast.makeText(MainActivity.this, "Không thể xác thực email", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            });
        }

    }
    public void Anhxa(){
        btndangnhap = (Button) findViewById(R.id.btndangnhap);
        btndangki   = (Button) findViewById(R.id.btndangki);
        editEmail   = (EditText) findViewById(R.id.edtuser);
        editPass    = (EditText) findViewById(R.id.edtpass);
        btnForgetPass = findViewById(R.id.btnForgetPass);
        editEmail.setText("vu@gmail.com");
        editPass.setText("123456");

    }
    private void DangNhap(){
        String TaiKhoan = editEmail.getText().toString();
        final String MatKhau = editPass.getText().toString();
        mAuthencation.signInWithEmailAndPassword(TaiKhoan, MatKhau)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = mAuthencation.getCurrentUser();
                            Toast.makeText(MainActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                            Intent intent_to_dangnhap = new Intent(MainActivity.this,My_message_window.class);
                            intent_to_dangnhap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent_to_dangnhap.putExtra("resetPass",editPass.getText().toString());
                            startActivity(intent_to_dangnhap);
                            finish();

                        }

                        else {
                            Toast.makeText(MainActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
