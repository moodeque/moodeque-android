package com.whiterabbit.hackitaly.Activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.Storage.DbHelper;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class PlaylistFragment extends SherlockListFragment {
    class PlayListAdapter extends CursorAdapter {

        Context mContext;
        private LayoutInflater mInflater;


        public PlayListAdapter(Context context, Cursor c) {
            super(context, c, true);
            mContext = context;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        protected synchronized void onContentChanged() {
            super.onContentChanged();
        }




        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            // Reset the view (in case it was recycled) and prepare for binding

            TextView artist = (TextView) view.findViewById(R.id.playlist_artist);
            TextView songName = (TextView) view.findViewById(R.id.playlist_songname);

            String artistName = cursor.getString(DbHelper.SONG_ARTIST_COLUMN);
            String song = cursor.getString(DbHelper.SONG_NAME_COLUMN);

            final Long rowId = cursor.getLong(0);
            artist.setText(artistName);
            songName.setText(song);


        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.playlist_element, parent, false);
        }

    }


    int mNum;
    DbHelper mDbHelper;
    PlayListAdapter mAdapter;


    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static PlaylistFragment newInstance(int num) {
        PlaylistFragment f = new PlaylistFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_view, container, false);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDbHelper = new DbHelper(getActivity());
        ((InVenueActivity)getActivity()).setPlaylistFragment(this);



    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        mDbHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        mDbHelper.open();
    }

    public void updatePlaylist(){
        Cursor c  = mDbHelper.getAllSong();
        c.moveToFirst();
        mAdapter = new PlayListAdapter(getActivity(), c);
        getListView().setAdapter(mAdapter);
        mAdapter.changeCursor(c);


    }
}