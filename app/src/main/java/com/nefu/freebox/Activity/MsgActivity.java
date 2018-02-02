package com.nefu.freebox.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nefu.freebox.Adapter.Adapter_Msg_Msg;
import com.nefu.freebox.Entity.Msg;
import com.nefu.freebox.R;

import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends AppCompatActivity {

    public static final String MSG_USER_IMAGE = "msg_user_image";
    public static final String MSG_USER_NAME = "msg_user_name";

    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private Adapter_Msg_Msg adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_msg);
        Intent intent = getIntent();
        String Msg_userName = intent.getStringExtra(MSG_USER_NAME);
        int Msg_userImage = intent.getIntExtra(MSG_USER_IMAGE, 0);
        Toolbar toolbar = findViewById(R.id.activity_msg_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Msg_userName);
        }
        initMsgs();
        editText = (EditText) findViewById(R.id.msg_input);
        send = (Button) findViewById(R.id.msg_send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.activity_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter_Msg_Msg(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    editText.setText("");
                }
            }
        });
    }

    private void initMsgs(){
        Msg msg1 = new Msg("Test.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Test!", Msg.TYPE_SENT);
        msgList.add(msg2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
