package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	private ArrayList<Crime> mCrimes;
	private static final String TAG = "Crime";

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			Intent i = new Intent(getActivity(), CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	@Override
	public void onResume() {
	super.onResume();
	((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d(TAG, " was clicked");
		//Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		Crime c = (Crime)(getListAdapter()).getItem(position);
		Log.d(TAG, c.getTitle() + " was clicked");
		
		// Запуск CrimePagerActivity с объектом Сrime
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Log.d(TAG, "Call CrimeListFragment-onCreate");
		
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();

		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
		
	//	ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), 
	//			android.R.layout.simple_list_item_1,mCrimes);
	//			setListAdapter(adapter);
	}
	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
			Log.d(TAG, "Call CrimeListFragment-CrimeAdapter");
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "Call CrimeListFragment-CrimeAdapter-getView");
			// Если мы не получили представление, заполняем его
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
				.inflate(R.layout.list_item_crime, null);
				}
				// Настройка представления для объекта Crime
			
				Crime c = getItem(position);
				TextView titleTextView = 
				(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
				titleTextView.setText(c.getTitle());
				TextView dateTextView = 
				(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
				dateTextView.setText(c.getDate().toString());
				CheckBox solvedCheckBox = 
				(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
				solvedCheckBox.setChecked(c.isSolved());
					
			return convertView;
		}
	
	}
	
}
		/*
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}

	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
			Log.d(TAG, "Call CrimeListFragment-CrimeAdapter");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "Call CrimeListFragment-CrimeAdapter-getView");
			// Если мы не получили представление, заполняем его
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_crime, null);
			}
			// Настройка представления для объекта Crime
			
			Crime c = getItem(position);
			
			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_titleTextView);			
			titleTextView.setText(c.getTitle());
			
			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_dateTextView);			
			dateTextView.setText(c.getDate().toString());
			
			CheckBox solvedCheckBox = (CheckBox) convertView
					.findViewById(R.id.crime_list_item_solvedCheckBox);			
			solvedCheckBox.setChecked(c.isSolved());
			
			return convertView;
		}
	}
}
*/
