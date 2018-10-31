package kr.ac.ajou.heidi.criminalintentj.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CrimeListActivity", "onCreate");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("CrimeListActivity", "onResume");
    }
}
