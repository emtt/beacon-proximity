package com.c20.dlm;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME = "DLMPref";

	private static final String MAJOR = "Major";
	private static final String MINOR = "Minor";
	private static final String DISTANCE = "Distance";
	
	public Session(Context context){
		this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
	}
	
	public void SaveSession(int _Major, int _Minor, int _Distance){
		editor.putInt(MAJOR, _Major);
		editor.putInt(MINOR, _Minor);
		editor.putInt(DISTANCE, _Distance);
		editor.commit();
	}
	
	public HashMap<String, String> getUserDetails(){
		
		HashMap<String, String> usuario = new HashMap<String, String>();
		usuario.put(MAJOR, String.valueOf(pref.getInt(MAJOR, 0)));
		usuario.put(MINOR, String.valueOf(pref.getInt(MINOR, 0)));
		usuario.put(DISTANCE, String.valueOf(pref.getInt(DISTANCE, 0)));
		
		return usuario;
	}
	
	
}

