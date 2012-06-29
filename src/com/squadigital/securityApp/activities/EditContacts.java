package com.squadigital.securityApp.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.util.SecurityAppUtil;

public class EditContacts extends ListActivity {
	
	private String userId;
	private String contactId;
	
	private String contactList[];
	private String idList[];
	
	AgencyList agencyList;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");
		
		connectWebservice();
		setContentView(R.layout.editcontacts);
		ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
		
	}
	
	@Override
	public void setListAdapter(ListAdapter adapter) {
		super.setListAdapter(adapter);
	}
	
	 protected ListAdapter createAdapter()
	    {
	 
	    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getcontactList());
	 
	    	return adapter;
	    }
	 
	 public String[] getcontactList(){
		 String[] myArray = {"No contacts"};
		 for(int i = 0; i < contactList.length; i++){
			 if(!contactList[i].equalsIgnoreCase("null")) {    //if user deletes all contacts array is null.
				 myArray = contactList;
			 }
		 }
		 return myArray;
	 }

	
	private void connectWebservice(){
		SecurityAppUtil.fetchXml(Global.url + "clients/listClientContact/"+userId);
		agencyList = MyXMLHandler.agencyList;
		contactList = new String[agencyList.getName().size()];
		contactList = agencyList.getName().toArray(contactList);
		idList = new String[agencyList.getId().size()];
		idList = agencyList.getId().toArray(idList);
	}
	
	 @Override
	 protected void onListItemClick(ListView l, View v, int position, long id) {
	  super.onListItemClick(l, v, position, id);
	  CharSequence itemSelected = (CharSequence)getListView().getItemAtPosition(position);
	  for (int i = 0; i < contactList.length; i ++){
		  if(itemSelected.equals(contactList[i])) { 
			  contactId = idList[i];
			  
			  Bundle bundle = new Bundle();
			  bundle.putString("contactid", contactId);
			  startActivity(new Intent(getApplicationContext(), UpdateContacts.class).putExtras(bundle));
		  } 
	  }
	  
	 }
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_complete, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.home:
				startActivity(new Intent(getApplicationContext(), Home.class));
				break;
			case R.id.account:
				break;
			case R.id.profile:
				startActivity(new Intent(getApplicationContext(), ManageProfile.class));
				break;
			case R.id.help:
				startActivity(new Intent(getApplicationContext(), Help.class));
				break;
			case R.id.logout:
				break;
			}
			return true; 
		}

}
