package com.komect.showroom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.komect.showroom.dagger.AppComponent;
import com.komect.showroom.event.ActivityStartEvent;
import com.komect.showroom.event.GlobalMsgEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xieyusheng on 2017/9/30.
 */

public class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_BUNDLE = "bundle";

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressed() {
        dismissAlertDialog();
        dismissProgressDialog();
        super.onBackPressed();
    }


    public AppComponent getAppComponent() {
        return AppComponent.getInstance(getApplicationContext());
    }


    /**
     * 显示进度对话框
     *
     * @param msg 对话框的文案信息
     */
    public void showProgressDialog(CharSequence msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, null, msg);
            mProgressDialog.setCancelable(true);
        } else {
            mProgressDialog.setMessage(msg);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }


    /**
     * 隐藏正在显示的进度对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * 显示弹框提示
     *
     * @param msg 对话框的文案信息
     */
    public void showAlertDialog(CharSequence msg) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setPositiveButton("确定", null)
                    .create();
        } else {
            mAlertDialog.setMessage(msg);
        }
        mAlertDialog.show();
    }


    /**
     * 显示弹框提示
     *
     * @param msg 对话框的文案信息
     */
    public void showAlertDialog(CharSequence msg, final DialogInterface.OnClickListener onPositiveClick) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setPositiveButton("确定", onPositiveClick)
                    .create();
        } else {
            mAlertDialog.setMessage(msg);
        }
        mAlertDialog.show();
    }


    /**
     * 隐藏弹框提示
     */
    public void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }


    /**
     * 弹出对话框，并设置"确认"按钮的点击事件
     *
     * @param title 标题文案
     * @param msg 内容文案
     * @param onPositiveClick 确认按钮点击事件
     */
    public void showAlertDialog(CharSequence title, CharSequence msg,
                                final DialogInterface.OnClickListener onPositiveClick) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", onPositiveClick)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
    }


    /**
     * 接收到EventBus发送的事件，处理UI消息提示的显示
     *
     * @param event 包含了提示信息和显示方式的EventBus事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalMsg(GlobalMsgEvent event) {
        dismissProgressDialog();
        if (event.getDisplayType() == GlobalMsgEvent.DisplayType.Toast) {
            Toast.makeText(this, "" + event.getMsg(), Toast.LENGTH_SHORT).show();
        } else if (event.getDisplayType() == GlobalMsgEvent.DisplayType.Dialog) {
            if (event.isCloseDialog()) {
                dismissAlertDialog();
            } else {
                showAlertDialog(event.getMsg());
            }
        }
    }


    /**
     * 接收到EventBus事件处理Activity页面跳转
     *
     * @param event 包含了页面跳转参数的EventBus事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityStartEvent(ActivityStartEvent event) {
        if (event == null) {
            //do nothing
            return;
        }
        //启动新活动
        if (event.getTargetActivityCls() != null) {
            Intent intent = new Intent(this, event.getTargetActivityCls());
            if (event.getIntentFlags() > 0) {
                intent.setFlags(event.getIntentFlags());
            }
            if (event.getBundle() != null) {
                intent.putExtra(EXTRA_BUNDLE, event.getBundle());
            }
            if (event.getRequestCode() > 0) {
                startActivityForResult(intent, event.getRequestCode());
            } else {
                startActivity(intent);
            }
        }
        //关闭当前活动
        if (event.isFinishCurrentActivity()) {
            if (event.getResult() == RESULT_OK) {
                setResult(RESULT_OK);
            }
            finish();
        }
    }
}
