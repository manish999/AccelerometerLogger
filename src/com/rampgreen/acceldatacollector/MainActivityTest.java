package com.rampgreen.acceldatacollector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rampgreen.acceldatacollector.util.AppLog;

public class MainActivityTest extends Activity  
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.logging_new);

		initUI();
		
	}
	

	private void disableAllToggleButton() {
		buttonRunning.setEnabled(false);
		buttonWalking.setEnabled(false);
		buttonSitting.setEnabled(false);
		buttonSitting.setEnabled(false);
		buttonclimbingUp.setEnabled(false);
		buttonClimbingDown.setEnabled(false);
	}

	private void enableAllToggleButton() {
		buttonRunning.setEnabled(true);
		buttonWalking.setEnabled(true);
		buttonSitting.setEnabled(true);
		buttonSitting.setEnabled(true);
		buttonclimbingUp.setEnabled(true);
		buttonClimbingDown.setEnabled(true);
	}
	
	ProgressBar runningBar1; 
	ProgressBar runningBar2;
	ProgressBar runningBar3;
	Button buttonRunning;
	
	ProgressBar walkingBar1;
	ProgressBar walkingBar2;
	ProgressBar walkingBar3;
	Button buttonWalking;
	
	ProgressBar sittingBar1;
	ProgressBar sittingBar2;
	ProgressBar sittingBar3;
	Button buttonSitting;
	
	ProgressBar climbingUpBar1;
	ProgressBar climbingUpBar2;
	ProgressBar climbingUpBar3;
	Button buttonclimbingUp;
	
	ProgressBar climbingDownBar1;
	ProgressBar climbingDownBar2;
	ProgressBar climbingDownBar3;
	Button buttonClimbingDown;
	
	private void initUI() {
		LinearLayout layoutRunning = (LinearLayout)findViewById(R.id.barbutton1);
		LinearLayout layoutWalking = (LinearLayout)findViewById(R.id.barbutton2);
		LinearLayout layoutSitting = (LinearLayout)findViewById(R.id.barbutton3);
		LinearLayout layoutClimbingUp = (LinearLayout)findViewById(R.id.barbutton4);
		LinearLayout layoutClimbingDown = (LinearLayout)findViewById(R.id.barbutton5);
		
		runningBar1 = (ProgressBar)layoutRunning.findViewById(R.id.indicator1);
		runningBar2 = (ProgressBar)layoutRunning.findViewById(R.id.indicator2);
		runningBar3 = (ProgressBar)layoutRunning.findViewById(R.id.indicator3);
		buttonRunning = (Button)layoutRunning.findViewById(R.id.btn_start_stop);
		
		walkingBar1 = (ProgressBar)layoutWalking.findViewById(R.id.indicator1);
		walkingBar2 = (ProgressBar)layoutWalking.findViewById(R.id.indicator2);
		walkingBar3 = (ProgressBar)layoutWalking.findViewById(R.id.indicator3);
		buttonWalking = (Button)layoutWalking.findViewById(R.id.btn_start_stop);
		
		sittingBar1 = (ProgressBar)layoutSitting.findViewById(R.id.indicator1);
		sittingBar2 = (ProgressBar)layoutSitting.findViewById(R.id.indicator2);
		sittingBar3 = (ProgressBar)layoutSitting.findViewById(R.id.indicator3);
		buttonSitting = (Button)layoutSitting.findViewById(R.id.btn_start_stop);
		
		climbingUpBar1 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator1);
		climbingUpBar2 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator2);
		climbingUpBar3 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator3);
		buttonclimbingUp = (Button)layoutClimbingUp.findViewById(R.id.btn_start_stop);
		
		climbingDownBar1 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator1);
		climbingDownBar2 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator2);
		climbingDownBar3 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator3);
		buttonClimbingDown = (Button)layoutClimbingDown.findViewById(R.id.btn_start_stop);
		
		buttonRunning.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					AppLog.e("running buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
				} else{
					AppLog.e("running buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
				}
			}
		});
		
		buttonWalking.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					AppLog.e("buttonWalking buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
				} else{
					AppLog.e("buttonWalking buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
				}
			}
		});
		
		buttonSitting.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					AppLog.e("buttonSitting buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
				} else{
					AppLog.e("buttonSitting buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
				}
			}
		});
		
		
		buttonclimbingUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					AppLog.e("buttonclimbingUp buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
				} else{
					AppLog.e("buttonclimbingUp buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
				}
			}
		});
		
		
		buttonClimbingDown.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					AppLog.e("buttonClimbingDown buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
				} else{
					AppLog.e("buttonClimbingDown buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
				}				
			}
		});
	}
}
