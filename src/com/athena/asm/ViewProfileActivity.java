package com.athena.asm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.athena.asm.data.Profile;
import com.athena.asm.util.SmthSupport;
import com.athena.asm.util.StringUtility;
import com.athena.asm.util.task.LoadProfileTask;
import com.athena.asm.util.vt100.Vt100TerminalModel;

public class ViewProfileActivity extends SherlockActivity {

    public SmthSupport m_smthSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(aSMApplication.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        m_smthSupport = SmthSupport.getInstance();

        String userID = getIntent().getStringExtra(StringUtility.USERID);

        LoadProfileTask loadProfileTask = new LoadProfileTask(this, userID);
        loadProfileTask.execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // do nothing to stop onCreated
        super.onConfigurationChanged(newConfig);
    }

    public void reloadProfile(final Profile profile) {
        if (profile != null) {
            ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);
            ((RelativeLayout) searchButton.getParent()).setVisibility(View.GONE);

            Button msgButton = (Button) findViewById(R.id.btn_send_mail);
            msgButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(StringUtility.URL, "http://www.newsmth.net/bbspstmail.php");
                    bundle.putSerializable(StringUtility.WRITE_TYPE, 1);
                    bundle.putSerializable(StringUtility.IS_REPLY, false);
                    bundle.putSerializable(StringUtility.USERID, profile.getUserID());

                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClassName("com.athena.asm", WritePostActivity.class.getName());
                    startActivity(intent);
                }
            });



            TextView userIDTextView = (TextView) findViewById(R.id.profile_userid);
            userIDTextView.setText(profile.getUserIDNickName());

            TextView userScoreTextView = (TextView) findViewById(R.id.profile_user_score);
            if (profile.getScore() != 0) {
                userScoreTextView.setText("积分:" + profile.getScore());
            } else {
                userScoreTextView.setVisibility(View.GONE);
            }

            TextView userIpTextView = (TextView) findViewById(R.id.profile_user_ip);
            userIpTextView.setText("来自:" + profile.getIp());

            TextView descTextView = (TextView) findViewById(R.id.profile_user_desc);
            Vt100TerminalModel.handleContent(profile.getDescription(), descTextView, "");

            TextView aliveTextView = (TextView) findViewById(R.id.profile_aliveness);
            aliveTextView.setText(profile.getAliveness() + "");
            TextView loginedTimeTextView = (TextView) findViewById(R.id.profile_login_times);
            loginedTimeTextView.setText(profile.getLoginTime() + "");
            TextView postNoTextView = (TextView) findViewById(R.id.profile_post_number);
            postNoTextView.setText(profile.getPostNumber() + "");
            TextView onlineTextView = (TextView) findViewById(R.id.profile_online_status);
            switch (profile.getOnlineStatus()) {
            case 0:
                onlineTextView.setText("离线");
                break;
            case 1:
                onlineTextView.setText("不明");
                break;
            case 2:
                onlineTextView.setText("在线");
                break;

            default:
                break;
            }

            setTitle(R.string.title_profile);
        }
    }
}
