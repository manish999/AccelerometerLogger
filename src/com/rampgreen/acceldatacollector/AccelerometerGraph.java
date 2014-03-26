//package com.rampgreen.acceldatacollector;
//
//import java.util.ArrayList;
//import java.util.Timer;
//
//import org.achartengine.GraphicalView;
//
//import android.app.Activity;
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//
//public class AccelerometerGraph extends Activity implements SensorEventListener {
//
//	public boolean init = false;
//	SensorManager sMngr;
//	Sensor snsr;
//
//	Context context;
//	Timer tmr;
//
//	ProgressBar pb;
//	public static LinearLayout ll;
//	Button btnStart;
//	Button btnStop;
//
//	Graph mGraph;
//	GraphicalView view;
//
//	ArrayList<Double> x, y, z;
//
//	Graph graph;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		ll = (LinearLayout) findViewById(R.id.chart_containerAcc);
//		sMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		snsr = (Sensor) sMngr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//		x = new ArrayList<Double>();
//		y = new ArrayList<Double>();
//		z = new ArrayList<Double>();
//
//		btnStart = (Button) findViewById(R.id.startTimer);
//		btnStart.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				sMngr.registerListener(AccelerometerGraph.this, snsr,
//						SensorManager.SENSOR_DELAY_NORMAL);
//			}
//		});
//		mGraph = new Graph(this);
////		btnStop = (Button) findViewById(R.id.stop);
////		btnStop.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View arg0) {
////				sMngr.unregisterListener(AccelerometerGraph.this);
////			}
////		});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu, menu);
//		return true;
//	}
//
//	@Override
//	public void onAccuracyChanged(Sensor arg0, int arg1) {
//
//	}
//
//	int capacityHTC= 0;
//	
//	
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		x.add((double) event.values[0]);
//		y.add((double) event.values[1]);
//		z.add((double) event.values[2]);
//		
//		mGraph.initData(x, y, z);
//		mGraph.setProperties();
//		if (!init) {
//			view = mGraph.getGraph();
//			ll.addView(view);
//			init = true;
//			capacityHTC++;
//		} else {
//			ll.removeView(view);
//			view = mGraph.getGraph();
//			ll.addView(view);
//			capacityHTC++;
//		}
//		Log.e("HTC ACCELEORMETER", capacityHTC+"");
//	}
//
//}
