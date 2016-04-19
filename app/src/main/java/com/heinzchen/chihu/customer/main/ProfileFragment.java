package com.heinzchen.chihu.customer.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.R;
import com.heinzchen.chihu.account.ProfileManager;
import com.heinzchen.chihu.login.LoginActivity;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/7.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = ProfileFragment.class.getSimpleName();

    private View mModify;
    private View mSave;
    private View mCancel;

    private EditText mUsername;
    private EditText mEmail;
    private EditText mNetID;
    private EditText mPhone;
    private EditText mReceiver;
    private EditText mAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mModify = view.findViewById(R.id.profile_modify);
        mModify.setOnClickListener(this);

        mSave = view.findViewById(R.id.profile_save);
        mSave.setOnClickListener(this);

        mCancel = view.findViewById(R.id.profile_cancel);
        mCancel.setOnClickListener(this);

        mUsername = (EditText) view.findViewById(R.id.profile_username);
        mEmail = (EditText) view.findViewById(R.id.profile_email);
        mPhone = (EditText) view.findViewById(R.id.profile_phone_number);
        mReceiver = (EditText) view.findViewById(R.id.profile_receiver);
        mAddress = (EditText) view.findViewById(R.id.profile_address);
        mNetID = (EditText) view.findViewById(R.id.profile_netid_account);

        view.findViewById(R.id.logout).setOnClickListener(this);

        showData();


        return view;
    }

    private void showData() {
        final Chihu.Profile profile = ProfileManager.getInstance().getCachedProfile();
        if (null != profile) {//get data from sp
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProfile(profile);
                }
            });
        } else {//get data from server
            ProfilePresenter.getInstance().getProfileFromServer();
        }
    }

    private void setEditable(boolean editable) {
        mEmail.setEnabled(editable);
        mPhone.setEnabled(editable);
        mReceiver.setEnabled(editable);
        mAddress.setEnabled(editable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_modify: {
                MLog.i(TAG, "profile_modify");
                mModify.setVisibility(View.GONE);
                mSave.setVisibility(View.VISIBLE);
                mCancel.setVisibility(View.VISIBLE);

                setEditable(true);
                break;
            }
            case R.id.profile_save: {
                MLog.i(TAG, "profile_save");
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String phone = mPhone.getText().toString();
                String receiver = mReceiver.getText().toString();
                String address = mAddress.getText().toString();
                String netId = mNetID.getText().toString();

                ProfilePresenter.getInstance().updateProfile(username, email, netId, phone, receiver, address);

                mModify.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                setEditable(false);

                break;
            }
            case R.id.profile_cancel: {
                MLog.i(TAG, "profile_cancel");
                mModify.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);

                setEditable(false);
                break;
            }
            case R.id.logout: {
                ProfileManager.getInstance().logout();
                LoginActivity.jumpToMe(getActivity());
                getActivity().finish();
                break;
            }

            default:
                break;
        }
    }

    private void setProfile(Chihu.Profile profile) {
        mUsername.setText(profile.getUsername());
        mEmail.setText(profile.getEmail());
        mNetID.setText(profile.getNetid());
        mReceiver.setText(profile.getReceiver());
        mAddress.setText(profile.getAddress());
        mPhone.setText(profile.getPhone());
    }

    @Subscribe
    public void onEvent(final GetProfileMessage msg) {
        if (null != msg.profile) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProfile(msg.profile);
                }
            });
        }
    }

    @Subscribe
    public void onEvent(UpdateProfileResultMessage msg) {
        switch (msg.status) {
            case EventBusMessage.SUCCEED: {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CApplication.GLOBAL_CONTEXT, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case EventBusMessage.FAIL: {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CApplication.GLOBAL_CONTEXT, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            default:
                break;
        }
    }
}
