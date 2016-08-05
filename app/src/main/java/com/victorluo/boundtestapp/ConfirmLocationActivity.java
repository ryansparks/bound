package com.victorluo.boundtestapp;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class ConfirmLocationActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
	private static final int PLACE_PICKER_REQUEST_CODE = 1;

	private TextView locationOut;

	private GoogleApiClient mGoogleApiClient = null;

	boolean hasLocationPermission = false;
	boolean hasRegisteredForUpdates = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_location);

		locationOut = (TextView) findViewById(R.id.location_out);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.addApi(Places.PLACE_DETECTION_API)
					.build();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
			hasLocationPermission = false;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
			}
		} else {
			hasLocationPermission = true;
			startLocationFetch();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(hasRegisteredForUpdates) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			hasRegisteredForUpdates = false;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
			hasLocationPermission = true;
			startLocationFetch();
		} else {
			Toast.makeText(this, "No location permission granted!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void startLocationFetch() {
		if(mGoogleApiClient.isConnected() && hasLocationPermission){
			fetchLocation();
			findViewById(R.id.place_pick).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
					try {
						startActivityForResult(builder.build(ConfirmLocationActivity.this), PLACE_PICKER_REQUEST_CODE);
					} catch (Exception ignored) {}
				}
			});
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(this, data);
				Toast.makeText(this, String.format("Place: %s", place.getName()), Toast.LENGTH_LONG).show();
			}
		}
	}

	@SuppressWarnings("MissingPermission")
	private void fetchLocation(){
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			locationOut.setText("From last location: " + String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
		}

		LocationRequest req = new LocationRequest();
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
		hasRegisteredForUpdates = true;
	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		startLocationFetch();
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {
		Toast.makeText(this, "Failed to connect to Play Services!", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void onLocationChanged(Location location) {
		locationOut.setText("From update: " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
	}
}
