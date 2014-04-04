package com.example.criminalintent.fragments;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.criminalintent.Crime;
import com.example.criminalintent.CrimeLab;
import com.example.criminalintent.R;
import com.example.criminalintent.activities.CrimePagerActivity;
import com.example.criminalintent.adapters.AdapterFactory;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> crimes;
	private Boolean subtitleVisible;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.crimes_title);
		CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
		crimes = crimeLab.getCrimes();

		ArrayAdapter<Crime> arrayAdapter = AdapterFactory.getCrimeListAdapter(getActivity(), crimes);
		setListAdapter(arrayAdapter);

		setRetainInstance(true);
		subtitleVisible = false;
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (subtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.show_subtitle);
			}
		}

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		@SuppressWarnings("unchecked")
		ArrayAdapter<Crime> arrayAdapter = (ArrayAdapter<Crime>) getListAdapter();
		arrayAdapter.notifyDataSetChanged();
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean optionsItemsSelected;

		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.getInstance(getActivity()).addCrime(crime);
			Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivityForResult(intent, 0);
			optionsItemsSelected = true;
			break;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.show_subtitle);
				subtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				subtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}
			optionsItemsSelected = true;
		default:
			optionsItemsSelected = super.onOptionsItemSelected(item);
			break;
		}

		return optionsItemsSelected;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		@SuppressWarnings("unchecked")
		Crime crime = ((ArrayAdapter<Crime>) getListAdapter()).getItem(position);
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
		startActivity(intent);
	}
}
