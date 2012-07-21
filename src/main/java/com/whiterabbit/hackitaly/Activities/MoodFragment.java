package com.whiterabbit.hackitaly.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.whiterabbit.hackitaly.R;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class MoodFragment extends SherlockFragment implements SeekBar.OnSeekBarChangeListener {
    int mNum;
    SeekBar mSeekbar;
    TextView mNowPlaying;
    int mInitialValue;
    int mFinalValue;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static MoodFragment newInstance(int num) {
        MoodFragment f = new MoodFragment();

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
        View v = inflater.inflate(R.layout.inmood_view, container, false);

        mSeekbar = (SeekBar) v.findViewById(R.id.mood_seekbar);
        mNowPlaying = (TextView) v.findViewById(R.id.mood_now_playing);

        mSeekbar.setMax(5);
        mSeekbar.setOnSeekBarChangeListener(this);
        mSeekbar.setProgress(1);

        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ((InVenueActivity)getActivity()).setMood(seekBar.getProgress());
    }


    public void changeCurrentSong(String currentSong){
        mNowPlaying.setText(currentSong);
    }
}