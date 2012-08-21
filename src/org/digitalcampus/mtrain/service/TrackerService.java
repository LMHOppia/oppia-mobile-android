package org.digitalcampus.mtrain.service;

import org.digitalcampus.mtrain.application.DbHelper;
import org.digitalcampus.mtrain.application.MTrain;
import org.digitalcampus.mtrain.task.Payload;
import org.digitalcampus.mtrain.task.SubmitMQuizTask;
import org.digitalcampus.mtrain.task.SubmitTrackerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.bugsense.trace.BugSenseHandler;

public class TrackerService extends Service {

	public static final String TAG = "TrackerService";

	private final IBinder mBinder = new MyBinder();

	@Override
	public void onCreate() {
		super.onCreate();
		BugSenseHandler.setup(this, MTrain.BUGSENSE_API_KEY);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean backgroundData = true;
		Bundle b = intent.getExtras();
		if (b != null) {
			backgroundData = b.getBoolean("backgroundData");
		}

		if (isOnline() && backgroundData) {
			DbHelper db = new DbHelper(this);

			Payload p = db.getUnsentLog();
			SubmitTrackerTask stt = new SubmitTrackerTask(this);
			stt.execute(p);

			Payload mqp = db.getUnsentMquiz();
			SubmitMQuizTask smqt = new SubmitMQuizTask(this);
			smqt.execute(mqp);

			db.close();

		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		public TrackerService getService() {
			return TrackerService.this;
		}
	}

	public boolean isOnline() {
		getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

}