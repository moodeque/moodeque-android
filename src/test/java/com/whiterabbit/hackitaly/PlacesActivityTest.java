package com.whiterabbit.hackitaly;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.whiterabbit.hackitaly.Activities.PlacesActivity;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.shadows.ShadowListView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class PlacesActivityTest {
	
	PlacesActivity activity;
	
    ShadowListView s;



	 @Before
	 public void setUp() throws Exception {
         activity = new PlacesActivity();


         ListView decks = (ListView) activity.findViewById(R.id.deck_list_list);
         s = shadowOf(decks);


	 }


	 
	 @Test
	 public void testLoad(){


        int childs = s.getCount();



	    assertEquals(childs, 3);
        View first = (View) s.getChildAt(0);

        TextView name = (TextView) first.findViewById(R.id.deck_list_deckname);
        assertEquals("Robots", name.getText());

         View second = (View) s.getChildAt(1);

         name = (TextView) second.findViewById(R.id.deck_list_deckname);
         assertEquals("Robots", name.getText());

   	}


    @Test
    public void testLaunch(){
        s.performItemClick(0);
        ShadowActivity sa = shadowOf(activity);
        Intent i = sa.getNextStartedActivity();
        ShadowIntent si = shadowOf(i);

        assertEquals(si.getComponent().getClassName(),DeckShowcaseActivity.class.getName());

    }
    



}