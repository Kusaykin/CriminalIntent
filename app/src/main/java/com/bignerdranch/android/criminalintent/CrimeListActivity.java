package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity {
	private static final String TAG = "Crime";
	@Override
	protected Fragment createFragment() {
		Log.d(TAG, "Call CrimeListActivity-createFragment");
		return new CrimeListFragment();
	}
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}
}
