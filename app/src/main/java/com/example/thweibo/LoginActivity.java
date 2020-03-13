package com.example.thweibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText mAccount;
    EditText mPassword;
    CheckBox mSavePassword;
    TextView mGoRegister;
    Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mSavePassword = (CheckBox) findViewById(R.id.remember_pass);
        mGoRegister = (TextView) findViewById(R.id.go_register);
        mLoginBtn = (Button) findViewById(R.id.login);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccount(mAccount.getText().toString());
                setSavePassword(mSavePassword.isChecked());
                if (mSavePassword.isChecked()) {
                    setPassword(mPassword.getText().toString());
                } else {
                    setPassword("");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = login(mAccount.getText().toString(), mPassword.getText().toString());
                            Looper.prepare();
                            if (result == 1) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else if (result == 0) {
                                Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (JSONException jse) {
                            jse.printStackTrace();
                        } finally {

                        }
                    }
                }).start();
            }
        });
        mGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAccount.setText(getAccount());
        mPassword.setText(getPassword());
        mSavePassword.setChecked(getSavePassword());
    }

    private int login(String username, String password) throws IOException, JSONException {
        String urlStr = "http://10.0.2.2/weibo/login.php";
        String params = "username=" + username + "&password=" + password;
        String requestMethod = "POST";
        String responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
        Log.i(TAG, "login: " + responseText);
        JSONObject userInfo = new JSONObject(responseText);
        return userInfo.getInt("status");
    }

    private void setAccount(String account) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString("username", account);
        editor.commit();
    }

    private void setPassword(String password) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString("password", password);
        editor.commit();
    }

    private void setSavePassword(boolean savePassword) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putBoolean("savePassword", savePassword);
        editor.commit();
    }

    private String getAccount() {
        SharedPreferences pre = getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getString("username", "");
    }

    private String getPassword() {
        SharedPreferences pre = getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getString("password", "");
    }

    private boolean getSavePassword() {
        SharedPreferences pre = getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getBoolean("savePassword", false);
    }
}
