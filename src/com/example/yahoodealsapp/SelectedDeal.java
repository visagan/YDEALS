package com.example.yahoodealsapp;





import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;



public class SelectedDeal extends Activity {
	// XML node keys
		static final String KEY_SONG = "song"; // parent node
		static final String KEY_ID = "id";
		static final String KEY_TITLE = "title";
		static final String KEY_ARTIST = "artist";
		static final String KEY_DURATION = "duration";
		static final String KEY_THUMB_URL = "thumb_url";
		
		
		/*DEALS GLOBAL VALUES */
		static final String DEAL_ROWS = "DEALS";
		static final String DEAL_NAME = "DEALNAME";
		static final String DEAL_COMPANY_NAME = "COMPANYNAME";
		static final String DEAL_RATING = "RATING";
		static final String DEAL_ADDRESS = "ADDRESS";
		static final String DEAL_LATITUDE = "LAT";
		static final String DEAL_LONGITUDE = "LONG";
		static final String DEAL_IMG_URL = "IMAGE_URL";
		static final String DISTANCE_TO_DEAL = "DISTANCE";
		static final String DEAL_CITY = "CITY";
		static final String DEAL_STATE = "STATE";
		static final String DEAL_PHONE = "PHONE";
		private static final float THRESHOLD = 0;
		

		/*END OF DEALS GLOBAL VALUES*/
		
		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_deal);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get XML values from previous intent
        String name = in.getStringExtra(DEAL_NAME);
        String companyName = in.getStringExtra(DEAL_COMPANY_NAME);
        String description = in.getStringExtra(DEAL_RATING);
        String url = in.getStringExtra(DEAL_IMG_URL);
        String latitude = in.getStringExtra(DEAL_LATITUDE);
        String longitude = in.getStringExtra(DEAL_LONGITUDE);
        String Address = in.getStringExtra(DEAL_ADDRESS);
        String City = in.getStringExtra(DEAL_CITY);
        String State = in.getStringExtra(DEAL_STATE);
        String Phone = in.getStringExtra(DEAL_PHONE);
        String distance = in.getStringExtra(DISTANCE_TO_DEAL);
        
        String AddressWhole = "Contact:\n"+Address+"\n"+City+"\n"+State +"\n" +Phone;
        
        
        TextView dealCompany = (TextView) findViewById(R.id.dealCompany);
        TextView dealName = (TextView) findViewById(R.id.dealName);
        TextView dealDistance = (TextView) findViewById(R.id.dealDistance);
        TextView dealAddress = (TextView) findViewById(R.id.dealAddress);
        ImageView dealBarCode = (ImageView) findViewById(R.id.dealBarCode);
        
        // Displaying all values on the screen
        /*
        
        TextView dealRating = (TextView) findViewById(R.id.dealRating);
       
        
      
        */
    
        dealCompany.setText(companyName);
        dealName.setText(name);
        dealDistance.setText("Distance to Deal: " +distance );
        dealAddress.setText(AddressWhole);
        dealBarCode.setAlpha(127);
        //Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("selectedImage");  
        ImageView image = (ImageView)findViewById(R.id.list_image_selected);
        //image.setImageBitmap(bitmap);
        /*
        
        dealRating.setText("Rating: "+description);
        
        lblDistance
		.setText(String.valueOf(determineEvent(latitude, longitude)));
         
        Bitmap bitmap=ImageLoader.memoryCache.get(url);
        if(bitmap!=null)
            image.setImageBitmap(bitmap);
        
         */
        
        
        addActionListener(latitude, longitude);
	}

	class MyListener implements View.OnClickListener {

		String latitude, longitude;

		public MyListener(String latitude, String longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		@Override
		public void onClick(View v) {
			String uri = "geo:"+ latitude + "," + longitude;
	    	startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		}

	};
	
	private void addActionListener(String latitude, String longitude) {
		/*Button button= (Button) findViewById(R.id.getDirections);
		button.setOnClickListener(new MyListener(latitude, longitude));
		*/
	}
	public enum Event {
		CLICK, CONVERSION, ACQUISTION
	}
	
	private void notifyServer(String name, String cost, String latitude,
			String longitude) {
		// TODO Auto-generated method stub
		Event event = determineEvent(latitude, longitude);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = null;
		switch (event) {
		case CLICK:
			httppost = new HttpPost("http://www.yoursite.com/script.php");
			break;
		case CONVERSION:
			httppost = new HttpPost("http://www.yoursite.com/script.php");
			break;
		case ACQUISTION:
			httppost = new HttpPost("http://www.yoursite.com/script.php");
			break;
		}

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", "12345"));
			nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	@SuppressWarnings("null")
	private Event determineEvent(String latitude, String longitude) {

		LocationManager currentLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = currentLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		//
		Location location = currentLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		float[] results = new float[1];
		System.out.println(Double.valueOf(latitude));
		
		if(location != null) {
		Location.distanceBetween(Double.valueOf(latitude),
				Double.valueOf(longitude), location.getLatitude(),
				location.getLongitude(), results);
		Log.w("myapp", String.valueOf(results[0]));
		}
		return results[0] > THRESHOLD ? Event.CLICK : Event.CONVERSION;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_deal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
	  SelectedDeal.this.finish();
	  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	  //YahooDealsMain.stationsContainer.setVisibility(View.VISIBLE);
	}
}
