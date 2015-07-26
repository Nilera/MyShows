package me.myshows.android.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.myshows.android.R;
import me.myshows.android.api.StorageMyShowsClient;
import me.myshows.android.api.impl.Credentials;
import me.myshows.android.api.impl.MyShowsClientImpl;
import me.myshows.android.api.impl.PreferenceStorage;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Whiplash
 * @date 14.06.2015
 */
public class LoginActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://myshows.me/";
    private static final int ANIMATION_DURATION = 500;

    private View logo;
    private ViewGroup loginLayout;

    private boolean needAnimate;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StorageMyShowsClient client = MyShowsClientImpl.get(new PreferenceStorage(getApplicationContext()),
                AndroidSchedulers.mainThread());

        logo = findViewById(R.id.logo);
        loginLayout = (ViewGroup) findViewById(R.id.login_layout);

        TextView newAccount = (TextView) findViewById(R.id.new_account);
        setupNewAccountTextView(newAccount);

        findViewById(R.id.login_button).setOnClickListener(view -> {
            String login = ((EditText) findViewById(R.id.login)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            Credentials credentials = Credentials.make(login, password);
            processAuthenticationObserver(client.authentication(credentials));
        });

        if (client.hasCredentials()) {
            if (hasInternetConnection()) {
                needAnimate = true;
                setEnabled(loginLayout, false);
                processAuthenticationObserver(client.authentication());
            } else {
                changeActivity();
            }
        } else {
            logo.setTranslationY(0);
            loginLayout.setAlpha(1);
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void setupNewAccountTextView(TextView view) {
        String register = getString(R.string.register);
        String newAccount = getString(R.string.new_account, register);
        int start = newAccount.indexOf(register);
        int end = start + register.length();
        SpannableString ss = new SpannableString(newAccount);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REGISTER_URL));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.red_80_opacity));
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(ss, TextView.BufferType.SPANNABLE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setEnabled(ViewGroup parent, boolean enabled) {
        parent.setEnabled(enabled);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                setEnabled((ViewGroup) child, enabled);
            } else {
                child.setEnabled(enabled);
            }
        }
    }

    private void animate() {
        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, 0);
        ObjectAnimator loginLayoutAnimator = ObjectAnimator.ofFloat(loginLayout, View.ALPHA, 1);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(logoAnimator, loginLayoutAnimator);
        animation.setDuration(ANIMATION_DURATION);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setEnabled(loginLayout, true);
                needAnimate = false;
            }
        });
        animation.start();
    }

    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void processAuthenticationObserver(Observable<Boolean> observable) {
        subscription = observable.subscribe(result -> {
            if (result) {
                changeActivity();
            } else {
                if (needAnimate) {
                    animate();
                } else {
                    Toast.makeText(this, R.string.incorrect_login_or_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
