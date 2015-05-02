package com.xudafeng.jsbridge_android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends WebViewActivity implements Callback {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		hideLeftButton();
		setDistUrl("file:///android_asset/index.html");
		super.onCreate(savedInstanceState);
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
