package com.clubclient.clubclient;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NotifyActivity extends Activity{

	private Button startNotify;
	private Vibrator mVibrator;
	private LocationClient mLocationClient;
	private NotiftLocationListener listener;
	private double longtitude,latitude;
	private NotifyLister mNotifyLister;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify);
		listener = new NotiftLocationListener();
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		
		startNotify = (Button)findViewById(R.id.notifystart);
		mLocationClient  = new LocationClient(this);
		mLocationClient.registerLocationListener(listener);
		startNotify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(startNotify.getText().toString().equals("����λ������")){
					mLocationClient.start();
					startNotify.setText("�ر�λ������");
				}else{
					if(mNotifyLister!=null){
						mLocationClient.removeNotifyEvent(mNotifyLister);
						startNotify.setText("����λ������");
					}
					
				}
					
				
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationClient.removeNotifyEvent(mNotifyLister);
		mLocationClient = null;
		mNotifyLister= null;
		listener = null;
		
	}

	private Handler notifyHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mNotifyLister = new NotifyLister();
			mNotifyLister.SetNotifyLocation(latitude,longtitude, 3000,"gcj02");//4��������Ҫλ�����ѵĵ����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ�����ϵ����(gcj02,gps,bd09,bd09ll)
			mLocationClient.registerNotify(mNotifyLister);
		}
		
	};
	public class NotiftLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			longtitude = location.getLongitude();
			latitude = location.getLatitude();
			notifyHandler.sendEmptyMessage(0);
		}
	}
	public class NotifyLister extends BDNotifyListener{
	    public void onNotify(BDLocation mlocation, float distance){
	    	mVibrator.vibrate(1000);//�������ѵ��趨λ�ø���
	    	Toast.makeText(NotifyActivity.this, "������", Toast.LENGTH_SHORT).show();
	    }
	}
}
