package com.example.organizze.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.activity.config.ConfiguracaoFirebase;
import com.example.organizze.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText login_edtEmail, login_edtSenha;
    private Button login_btnEntrar;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_edtEmail = findViewById(R.id.login_edtEmail);
        login_edtSenha = findViewById(R.id.login_edtSenha);
        login_btnEntrar = findViewById(R.id.login_btnEntrar);

        login_btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = login_edtEmail.getText().toString();
                String senha = login_edtSenha.getText().toString();

                //validar se os campos foram preenchidos
                    if ( !email.isEmpty() ) {
                        if ( !senha.isEmpty() ) {
                            usuario = new Usuario();
                            usuario.setEmail( email );
                            usuario.setSenha( senha );

                            validarLogin();
                        } else {
                            Toast.makeText(LoginActivity.this, "Insira uma senha", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Insira um email", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        login_edtSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                        login_btnEntrar.performClick();
                        return true;
                }
                return false;
            }
        });

    }

    public void validarLogin(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    abrirTelaPrincipal();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado";
                    } catch ( FirebaseAuthInvalidCredentialsException e ) {
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}