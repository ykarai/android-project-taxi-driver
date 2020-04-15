package com.example.yosk7.drivertaxiapp.controller;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yosk7.drivertaxiapp.R;
import com.example.yosk7.drivertaxiapp.model.backend.BackendFactory;
import com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager;
import com.example.yosk7.drivertaxiapp.model.datasource.Utils;
import com.example.yosk7.drivertaxiapp.model.entities.Client;
import com.example.yosk7.drivertaxiapp.model.entities.Driver;
import com.example.yosk7.drivertaxiapp.model.entities.Locationf;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.notifyToDriversList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.notifyToclientsList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.stopNotifyToDriverList;
import static com.example.yosk7.drivertaxiapp.model.datasource.Firebase_DBManager.stopNotifyToclientList2;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String PREFS_NAME = "driverSharedPreferences";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    boolean finishLodingDataFlag = false;

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    Location locationA;

    Driver currentDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //Locatin manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationA = new Location("A");//= new Location(from);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                locationA = location;
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        getSupportActionBar().setTitle("          Welome To Taxi App !");
 //     find driver location
        getLocation();
        populateAutoComplete();

//      start loading drivers with "finishLodingDataFlag"
        notifyToDriversList(new Utils.NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> obj) {
            }
            @Override
            public void onFailure(Exception exception) {
            }
        }, new Utils.NotifyEndLoadinData<Boolean>() {
            @Override
            public void onEnd(Boolean obj) {
                if (obj == true) { // succeed loading all data from fireBase
                    finishLodingDataFlag = true;
                }
            }
        });


//      shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("EMAIL"))
            mEmailView.setText(sharedpreferences.getString("EMAIL", ""));
        if (sharedpreferences.contains("PASSWORD"))
            mPasswordView.setText(sharedpreferences.getString("PASSWORD", ""));


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //click on login icon
        ImageButton mEmailSignInButton2 = (ImageButton) findViewById(R.id.email_sign_in_button2);
        mEmailSignInButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });


    }

    //end on create


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     *
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;

            // Check for a valid email address.
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);

            ////aciveate UserLoginTask AsyncTask

            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


                         /////Async Task - UserLoginTask/////
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private static final String PREFS_NAME = "driverSharedPreferences";
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.

            try {       // finishLodingDataFlag-  is a  variable that update from interface "notifyEndLoadinData"
                        // when the listener finish loading to driverlist all the data from Firebase
                while (finishLodingDataFlag == false)
                    Thread.sleep(1000);
                for (Driver item : BackendFactory.getDB().getDriverList()) {
                    if (item.geteMail().equals(mEmail))
                        if (item.getePassword().equals(mPassword)) {
                            currentDriver=item;
                            return true;

                        }
                    //System.out.println(item);
                }
               // Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }


//            TODO: register the new account here.    //
            Driver driver = new Driver();
            Locationf l = new Locationf(locationA);//= new Location(from);
            driver.setStartPoint(l);
            driver.seteMail(mEmail);
            driver.setePassword(mPassword);
            driver.setId(Long.parseLong(mPassword));


            BackendFactory.getDB().addDriver(driver, new Utils.Action<Long>() {
                @Override
                public void onSuccess(Long obj) {
                    Toast.makeText(getBaseContext(), "Upload successful id: " + obj, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });

//           SharedPreferences save
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("EMAIL", mEmail);
            editor.putString("PASSWORD", mPassword);
            editor.commit();

            //  if comes here  means -> not find driver om list and add new one
            return false;
//          return true;


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            final String TAG = "myLogin";

            //  if comes here  means -> find driver on driver list
            if (success) {
                Log.d(TAG, " enter to driver details");
                Toast.makeText(getBaseContext(), "enter  to driver area", Toast.LENGTH_LONG).show();

                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                Intent intent = new Intent(LoginActivity.this, ActivityMainNevigationDrawer.class);
                intent.putExtra("firebaseDriverKey",currentDriver.getPushFireBaseKey());
                intent.putExtra("firebaseDriverId",currentDriver.getId());
                startActivity(intent);

                // finish();
            }
            //  if comes here  means -> not find driver om list and add new one
            else
                {

                Toast.makeText(getBaseContext(), "new driver added ", Toast.LENGTH_LONG).show();
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


// get location for driver
    public void getLocation() {
        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        } else {

            // getting GPS status
            Boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            Boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // getting network status
            Boolean isPassivenabled = locationManager
                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 8000, 0, locationListener);

                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {

                        //latitude = location.getLatitude();
                        //longitude = location.getLongitude();
                    }
                }
            } else if (isNetworkEnabled) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {

                        //latitude = location.getLatitude();
                        //longitude = location.getLongitude();
                    }
                }
                return;
            } else if (isPassivenabled) {

                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);

                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    if (location != null) {
                        //latitude = location.getLatitude();
                        //longitude = location.getLongitude();
                    }
                }
                return;
            } else {
                Toast.makeText(this, "cant access location PROVIDER", Toast.LENGTH_SHORT).show();
            }

            // Android version is lesser than 6.0 or the permission is already granted.
            //stopUpdateButton.setEnabled(true);
            //getLocationButton.setEnabled(false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//      stop  listener listen for  drivers
        stopNotifyToDriverList();

    }



}






