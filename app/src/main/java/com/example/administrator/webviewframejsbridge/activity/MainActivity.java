package com.example.administrator.webviewframejsbridge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import com.example.administrator.webviewframejsbridge.R;
import com.example.administrator.webviewframejsbridge.utils.js.client.BridgeWebViewClient;
import com.example.administrator.webviewframejsbridge.utils.js.webview.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private BridgeWebView wv_web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initWebView();
    }

    private void initData() {
        wv_web_view = (BridgeWebView) findViewById(R.id.wv_web_view);
        configWebView();
        wv_web_view.loadUrl("file:///android_asset/jsdemo.html");
    }

    private void configWebView() {
        wv_web_view.setVisibility(View.VISIBLE);
        wv_web_view.setWebViewClient(new BridgeWebViewClient(wv_web_view, this));
        // set HadlerCallBack
        wv_web_view.setDefaultHandler(new myHadlerCallBack());
    }

    private void initWebView() {
        wv_web_view.setVisibility(View.VISIBLE);
        //必须和js函数名字一致，注册好具体执行回调函数，类似java实现类。
        wv_web_view.registerHandler("callAndoridNativeNetworkHandle", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("medicEdit", data);
                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
                function.onCallBack(data);
            }

        });

        //必须和js函数名字一致，注册好具体执行回调函数，类似java实现类。
        wv_web_view.registerHandler("transferDataToAndroidNativeHandle", new BridgeHandler() {
            @Override
            public void handler(String dataObj, CallBackFunction function) {
                //当返回数据是1，关闭当前activity
                try {
                    JSONObject jsonObject = new JSONObject(dataObj);
                    int data =jsonObject.getInt("data");
                    if (data == 1) {
                        finish();
                    }
                } catch (Exception e) {
                    finish();
                }

            }

        });

        wv_web_view.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
            }
        });

    }


    /**
     * 自定义回调
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if (function != null) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
