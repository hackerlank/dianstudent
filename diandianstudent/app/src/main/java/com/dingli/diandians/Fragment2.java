package com.dingli.diandians;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Fragment2 extends Fragment {

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		   View viewtwo=inflater.inflate(
				   R.layout.activity_guid_item_2,
				   container,
				   false);
		return viewtwo;
	}

}
