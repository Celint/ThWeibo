package com.example.thweibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    ImageView mUploadIcon;
    EditText mSetAccount;
    EditText mSetPassword;
    EditText mSetPassword2;
    Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUploadIcon = (ImageView) findViewById(R.id.upload_icon);
        mSetAccount = (EditText) findViewById(R.id.set_account);
        mSetPassword = (EditText) findViewById(R.id.set_password);
        mSetPassword2 = (EditText) findViewById(R.id.set_password2);
        mRegisterBtn = (Button) findViewById(R.id.register);

        mUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "上传头像", Toast.LENGTH_SHORT).show();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String account = mSetAccount.getText().toString();
                final String password = mSetPassword.getText().toString();
                String password2 = mSetPassword2.getText().toString();
                if (password.equals("") || password2.equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(password2)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int result = register(account, password, "");
                                    Looper.prepare();
                                    if (result == 1) {
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (result == 2) {
                                        Toast.makeText(RegisterActivity.this, "此账号已被注册", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
                    } else {
                        Toast.makeText(RegisterActivity.this, "前后密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int register(String username, String password, String iconPath) throws IOException, JSONException {
        String urlStr = "http://10.0.2.2/weibo/register.php";
        String params = "username=" + username + "&password=" + password + "&iconPath=" + iconPath;
        String requestMethod = "POST";
        String responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
        JSONObject userInfo = new JSONObject(responseText);
        String nickName = userInfo.getString("username");
        String passwd = userInfo.getString("password");
        String icon = userInfo.getString("iconPath");
        int status = userInfo.getInt("status");
        if (status == 1) {
            setAccount(nickName);
            setPassword(passwd);
            setIconPath(icon);
        }
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

    private void setIconPath(String iconPath) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString("iconPath", iconPath);
        editor.commit();
    }
}
