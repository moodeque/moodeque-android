package com.whiterabbit.hackitaly.Activities;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 *//*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.Utils.PreferencesStore;
import com.whiterabbit.hackitaly.serverinteraction.CheckinCommand;
import com.whiterabbit.hackitaly.serverinteraction.GetPlaylistCommand;
import com.whiterabbit.hackitaly.serverinteraction.SetMoodCommand;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;

import java.util.ArrayList;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI
 * that switches between tabs and also allows the user to perform horizontal
 * flicks to move between the tabs.
 */
public class InVenueActivity extends SherlockFragmentActivity implements ServerInteractionResponseInterface {
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;

    public final String SEND_MOOD = "SendMood";
    public final String CHECKIN = "Checkin";
    public final String GET_PLAYLIST = "History";
    public static final String VENUE = "Venue";


    MoodFragment mMoodFragment;
    PlaylistFragment mPlaylistFragmetn;

    private String mVenueName;
    private int mVenueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        mVenueId = getIntent().getIntExtra(VENUE, -1);
        setSupportProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.in_venue_activity_pager);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("mood").setIndicator(getString(R.string.invenue_mood)),
                MoodFragment.class, null);

        mTabsAdapter.addTab(mTabHost.newTabSpec("playlist").setIndicator(getString(R.string.invenue_playlist)),
                PlaylistFragment.class, null);


        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Refresh")
                .setIcon(R.drawable.ic_refresh_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        requestPlaylist();
        return true;
    }


    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    public void setMood(int mood){
        if(!ServerInteractionHelper.getInstance().isRequestAlreadyPending(SEND_MOOD)){
                SetMoodCommand c = new SetMoodCommand(PreferencesStore.getUsername(this), mood);
            try {
                ServerInteractionHelper.getInstance().sendCommand(this, c, SEND_MOOD);
                getMoodFragment().updatingMood();

            } catch (SendingCommandException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }


    }

    @Override
    public void onResume(){
        super.onResume();
        ServerInteractionHelper.getInstance().registerEventListener(this, this);
        checkin();
        requestPlaylist();
    }



    private void requestPlaylist(){
        if(!ServerInteractionHelper.getInstance().isRequestAlreadyPending(GET_PLAYLIST)){
            sendGetPlayList();
            setSupportProgressBarIndeterminateVisibility(true);
        }else{
            setSupportProgressBarIndeterminateVisibility(true);
        }
    }

    private void sendGetPlayList(){
        GetPlaylistCommand c = new GetPlaylistCommand(mVenueId);
        try {
            ServerInteractionHelper.getInstance().sendCommand(this, c, GET_PLAYLIST);
            setSupportProgressBarIndeterminateVisibility(true);
        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

        ServerInteractionHelper.getInstance().unregisterEventListener(this, this);
    }


    @Override
    public void onServerResult(String result, String requestId) {
        if(requestId.equals(GET_PLAYLIST)){
            getMoodFragment().changeCurrentSong(PreferencesStore.getCurrentlyPlaying(this));
            getPlaylistFragment().updatePlaylist();
            setSupportProgressBarIndeterminateVisibility(false);
        }

        if(requestId.equals(SEND_MOOD)){
            getMoodFragment().unsetUpdatingMood();
        }
    }

    @Override
    public void onServerError(String result, String requestId) {

        if(requestId.equals(SEND_MOOD)){
            getMoodFragment().unsetUpdatingMood();
        }else if (requestId.equals(GET_PLAYLIST)){
            setSupportProgressBarIndeterminateVisibility(false);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }

    }




    public void setMoodFragment(MoodFragment m){
        mMoodFragment = m;
    }

    public void setPlaylistFragment(PlaylistFragment m){
        mPlaylistFragmetn = m;
    }


    MoodFragment getMoodFragment(){
        return mMoodFragment;



    }

    PlaylistFragment getPlaylistFragment(){
        return mPlaylistFragmetn;
    }


    private void checkin(){
        CheckinCommand c = new CheckinCommand(PreferencesStore.getUsername(this), mVenueId);
        try {
            ServerInteractionHelper.getInstance().sendCommand(this, c, CHECKIN);

        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void checkout(){
        CheckinCommand c = new CheckinCommand(PreferencesStore.getUsername(this), mVenueId);
        try {
            ServerInteractionHelper.getInstance().sendCommand(this, c, CHECKIN);

        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onBackPressed(){
        checkout();
        super.onBackPressed();
    }

}

