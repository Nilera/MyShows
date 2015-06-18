package me.myshows.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import me.myshows.android.api.MyShowsClient;

/**
 * @author Whiplash
 * @date 14.06.2015
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (MyShowsClient.get(this).isLogin()) {
            userHasLogin();
        } else {
            findViewById(R.id.loginButton).setOnClickListener(view -> {
                String login = ((EditText) findViewById(R.id.login)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                MyShowsClient.get(this).authentication(login, password, isLogin -> {
                    if (isLogin) {
                        userHasLogin();
                    } else {
                        Toast.makeText(this, R.string.incorrect_login_or_password, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    private void userHasLogin() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
