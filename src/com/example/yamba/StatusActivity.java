package com.example.yamba;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity{
	private EditText editStatus;
	private Button buttonTweet;
	private static final String TAG =  "StatusActivity";
	private TextView textCount;
	private int defaultTextColor;
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		editStatus = (EditText)findViewById(R.id.editStatus);
		buttonTweet = (Button)findViewById(R.id.buttonTweet);
		textCount = (TextView)findViewById(R.id.textCount);

		buttonTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String status = editStatus.getText().toString();
				Log.d(TAG, "onClicked with status: " + status);
				new PostTask().execute(status);
			}
		});
		
		ColorStateList colors = textCount.getTextColors();
		defaultTextColor = colors.getDefaultColor();
		editStatus.addTextChangedListener(new TextWatcher() {	
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}	
			@Override
			public void afterTextChanged(Editable arg0) {
				int count = 140 - editStatus.length();
				textCount.setText(Integer.toString(count));
				textCount.setTextColor(Color.GREEN);
				if(count < 10)
					textCount.setTextColor(Color.RED);
				else
					textCount.setTextColor(defaultTextColor);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private final class PostTask extends
			AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			try{
				prefs = PreferenceManager.getDefaultSharedPreferences(StatusActivity.this);
				String username = prefs.getString("username", "");
				String password = prefs.getString("password", "");
				if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
					StatusActivity.this.startActivity(
							new Intent(StatusActivity.this, SettingsActivity.class));
					return "Empty words!!!";
				}
				YambaClient cloud = new YambaClient(username, password);
				cloud.postStatus(params[0]);
				return "Send Successful";
			}catch(YambaClientException e){
				e.printStackTrace();
				return "Send Failed";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, 
					Toast.LENGTH_LONG).show();
		}
		
		
	}

}
