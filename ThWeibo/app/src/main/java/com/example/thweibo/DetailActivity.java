package com.example.thweibo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    MyBlog blog;

    private List<Comment> comments = new ArrayList<>();

    RecyclerView recyclerView;
    TextView detail_nickname;
    TextView detail_time;
    TextView detail_text;
    Button detail_delete;
    EditText detail_comment;
    Button detail_comment_btn;

    String commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        blog = getIntent().getParcelableExtra("blog");
        detail_nickname = (TextView) findViewById(R.id.detail_nickname);
        detail_time = (TextView) findViewById(R.id.detail_time);
        detail_text = (TextView) findViewById(R.id.detail_text);
        detail_delete = (Button) findViewById(R.id.detail_delete);
        detail_comment = (EditText) findViewById(R.id.detail_comment);
        detail_comment_btn = (Button) findViewById(R.id.detail_comment_btn);
        detail_comment_btn.setVisibility(View.GONE);
        detail_comment.setHint(getUsername() + " 表达你的观点...");
        detail_nickname.setText(blog.getNickname());
        detail_time.setText(blog.getDatetime());
        detail_text.setText(blog.getContent());
        recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CommentAdapter(comments));
        if (getUsername().equals(blog.getNickname())) {
            detail_delete.setVisibility(View.VISIBLE);
        } else {
            detail_delete.setVisibility(View.GONE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                comments.addAll(fetchComments());
                setupAdapter();
            }
        }).start();
        detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBlog();
            }
        });
        detail_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    detail_comment_btn.setVisibility(View.VISIBLE);
                } else {
                    detail_comment_btn.setVisibility(View.GONE);
                }
            }
        });
        detail_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail_comment.getText().toString().equals("")) {
                    Toast.makeText(DetailActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                } else {
                    commentText = detail_comment.getText().toString();
                    new FetchCommentsTask().execute();
                }
            }
        });
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        int id;
        TextView comment_nickname;  // 用户名
        TextView comment_content;   // 评论内容
        TextView comment_time;      // 评论时间

        public CommentHolder(View itemView) {
            super(itemView);
            comment_nickname = (TextView) itemView.findViewById(R.id.comment_nickname);
            comment_content = (TextView) itemView.findViewById(R.id.comment_text);
            comment_time = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        private List<Comment> commentList;

        public CommentAdapter(List<Comment> comments) {
            commentList = comments;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            CommentHolder holder = new CommentHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.id = position;
            holder.comment_nickname.setText(comment.getNickname());
            holder.comment_content.setText(comment.getContent());
            holder.comment_time.setText(comment.getCommentTime());
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }

    private void setupAdapter() {
        recyclerView.setAdapter(new CommentAdapter(comments));
    }

    private class FetchCommentsTask extends AsyncTask<Void, Boolean, List<Comment>> {

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            publishProgress(sendComment(commentText));
            Log.e("print", "chenggong");
            return fetchComments();
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if (!values[0]) {
                Toast.makeText(DetailActivity.this, "评论失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } else {
                detail_comment.setText("");
            }
        }

        @Override
        protected void onPostExecute(List<Comment> commentList) {
            comments.clear();
            comments.addAll(commentList);
            setupAdapter();
        }
    }

    private List<Comment> fetchComments() {
        String urlStr = "http://10.0.2.2/weibo/fetchComments.php";
        String params = "weibo_id=" + blog.getWeiboId();
        String requestMethod = "POST";
        List<Comment> commentList = new ArrayList<>();
        try {
            String responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
            Log.i("fetchComments", "fetchComments: " + responseText);
            JSONObject object = new JSONObject(responseText);
            JSONArray array = object.getJSONArray("comments");
            for (int i = 0; i < array.length(); i++) {
                JSONObject commentObject = array.getJSONObject(i);
                Comment comment = new Comment();
                comment.setCommentId(commentObject.getInt("comment_id"));
                comment.setWeiboId(commentObject.getInt("weibo_id"));
                comment.setNickname(commentObject.getString("username"));
                comment.setContent(commentObject.getString("comment_content"));
                comment.setCommentTime(commentObject.getString("comment_time"));
                commentList.add(comment);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jse) {
            jse.printStackTrace();
        } finally {
            return commentList;
        }
    }

    public void deleteBlog() {
        builder = new AlertDialog.Builder(this)
                .setTitle("删除这条微博？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (delete()) {
                                    Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(1000);
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(DetailActivity.this, "删除失败, 请稍后重试", Toast.LENGTH_LONG).show();
                                }
                                Looper.loop();
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private boolean delete() {
        String urlStr = "http://10.0.2.2/weibo/delete.php";
        String params = "username=" + blog.getNickname() + "&id=" + blog.getWeiboId();
        String requestMethod = "POST";
        String responseText;
        try {
            responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
            Log.i("Delete", "delete: " + responseText);
            JSONObject object = new JSONObject(responseText);
            if (object.getInt("status") == 1) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return false;
        }
    }

    private boolean sendComment(String comment) {
        String urlStr = "http://10.0.2.2/weibo/send_comment.php";
        String params = "weibo_id=" + blog.getWeiboId() + "&username=" + getUsername() + "&comment=" + comment;
        String requestMethod = "POST";
        try {
            String responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
            Log.i("sendComment", "sendComment: " + responseText);
            JSONObject object = new JSONObject(responseText);
            if (object.getInt("status") == 1) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return false;
        }
    }

    private String getUsername() {
        SharedPreferences pre = getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getString("username", "");
    }
}
