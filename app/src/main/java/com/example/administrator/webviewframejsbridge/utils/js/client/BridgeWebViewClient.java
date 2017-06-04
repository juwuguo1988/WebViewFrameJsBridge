package com.example.administrator.webviewframejsbridge.utils.js.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.administrator.webviewframejsbridge.utils.js.utils.BridgeUtil;
import com.example.administrator.webviewframejsbridge.utils.js.webview.BridgeWebView;
import com.github.lzyzsd.jsbridge.Message;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juwuguo on 2017/5/22.
 */

public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;
    private Context mContext;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    public BridgeWebViewClient(BridgeWebView webView, Context mContext) {
        this.webView = webView;
        this.mContext = mContext;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) {
            webView.flushMessageQueue();
            return true;
        } else {
            Map<String, String> additionalHttpHeaders = new HashMap<>();
            //additionalHttpHeaders.put("Authorization", "Bearer " + UserInfoUtils.getAccessToken(mContext));
            view.loadUrl(url, additionalHttpHeaders);
            return true;
        }

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (BridgeWebView.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
        }

        //
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}
