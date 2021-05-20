package org.prototype.angkotku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.prototype.angkotku.model.User;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    ConstraintLayout rootLayout;
    TextView textViewLogin, textViewRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        rootLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Login");
        dialog.setMessage("Please use email to register");
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        EditText editEmail = login_layout.findViewById(R.id.editTextTextEmailAddress);
        EditText editPass = login_layout.findViewById(R.id.editTextTextPassword);
        TextView goToRegister = login_layout.findViewById(R.id.textViewLogin);
        dialog.setView(login_layout);

        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                btnLogin.setEnabled(false);
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Nama tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editPass.getText().toString())) {
                    Snackbar.make(rootLayout, "Password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                auth.signInWithEmailAndPassword(editEmail.getText().toString(), editPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        waitingDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, Welcome.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout, "Failed!\n" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Register");
        dialog.setMessage("Please use email to register");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        EditText editName = register_layout.findViewById(R.id.editTextTextPersonName);
        EditText editUsername = register_layout.findViewById(R.id.editTextUsername);
        EditText editEmail = register_layout.findViewById(R.id.editTextTextEmailAddress);
        EditText editNoHp = register_layout.findViewById(R.id.editTextPhone);
        EditText editPass = register_layout.findViewById(R.id.editTextTextPassword);
        EditText editConf = register_layout.findViewById(R.id.editTextTextPassword2);

        dialog.setView(register_layout);

        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Snackbar.make(rootLayout, "Nama tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editUsername.getText().toString())) {
                    Snackbar.make(rootLayout, "Username tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Email tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editNoHp.getText().toString())) {
                    Snackbar.make(rootLayout, "No Hp tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editPass.getText().toString()) || TextUtils.isEmpty(editConf.getText().toString())) {
                    Snackbar.make(rootLayout, "Password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!editPass.getText().toString().equals(editConf.getText().toString())) {
                    Snackbar.make(rootLayout, "Password harus sama", Snackbar.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User();
                        user.setEmail(editEmail.getText().toString());
                        user.setUsername(editUsername.getText().toString());
                        user.setNama(editName.getText().toString());
                        user.setNoHp(editNoHp.getText().toString());
                        user.setPassword(editPass.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Snackbar.make(rootLayout, "Berhasil!\nSilahkan Login Ya", Snackbar.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                Snackbar.make(rootLayout, "Failed!\n" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Snackbar.make(rootLayout, "Failed!+\n" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}