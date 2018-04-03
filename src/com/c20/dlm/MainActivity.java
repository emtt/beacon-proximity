package com.c20.dlm;

import java.util.HashMap;

import com.androidquery.AQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends Activity {

	AQuery aq;
	Session session;
	SeekBar seekBar;
	int valDistance;
	int _minor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Init();

	}

	public void Init(){
		aq = new AQuery(this);
		session = new Session(this);

		if(!isBluetoothAvailable()){
			new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.noBLTitle))
			.setMessage(getResources().getString(R.string.noBLMsg))
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					// NADA :)
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();
			return;
		}

		//CONTINUE
		//GET PREFERENCES
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		
		HashMap<String, String> user = session.getUserDetails();
		
		aq.id(R.id.txtMajor).text(user.get("Major"));
		aq.id(R.id.txtMinor).text(user.get("Minor"));
		aq.id(R.id.textView3).text(getResources().getString(R.string.txtDistance) + ": " +user.get("Distance")+ "mts");
		seekBar.setProgress(Integer.parseInt(user.get("Distance")));
		_minor = Integer.parseInt(user.get("Minor"));
		//
		aq.id(R.id.btnSave).clicked(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				session.SaveSession(Integer.parseInt(aq.id(R.id.txtMajor).getText().toString()),
						Integer.parseInt(aq.id(R.id.txtMinor).getText().toString()),
						valDistance);
				
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
				
			}

		});
		
		aq.id(R.id.btnInit).clicked(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(_minor != 0){
					
					startService(new Intent(MainActivity.this, PMService.class));
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.srvInit), Toast.LENGTH_LONG).show();
					
				}
				
			}

		});

		
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progress = progresValue;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				valDistance = progress;
				aq.id(R.id.textView3).text(getResources().getString(R.string.txtDistance) + ": " +valDistance + "mts");
				
			}

		});
	}

	//VERIFICAR SI BLUETOOTH ESTÁ HABILITADO
	public static boolean isBluetoothAvailable() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				return false;
			}else{
				return true;
			}
		}
	}

}
