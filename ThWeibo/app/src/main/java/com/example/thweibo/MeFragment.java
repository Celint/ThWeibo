package com.example.thweibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        TextView username = (TextView) root.findViewById(R.id.username);
        Button mLogout = (Button) root.findViewById(R.id.logout);
        username.setText(getUsername());
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return root;
    }

    private String getUsername() {
        SharedPreferences pre = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        return pre.getString("username", "");
    }
}