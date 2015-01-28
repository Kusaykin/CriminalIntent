package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks{
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

	public void onCrimeSelected(Crime crime) {
		if (findViewById(R.id.detailFragmentContainer) == null) {
			// Запуск экземпляра CrimePagerActivity
			Intent i = new Intent(this, CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(i);
		} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail = CrimeFragment.newInstance(crime.getId());
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}
			ft.add(R.id.detailFragmentContainer, newDetail);
			ft.commit();
		}
	}

	public void onCrimeUpdated(Crime crime) {
		FragmentManager fm = getSupportFragmentManager();
		CrimeListFragment listFragment = (CrimeListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}
}
