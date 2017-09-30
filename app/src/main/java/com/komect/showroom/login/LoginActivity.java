package com.komect.showroom.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.komect.showroom.MainActivity;
import com.komect.showroom.R;

/**
 * A login screen.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mAccount;
    private EditText mPasswordView;

    private LoginPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAccount = (EditText) findViewById(R.id.account);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    private void attemptLogin() {
        if (mPresenter != null) {
            return;
        }

        // Reset errors.
        mAccount.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccount.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(account)) {
            mAccount.setError(getString(R.string.error_field_required));
            focusView = mAccount;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccount.setError(getString(R.string.error_invalid_username));
            focusView = mAccount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mPresenter = new LoginPresenter();
            mPresenter.login(account, password);
            loginResult();
        }
    }


    private boolean isAccountValid(String account) {
        return account.length() > 0;
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }


    public void loginResult() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

