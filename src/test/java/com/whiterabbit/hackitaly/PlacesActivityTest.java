package com.whiterabbit.hackitaly;

import android.content.Context;
import android.widget.ListView;
import com.whiterabbit.hackitaly.Activities.PlacesActivity;
import com.whiterabbit.hackitaly.Storage.DbHelper;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.commands.ServerCommand;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowListView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class PlacesActivityTest {
	
	PlacesActivity activity;
	
    ShadowListView s;
    ServerInteractionHelper mockServer;
    DbHelper dbHelper;



	 @Before
	 public void setUp() throws Exception {
         mockServer =  mock(ServerInteractionHelper.class);
         activity = new PlacesActivity(){
            @Override
             public ServerInteractionHelper getInteractionHelper(){
                return mockServer;
            }};

         dbHelper = new DbHelper(activity);


         activity.onCreate(null);

         ListView places = (ListView) activity.findViewById(R.id.places_list);
         s = shadowOf(places);


	 }



    private void fillDb(){
        dbHelper.open();
        dbHelper.removeAllPlace();
        dbHelper.addPlace(Long.valueOf(23), "Fava", "Rava", Long.valueOf(0), Long.valueOf(0));
        dbHelper.addPlace(Long.valueOf(24), "Fava1", "Rava1", Long.valueOf(0), Long.valueOf(0));
        dbHelper.addPlace(Long.valueOf(25), "Fava2", "Rava2", Long.valueOf(0), Long.valueOf(0));
        dbHelper.close();
    }

	 @Test
	 public void testLoad(){
         when(mockServer.isRequestAlreadyPending(anyString())).thenReturn(false);


         activity.onResume();

         try {
             verify(mockServer).sendCommand(any(Context.class), any(ServerCommand.class), eq(PlacesActivity.GET_VENUES));
         } catch (SendingCommandException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }

         fillDb();

         activity.onServerResult(null, PlacesActivity.GET_VENUES);

         int childs = s.getCount();
         assertEquals(childs, 3);


         /*int childs = s.getCount();



         assertEquals(childs, 3);
         View first = (View) s.getChildAt(0);


         View second = (View) s.getChildAt(1);

         assertEquals("Robots", name.getText());
         */
   	}



    @Test
    public void testVenueParse(){
        String fava = "\"{\"venues\": [{\"latitude\": null, \"description\": \"Description\", \"id\": 0, \"longitude\": null, \"name\": \"prova1\"}, {\"latitude\": null, \"description\": \"Description\", \"id\": 1, \"longitude\": null, \"name\": \"prova2\"}]}";



    }

    



}