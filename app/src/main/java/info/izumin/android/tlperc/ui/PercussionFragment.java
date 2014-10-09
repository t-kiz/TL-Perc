package info.izumin.android.tlperc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import info.izumin.android.tlperc.R;
import info.izumin.android.tlperc.event.BluetoothReadEvent;
import info.izumin.android.tlperc.media.DrumsSound;
import info.izumin.android.tlperc.media.PercSound;
import info.izumin.android.tlperc.media.PianoSound;
import info.izumin.android.tlperc.model.BusProvider;
import info.izumin.android.tlperc.model.SoundManager;
import info.izumin.android.tlperc.ui.helper.BluetoothHelper;

/**
 * Created by izumin on 2014/10/08.
 */
public class PercussionFragment extends Fragment {
    public static final String TAG = PercussionFragment.class.getSimpleName();

    private BluetoothHelper mBluetoothHelper;

    private SoundManager mCurrentSoundManager;
    private SoundManager mDrumsSoundManager;
    private SoundManager mPercSoundManager;
    private SoundManager mPianoSoundManager;

    private CharSequence mPrevData = "00";

    public static PercussionFragment newInstance() {
        PercussionFragment f = new PercussionFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percussion, container, false);
        ButterKnife.inject(this, view);
        initialize();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBluetoothHelper.asyncRead();
    }

    @Override
    public void onDestroyView() {
        mBluetoothHelper.disconnect();
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onBluetoothReadEvent(BluetoothReadEvent event) {
        if (!event.getData().equals(mPrevData)) {
            mPrevData = event.getData();
            String str = "";
            int value = Integer.parseInt(event.getData().toString(), 16);
            List<String> keys = new ArrayList<String>();
            for (int i = 0; value != 0 && i < 8; i++) {
                if ((value & (int) Math.pow(2, i)) > 0) {
                    keys.add(mCurrentSoundManager.getLoadedSoundKeys().get(i));
                    str += mCurrentSoundManager.getLoadedSoundKeys().get(i) + " ";
                }
            }
            Log.d(TAG, str);
            for (String key : keys) mCurrentSoundManager.play(key);
        }
        mBluetoothHelper.asyncRead();
    }

    private void initialize() {
        BusProvider.getInstance().register(this);
        mDrumsSoundManager = new SoundManager(getActivity().getApplicationContext());
        for (DrumsSound sound : DrumsSound.values()) {
            mDrumsSoundManager.load(sound.name(), sound.getRawId());
        }
        mPercSoundManager = new SoundManager(getActivity().getApplicationContext());
        for (PercSound sound : PercSound.values()) {
            mPercSoundManager.load(sound.name(), sound.getRawId());
        }
        mPianoSoundManager = new SoundManager(getActivity().getApplicationContext());
        for (PianoSound sound : PianoSound.values()) {
            mPianoSoundManager.load(sound.name(), sound.getRawId());
        }
        mCurrentSoundManager = mDrumsSoundManager;
    }

    // TODO: bad practice
    public void setBluetoothHelper(BluetoothHelper helper) {
        mBluetoothHelper = helper;
    }
}
