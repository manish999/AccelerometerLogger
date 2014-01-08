package com.rampgreen.acceldatacollector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
	
public class DummySectionFragment extends Fragment implements SensorEventListener 
{

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	TextView title,tv,tv1,tv2;
	RelativeLayout layout;
	private com.rampgreen.acceldatacollector.Listener fastestListener;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public DummySectionFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		 fastestListener = new Listener(this);
		 mSensorManager.registerListener(fastestListener, mAccelerometer,
	                SensorManager.SENSOR_DELAY_FASTEST);
		//		//get layout
		//		layout = (RelativeLayout)findViewById(R.id.relative);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_main_dummy,
				container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		//get textviews
		title=(TextView) rootView.findViewById(R.id.name);   
		tv=(TextView) rootView.findViewById(R.id.xval);
		tv1=(TextView) rootView.findViewById(R.id.yval);
		tv2=(TextView) rootView.findViewById(R.id.zval);

		dummyTextView.setText(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		 fastestListener.startRecording();

	}


	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// Do something here if sensor accuracy changes.
	}

	@Override
	public final void onSensorChanged(SensorEvent event) 
	{
		// Many sensors return 3 values, one for each axis.
		float x =  event.values[0];
		float y =  event.values[1];
		float z =  event.values[2];


		//display values using TextView
		title.setText(R.string.app_name);
		tv.setText("X axis" +"\t\t"+x);
		tv1.setText("Y axis" + "\t\t" +y);
		tv2.setText("Z axis" +"\t\t" +z);

	}
	
	 public void displayRates() {
//       button1.setEnabled(true);

		 title.setText(String.format(
               "Sampling rate: %.2f", fastestListener.getSamplingRate()));
//               String.format("Normal rate: %.2f\nUI rate: %s\nGame rate: %s\nFastest rate: %s\n",
//                       normalListener.getSamplingRate(),
//                       uiListener.getSamplingRate(),
//                       gameListener.getSamplingRate(),
//                       fastestListener.getSamplingRate()));
   }


	@Override
	public void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}