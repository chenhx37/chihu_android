package com.heinzchen.chihu.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.heinzchen.chihu.login.LoginActivity;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chen on 2016/3/3.
 */
public class RegisterCustomerActivity extends Activity implements View.OnClickListener {
    public static final String TAG = RegisterCustomerActivity.class.getSimpleName();

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MLog.i(TAG, "onCreate");
        setContentView(R.layout.activity_register_customer);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.email);

        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(final RegisterCustomerResponseMessage msg) {
        MLog.i(TAG, "onEvent(RegisterCustomerResponseMessage msg)");
        switch (msg.result) {
            case EventBusMessage.SUCCEED: {
                MLog.i(TAG, "result = succeed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg.msg, Toast.LENGTH_SHORT).show();
                    }
                });

                LoginActivity.jumpToMe(this);
                finish();
                break;
            }
            case EventBusMessage.FAIL: {
                MLog.i(TAG, "result = fail");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg.msg, Toast.LENGTH_SHORT).show();
                    }
                });


                break;
            }

        }

        if (RegisterCustomerResponseMessage.FAIL == msg.result) {

        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish: {
                MLog.i(TAG, "clicked");
                //完成注册
                String msg = checkFields();
                if (null == msg) {
                    MLog.i(TAG, "null==msg");
                    String username = mUsername.getText().toString();
                    String password = mPassword.getText().toString();
                    String email = mEmail.getText().toString();
                    RegisterCustomerProcessor.getInstance().registerUser(username, password, email, "SunYatSanUniversity");

//                    Chihu.UserAccount.Builder builder = Chihu.UserAccount.newBuilder();
//                    builder.setUsername(username);
//                    builder.setPassword(password);
//                    builder.setEmail(email);
//
//                    Chihu.UserAccount userAccount = builder.build();
//                    NetworkManager.getInstance().requestRegisterUser(userAccount);

                } else {
                    //把msg展示出来
                    MLog.i(TAG, msg);

                }
                break;
            }
            case R.id.cancel: {
                finish();
                break;
            }
        }
    }

    /**
     *检查需要填写的field是否符合要求
     * @return
     */
    private String checkFields() {
        String msg;

        String username = mUsername.getText().toString();
        if (username.equals("")) {
            msg = "用户名不能为空";
            MLog.e(TAG, username);
            return msg;
        }
        if (!username.matches("[a-zA-Z0-9]+")) {
            msg = "用户名只能包含字母、数字";
            MLog.e(TAG, username);
            return msg;
        }

        String password = mPassword.getText().toString();
        if (password.equals("")) {
            msg = "密码不能为空";
            MLog.e(TAG, password);
            return msg;
        }
        if (password.contains(" ")) {
            msg = "密码不能包含空格," + password;
            MLog.e(TAG, password);
            return msg;
        }

        String email = mEmail.getText().toString();
        if (email.equals("")) {
            msg = "邮箱不能为空";
            MLog.e(TAG, email);
            return msg;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            msg = "邮箱格式不正确";
            MLog.e(TAG, email);
            return msg;
        }


        return null;
    }

    public static void jumpToMe(Context context) {
        Intent intent = new Intent(context, RegisterCustomerActivity.class);
        context.startActivity(intent);
    }
}
