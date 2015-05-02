package com.xudafeng.jsbridge_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class WebViewActivity extends Activity implements Callback,
		OnClickListener {
	private static final String TAG = "jsbridge";
	private static final String PREFIX = TAG + "://";
	private static final String BLANK = "";
	private static final String JSFILENAME = "JSBridge.js";
	private String distUrl = "";
	private Button leftButton;
	private TextView title;
	private WebView webView;
	private int isShow = View.VISIBLE;
	private String leftText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (distUrl == null || distUrl.equals("")) {
			distUrl = intent.getStringExtra("url");
		}
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_webview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		initView();
		Log.i(TAG, "start loading...");
	}

	public void setDistUrl(String distUrl) {
		this.distUrl = distUrl;
	}

	public void hideLeftButton() {
		isShow = View.INVISIBLE;
	}
	
	public void setLeftText(String text) {
		leftText = text;
	}

	public void initView() {
		leftButton = (Button) findViewById(R.id.button_first);
		leftButton.setOnClickListener(this);
		leftButton.setVisibility(isShow);
		title = (TextView) findViewById(R.id.title);

		if (leftText != null) {
			leftButton.setText(leftText);
		}

		initWebview();
	}

	HashMap<String, String> parseQuery(String querystring) {
		HashMap<String, String> res = new HashMap<String, String>();
		try {
			String text = URLDecoder.decode(querystring, "UTF-8");
			String[] arr = text.split("\\&");

			for (int i = 0; i < arr.length; i++) {
				String[] temp = arr[i].split("\\=");
				res.put(temp[0], temp[1]);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	};

	void setTitle(String text) {
		String t = (String) parseQuery(text).get("title");
		title.setText(t);
	};

	void pushView(String text) {
		String url = (String) parseQuery(text).get("url");
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	};

	void popView() {
		finish();
	};

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	void initWebview() {
		webView = (WebView) findViewById(R.id.webviewo);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.JSBridge.methods.setTitle(document.title);");
				super.onPageFinished(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith(PREFIX)) {
					url = url.replace(PREFIX, BLANK);
					String[] temp = url.split("\\?");
					String method = temp[0];
					HashMap<String, Integer> hash = new HashMap<String, Integer>();
					hash.put("setTitle", 0);
					hash.put("pushView", 1);
					hash.put("popView", 2);

					int i = (Integer) hash.get(method);
					switch (i) {
					case 0:
						setTitle(temp[1]);
						break;
					case 1:
						pushView(temp[1]);
						break;
					case 2:
						popView();
						break;
					default:
						break;
					}
					return true;
				}
				return true;
			}
		});

		InputStream in = null;
		try {
			in = webView.getContext().getAssets().open(JSFILENAME);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;
			StringBuilder sb = new StringBuilder();
			do {
				line = bufferedReader.readLine();
				if (line != null && !line.matches("^\\s*\\/\\/.*")) {
					sb.append(line);
				}
			} while (line != null);

			bufferedReader.close();
			in.close();
			webView.loadUrl("javascript:" + sb.toString());
			webView.loadUrl(distUrl);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_first:
			popView();
			break;
		case R.id.button_second:
			break;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
}