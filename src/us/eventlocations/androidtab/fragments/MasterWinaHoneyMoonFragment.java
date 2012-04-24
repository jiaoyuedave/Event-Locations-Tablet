package us.eventlocations.androidtab.fragments;

import android.app.Fragment;
import android.os.Bundle;

//http://eventlocations.us/admina/services/iphone.asmx
public class MasterWinaHoneyMoonFragment extends Fragment  {



    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
     
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putInt("curChoice", mCurCheckPosition);
    }



    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
       /* mCurCheckPosition = index;
        
        if (mDualPane) {
            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
            	
                details = DetailFragment.newInstance(index);
                if(accounts != null){
	               	DetailFragment.setData(accounts, index);
                }
                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
            
        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	DetailFragment.setData(accounts, index);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.SEARCH_BY_SITE_NAME);
            startActivity(intent);
        }*/
    }
    
  
	
 
    
   
    

    
}
