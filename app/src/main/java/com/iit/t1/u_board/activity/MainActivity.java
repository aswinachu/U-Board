package com.iit.t1.u_board.activity;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.iit.t1.u_board.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,GoogleApiClient.OnConnectionFailedListener,SwipeRefreshLayout.OnRefreshListener
{
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;
    public static boolean pref1,pref2,pref3,pref4;
    private GoogleApiClient mGoogleApiClient;
    private static String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ImageButton  imageButton;
    ImageButton imageButton1;
    private Toolbar mToolbar;
    ImageButton FAB;
    PackageInfo info;
    EditText et;
    public static String userName;
    private FragmentDrawer drawerFragment;
    SignInActivity signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        checkPref();
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
System.out.println("i am " + userName);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (hasConnection()) {
            try {
                info = getPackageManager().getPackageInfo("com.iit.t1.u_board.activity", PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md;
                    md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String something = new String(Base64.encode(md.digest(), 0));
                    //String something = new String(Base64.encodeBytes(md.digest()));
                    et.setText("" + something);
                    Log.e("hash key", something);
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("name not found", e1.toString());
            } catch (NoSuchAlgorithmException e) {
                Log.e("no such an algorithm", e.toString());
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // display the first navigation drawer view on app launch
        displayView(0);
        }
        else{
            Open_dilog();
        }
        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

   // @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ivImage.setImageBitmap(bm);
    }



    private boolean hasConnection() {

        ConnectivityManager cm = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    private void Open_dilog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("NO INTERNET");
        builder.setIcon(R.drawable.ic_alert2);
        builder.setMessage("No Internet Connection Available...Turn on Mobile data or Wifi.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //dialog.dismiss();
                finish();

            }
        });
        AlertDialog dialog_show = builder.create();
        dialog_show.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;

        String title = getString(R.string.app_name);
        switch (position) {
            case 0:

               fragment=new HomeFragment();
                title = getString(R.string.title_home);
                imageButton= (ImageButton)findViewById(R.id.fab_add);
                imageButton.setVisibility(View.VISIBLE);
                break;
            case 1:
                fragment = new CreateNoticeFragment();
                title = getString(R.string.title_new_notice);
                imageButton= (ImageButton)findViewById(R.id.fab_add);
                imageButton.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if(pref1) {
                    fragment = new SalesFragment();
                    title = getString(R.string.title_sales);
                    imageButton = (ImageButton) findViewById(R.id.fab_add);
                    imageButton.setVisibility(View.VISIBLE);
                }else
                {
                    Toast.makeText(getBaseContext(), "Sorry preferences for Sales are not set! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if(pref2) {
                    fragment = new HousingFragment();
                    title = getString(R.string.title_housing);
                    imageButton = (ImageButton) findViewById(R.id.fab_add);
                    imageButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Sorry preferences for Housing are not set! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if(pref3) {
                    fragment = new EventsFragment();
                    title = getString(R.string.title_events);
                    imageButton = (ImageButton) findViewById(R.id.fab_add);
                    imageButton.setVisibility(View.VISIBLE);
                }else
                {
                    Toast.makeText(getBaseContext(),"Sorry preferences for Events are not set! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                if(pref4) {
                    fragment = new JobsAndOpportunitiesFragment();
                    title = getString(R.string.title_jobs_and_opportunities);
                    imageButton = (ImageButton) findViewById(R.id.fab_add);
                    imageButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Sorry preferences for Jobs are not set! ", Toast.LENGTH_SHORT).show();
                }
                break;

            case 6:
                Intent prefac= new Intent(MainActivity.this,SetPreference.class);
                startActivity(prefac);
                title = getString(R.string.preferences);
                imageButton= (ImageButton)findViewById(R.id.fab_add);
                imageButton.setVisibility(View.INVISIBLE);
                checkPref();
                break;


            case 7:
                //fragment = new SignOutFragment();
                signOut();
                title = getString(R.string.title_signout);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

             public void callCreateNotice(View v)
             {
                 Fragment fragment=new CreateNoticeFragment();
                 FragmentManager fragmentManager = getSupportFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.container_body, fragment);
                 fragmentTransaction.addToBackStack(null);
                 fragmentTransaction.commit();
                 imageButton= (ImageButton)findViewById(R.id.fab_add);
                 imageButton.setVisibility(View.INVISIBLE);
                 // set the toolbar title
                 getSupportActionBar().setTitle("New Notice");
             }

             public void eventsmenu(View v)
             {
                 if(pref3)
                 {
                     Fragment fragment=new EventsFragment();
                     FragmentManager fragmentManager = getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.container_body, fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                     imageButton1= (ImageButton)findViewById(R.id.eventsButton);
                     imageButton1.setVisibility(View.INVISIBLE);
                     // set the toolbar title
                     getSupportActionBar().setTitle("Events");
                 }
                 else
                 {
                     Toast.makeText(getBaseContext(),"Sorry preferences for Events are not set! ", Toast.LENGTH_SHORT).show();
                 }

             }
             public void jobsmenu(View v)
             {
                 if(pref4) {
                     Fragment fragment = new JobsAndOpportunitiesFragment();
                     FragmentManager fragmentManager = getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.container_body, fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                     imageButton1 = (ImageButton) findViewById(R.id.jobsButton);
                     imageButton1.setVisibility(View.INVISIBLE);
                     // set the toolbar title
                     getSupportActionBar().setTitle("Jobs");
                 }else
                 {
                     Toast.makeText(getBaseContext(), "Sorry preferences for Jobs are not set! ", Toast.LENGTH_SHORT).show();
                 }
             }

             public void housemenu(View v)
             {
                 if(pref2) {
                     Fragment fragment = new HousingFragment();
                     FragmentManager fragmentManager = getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.container_body, fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                     imageButton1 = (ImageButton) findViewById(R.id.houseButton);
                     imageButton1.setVisibility(View.INVISIBLE);
                     // set the toolbar title
                     getSupportActionBar().setTitle("Housing");
                 }else
                 {
                     Toast.makeText(getBaseContext(),"Sorry preferences for Housing are not set! ", Toast.LENGTH_SHORT).show();
                 }
             }

             public void salemenu(View v) {
                 if (pref1) {
                     Fragment fragment = new SalesFragment();
                     FragmentManager fragmentManager = getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.container_body, fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                     imageButton1 = (ImageButton) findViewById(R.id.saleButton);
                     imageButton1.setVisibility(View.INVISIBLE);
                     // set the toolbar title
                     getSupportActionBar().setTitle("Sales");
                 } else {
                     Toast.makeText(getBaseContext(), "Sorry preferences for Sales are not set! ", Toast.LENGTH_SHORT).show();
                 }

             }
    public void setActionBar(String title)
    {
        imageButton.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(title);
    }

             @Override
             public void onConnectionFailed(ConnectionResult connectionResult) {

             }


             private void signOut() {
                 Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                         new ResultCallback<Status>() {
                             @Override
                             public void onResult(Status status) {
                                 // [START_EXCLUDE]
                                 Intent logout = new Intent(MainActivity.this, SignInActivity.class);
                                 MainActivity.this.startActivity(logout);
                                 // [END_EXCLUDE]
                             }
                         });
             }




    @Override
    public void onRefresh() {
        checkPref();
        imageButton = (ImageButton) findViewById(R.id.fab_add);
        imageButton.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getApplicationContext(),"Preferences Refreshed !",Toast.LENGTH_SHORT).show();
    }

    public  void checkPref(){

        SharedPreferences myPref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        pref1=myPref.getBoolean("pref_opt1", false);
        System.out.println("boolean"+pref1);
         pref2=myPref.getBoolean("pref_opt2", false);
        System.out.println("boolean" + pref2);
         pref3=myPref.getBoolean("pref_opt3", false);
        System.out.println("boolean" + pref3);
        pref4= myPref.getBoolean("pref_opt4", false);
        System.out.println("boolean" + pref4);

    }


    @Override
    public void onBackPressed() {
        System.out.println("I am Back");
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
            System.out.println("finis");
        } else {
            getFragmentManager().popBackStack();
            System.out.println("backstack");
        }
    }
}