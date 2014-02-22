package com.example.yamba;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.
					getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null){
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		}catch(Exception e){
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.action_tweet:
			startActivity(new Intent("com.example.yamba.action.tweet"));
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
