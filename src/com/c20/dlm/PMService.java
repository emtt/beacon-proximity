package com.c20.dlm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PMService extends Service implements BeaconConsumer {

	private BeaconManager beaconManager;
	private static int COUNT = 1;
	private int count;
	private List<Beacon> beaconsEncontrados;
	private Notification n;
	Session session;
	String _minor, _distance;
	Context _context;
	@Override
	public void onCreate() {
		super.onCreate();

		beaconManager = BeaconManager.getInstanceForApplication(this);
		beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
		beaconManager.bind(this);
		count = 0;
		beaconsEncontrados = new ArrayList();
		session = new Session(this);
		HashMap<String, String> user = session.getUserDetails();
		_minor = user.get("Minor");
		_distance = user.get("Distance");
		_context = this;
	}

	@Override
	public void onBeaconServiceConnect() {
		beaconManager.setRangeNotifier(new RangeNotifier() {
			@Override 
			public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
				if (beacons.size() > 0) {

					for(Beacon beacon:beacons) {

						Log.wtf("BeaconService", " Minor: "+ beacon.getId3() + " Distance: " + beacon.getDistance());

						if(beacon.getId3().toInt() == Integer.parseInt(_minor) && beacon.getDistance() > Double.parseDouble(_distance)){

							Intent intent = new Intent(_context, AlertActivity.class);
							PendingIntent pIntent = PendingIntent.getActivity(_context, 0, intent, 0);
							Uri alarmSound = Uri.parse("android.resource://com.c20.dlm/" + R.raw.alarmdlm);

							Log.wtf("BeaconService", " Beacon Encontrado");
							//abrir intent o notificar
							n  = new Notification.Builder(_context)
							.setContentTitle("Beacon encontrado")
							.setContentText("Beacon encontrado")
							.setSmallIcon(R.drawable.ic_dlmicon)
							.setContentIntent(pIntent)
							.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
							.setLights(Color.WHITE, 3000, 3000)
							.setSound(alarmSound)
							.setAutoCancel(true)
							.addAction(R.drawable.ic_dlmicon, "ALEJADO DE DISPOSITIVO", pIntent).build();
							NotificationManager notificationManager = 
									(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

							notificationManager.notify(0, n); 

							break;
						}

					}
				}

			}
		});

		try {
			beaconManager.startRangingBeaconsInRegion(new Region("Test", null, null, null));
		} catch (RemoteException e) {    }
	}

	public void processList(final List<Beacon> beacons){

		HashMap<String, String> user = session.getUserDetails();

		for(Beacon beacon:beacons) {

			Log.wtf("BeaconService", " Minor: "+ beacon.getId3() + " Distance: " + beacon.getDistance());

			if(beacon.getId3().toInt() == Integer.parseInt(user.get("Minor")) && beacon.getDistance() > Double.parseDouble(user.get("Distance"))){

				Intent intent = new Intent(this, AlertActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
				Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

				Log.wtf("BeaconService", " Beacon Minor 56678 Encontrado");
				//abrir intent o notificar
				n  = new Notification.Builder(this)
				.setContentTitle("Beacon 56678 encontrado")
				.setContentText("Beacon encontrado")
				.setSmallIcon(R.drawable.ic_dlmicon)
				.setContentIntent(pIntent)
				.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
				.setLights(Color.WHITE, 3000, 3000)
				.setSound(alarmSound)
				.setAutoCancel(true)
				.addAction(R.drawable.ic_dlmicon, "Ver Oferta-Anuncio", pIntent).build();
				NotificationManager notificationManager = 
						(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

				notificationManager.notify(0, n); 

				break;
			}

		}
		count = 0;
		beaconsEncontrados.clear();
	}

	@Override
	public void onDestroy() {
		beaconManager.unbind(this);
		super.onDestroy();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
