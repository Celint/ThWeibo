package com.example.thweibo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddBlogActivity extends AppCompatActivity {

    private static final String TAG = "AddBlogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_blog_toolbar);
        final EditText mAddBlog = (EditText) findViewById(R.id.add_blog);
        toolbar.inflateMenu(R.menu.menu_send);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.send) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int result = send(mAddBlog.getText().toString(), "");
                                Looper.prepare();
                                if (result == 1) {
                                    Toast.makeText(AddBlogActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddBlogActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                                Looper.loop();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } catch (JSONException jse) {
                                jse.printStackTrace();
                            }
                        }
                    }).start();
                }
                return true;
            }
        });
    }

    private int send(String content, String imagePath) throws IOException, JSONException {
        String urlStr = "http://10.0.2.2/weibo/send.php";
        String params = "username=" + getUsername() + "&content=" + content + "&imagePath=" + imagePath;
        String requestMethod = "POST";
        String responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
        Log.i(TAG, "send: " + responseText);
        JSONObject jsonObject = new JSONObject(responseText);
        int status = jsonObject.getInt("status");
        return status;
    }

    private String getUsername() {
        SharedPreferences pre = getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getString("username", "");
    }
}
