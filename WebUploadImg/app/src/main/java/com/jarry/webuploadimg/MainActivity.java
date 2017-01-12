package com.jarry.webuploadimg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jarry.webuploadimg.widget.ProgressWebView;

public class MainActivity extends AppCompatActivity {
    ProgressWebView webView;
    static final String TAG = "MainActivity";
    final String url = "https://test.doraemoney.com/newCube/SourceTestPage.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (ProgressWebView) findViewById(R.id.webview);
        initData();
    }

    private void initData() {
        webView.loadUrl(url);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                return true;
            } else {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode=" + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == ProgressWebView.TYPE_CAMERA) { // 相册选择
                webView.onActivityCallBack(true, null);
            } else if (requestCode == ProgressWebView.TYPE_GALLERY) {// 相册选择
                if (data != null) {
                    Uri uri = data.getData();
                    Log.d(TAG, "uri=" + uri);
                    if (uri != null) {
                        webView.onActivityCallBack(false, uri);
                    } else {
                        Toast.makeText(MainActivity.this, "获取数据为空", Toast.LENGTH_LONG).show();
                    }
                }
            }

        }
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ProgressWebView.TYPE_REQUEST_PERMISSION) {
            webView.toCamera();// 到相机
        }
    }
}

