package com.supermicro.replydog.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.supermicro.replydog.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DogViewerFragment extends Fragment {
	private Context mContext;
	private final String DOG_PREFERENCE_NAME = "dog_reply";
	private TextView mTxtShow;
	private AutoCompleteTextView mEditTxtSend;
	
	public DogViewerFragment() {
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (mContext == null)
			mContext = activity;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		Button btnAdd = (Button)rootView.findViewById(R.id.btnAdd);
		final EditText editTxtAdd = (EditText)rootView.findViewById(R.id.editTxtAdd);
		Button btnDel = (Button)rootView.findViewById(R.id.btnDel);
		final EditText editTxtDel = (EditText)rootView.findViewById(R.id.editTxtDel);
		Button BtnSend = (Button)rootView.findViewById(R.id.BtnSend);
		mEditTxtSend = (AutoCompleteTextView)rootView.findViewById(R.id.editTxtSend);
		mTxtShow = (TextView)rootView.findViewById(R.id.txtShow);
		
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editTxtAdd.getText() != null)
					commitDogData(editTxtAdd.getText().toString());
			}});
		
		btnDel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editTxtDel.getText() != null)
					removeDogData(editTxtDel.getText().toString());
			}});
		
		BtnSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mEditTxtSend.getText() != null)
					showDogData(mEditTxtSend.getText().toString());
			}});
		updateAutoCompleteText();
		
		return rootView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);

	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {

		    case R.id.clear_brain:
				clearDogMemory();
				break;
		    case R.id.show_memory:
		    	showDogMemory();
		    	break;
	        default:
	    		return super.onOptionsItemSelected(item);
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onStop() {
		super.onStop();
	
	}
	
	@Override
	public void onDestroyView()	{
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy()	{
		super.onDestroy();
	}
	
	private String[] getDogMemoryKey() {
		SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		Map<String,?> allMemory = dogPreferences.getAll();		
		Set<String> memoryKeySet = null;
		
		if (allMemory != null && allMemory.size() > 0) {			
			memoryKeySet = allMemory.keySet();
			String[] memoryKey = memoryKeySet.toArray(new String[memoryKeySet.size()]);			
			return memoryKey;
		}
		else
			return null;
	}
	
	private void updateAutoCompleteText() {		
		String[] autoString = getDogMemoryKey();
		
		if (autoString != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, autoString);
			mEditTxtSend.setAdapter(adapter);
		}
	}
	
	private void commitDogData(String input) {
		String [] dogData = input.split(";");
		
		if (dogData != null && dogData.length == 2) {
			SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = dogPreferences.edit();
			editor.putString(dogData[0].toLowerCase(), dogData[1]);
			
			if (editor.commit()) {
				Toast.makeText(mContext, "I get it", Toast.LENGTH_SHORT).show();
				updateAutoCompleteText();
			}
			else {
				Toast.makeText(mContext, "Please try again", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void removeDogData(String input) {		
		SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = dogPreferences.edit();		
		editor.remove(input.toLowerCase());
		
		if (editor.commit()) {
			Toast.makeText(mContext, "I forget it", Toast.LENGTH_SHORT).show();
			updateAutoCompleteText();
		}
		else {
			Toast.makeText(mContext, "Please try again", Toast.LENGTH_SHORT).show();
		}		
	}
	
	private void showDogData(String input) {
		SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		mTxtShow.setText(dogPreferences.getString(input.toLowerCase(), ""));
		
	}
	
	
	private void clearDogMemory() {
		SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = dogPreferences.edit();
		
		if (editor.clear().commit()) {
			Toast.makeText(mContext, "I forget all", Toast.LENGTH_SHORT).show();
			updateAutoCompleteText();
		}
		else {
			Toast.makeText(mContext, "Please try again", Toast.LENGTH_SHORT).show();
		}	
	}
	
	private void showDogMemory() {
		SharedPreferences dogPreferences = mContext.getSharedPreferences(DOG_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		Map<String,?> allMemory = dogPreferences.getAll();		
		Set<String> memoryKeySet = null;
		
		if (allMemory != null && allMemory.size() > 0) {			
			memoryKeySet = allMemory.keySet();
			String[] memoryKey = memoryKeySet.toArray(new String[memoryKeySet.size()]);
			AlertDialog.Builder memoryDialog = new AlertDialog.Builder(mContext);
			List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
		    
			for (int i = 0; i < memoryKeySet.size(); i++) {		    	
		        Map<String, Object> item = new HashMap<String, Object>();
		        item.put("key", memoryKey[i]);
		        item.put("text", dogPreferences.getString(memoryKey[i], ""));
		        items.add(item);
		    }
			memoryDialog.setTitle("Dog Memory").setAdapter(
					new SimpleAdapter(mContext, 
							items, 
							android.R.layout.simple_list_item_2, 
							new String[]{"key", "text"}, 
							new int[] {android.R.id.text1, android.R.id.text2}),
							null).show();
		}
		else {
			Toast.makeText(mContext, "Empty memory", Toast.LENGTH_SHORT).show();
		}
			
	}

}
