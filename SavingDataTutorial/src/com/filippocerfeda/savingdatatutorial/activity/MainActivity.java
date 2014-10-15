package com.filippocerfeda.savingdatatutorial.activity;

import com.filippocerfeda.savingdatatutorial.R;
import com.filippocerfeda.savingdatatutorial.R.id;
import com.filippocerfeda.savingdatatutorial.R.layout;
import com.filippocerfeda.savingdatatutorial.slidingtabs.SlidingTabsBasicFragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends FragmentActivity {

	private static final String DATABASE_NAME = "fc_test_database";
    public static final String TAG = "MainActivity";
    public SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }

}
