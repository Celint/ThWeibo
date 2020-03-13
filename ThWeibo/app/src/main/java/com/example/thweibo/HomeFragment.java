package com.example.thweibo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private List<MyBlog> blogs = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    public void onStart() {
        super.onStart();
        new FetchBlogsTask().execute();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    startActivity(new Intent(getActivity(), AddBlogActivity.class));
                }
                return true;
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BlogAdapter(blogs));

        return root;
    }

    public class BlogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id;
        TextView user_nickname;     // 用户名
        TextView pub_time;          // 发微博时间
        TextView blog_text;         // 微博内容

        public BlogHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            user_nickname = (TextView) view.findViewById(R.id.user_nickname);
            pub_time = (TextView) view.findViewById(R.id.pub_time);
            blog_text = (TextView) view.findViewById(R.id.blog_text);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("blog", blogs.get(id));
            startActivity(intent);
        }
    }

    public class BlogAdapter extends RecyclerView.Adapter<BlogHolder> {

        private List<MyBlog> myBlogs;

        public BlogAdapter (List<MyBlog> blogs) {
            myBlogs = blogs;
        }

        @Override
        public BlogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myblog_item, parent, false);
            BlogHolder holder = new BlogHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BlogHolder holder, int position) {
            MyBlog blog = myBlogs.get(position);
            holder.id = position;
            holder.user_nickname.setText(blog.getNickname());
            holder.pub_time.setText(blog.getDatetime());
            holder.blog_text.setText(blog.getContent());
        }

        @Override
        public int getItemCount() {
            return myBlogs.size();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            recyclerView.setAdapter(new BlogAdapter(blogs));
        }
    }

    private class FetchBlogsTask extends AsyncTask<Void, Void, List<MyBlog>> {

        @Override
        protected List<MyBlog> doInBackground(Void... voids) {
            return fetchblogs();
        }

        @Override
        protected void onPostExecute(List<MyBlog> myblogs) {
            blogs = myblogs;
            setupAdapter();
        }
    }

    private List<MyBlog> fetchblogs() {
        List<MyBlog> blogs = new ArrayList<>();
        String urlStr = "http://10.0.2.2/weibo/fetchblogs.php";
        String params = "";
        String requestMethod = "POST";
        String responseText;
        try {
            responseText = new MyHttpRequest().sendHttpRequest(urlStr, params, requestMethod);
            Log.i(TAG, "fetchblogs: " + responseText);
            JSONObject object = new JSONObject(responseText);
            JSONArray array = object.getJSONArray("blogs");
            for (int i = 0; i < array.length(); i++) {
                JSONObject blogObject = array.getJSONObject(i);
                MyBlog blog = new MyBlog();
                blog.setWeiboId(blogObject.getInt("id"));
                blog.setNickname(blogObject.getString("username"));
                blog.setContent(blogObject.getString("content"));
                blog.setDatetime(blogObject.getString("pubtime"));
                blogs.add(blog);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jse) {
            jse.printStackTrace();
        } finally {
            return blogs;
        }
    }
}
