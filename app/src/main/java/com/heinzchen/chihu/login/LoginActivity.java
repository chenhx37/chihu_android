package com.heinzchen.chihu.login;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.heinzchen.chihu.R;
import com.heinzchen.chihu.main.MainActivity;
import com.heinzchen.chihu.protocol.Chihu;
import com.heinzchen.chihu.register.RegisterCustomerActivity;
import com.heinzchen.chihu.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/3/12.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mUsername;
    private EditText mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.to_register).setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    private String checkFields() {
        if (mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {
            return "用户名和密码不能为空";
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish: {
                if (null != checkFields()) {
                    Toast.makeText(LoginActivity.this, checkFields(), Toast.LENGTH_SHORT).show();
                    break;
                }

                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                LoginProcessor.getInstance().login(username, password);

                break;
            }
            case R.id.cancel: {
                finish();
                break;
            }
            case R.id.to_register: {
                RegisterCustomerActivity.jumpToMe(this);
            }
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(final LoginResultMessage msg) {
        switch (msg.result) {
            case LoginResultMessage.SUCCEED: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, msg.msg, Toast.LENGTH_SHORT).show();
                    }
                });
                MainActivity.jumpToMe(this);
                finish();
                break;
            }
            case LoginResultMessage.FAIL: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, msg.msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            default:
                break;
        }
    }

    public static void jumpToMe(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
