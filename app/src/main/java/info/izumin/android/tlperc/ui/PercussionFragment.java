package info.izumin.android.tlperc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amalgam.view.DisplayUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
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

    private static final String SE_REPLACE = "SE replace";

    private BluetoothHelper mBluetoothHelper;

    private SoundManager mCurrentSoundManager, mButtonSoundManager;
    private SoundManager mDrumsSoundManager, mPercSoundManager, mPianoSoundManager;

    @Optional @InjectView(R.id.btn_drums) ImageView mBtnDrums;
    @Optional @InjectView(R.id.btn_perc) ImageView mBtnPerc;
    @Optional @InjectView(R.id.btn_piano) ImageView mBtnPiano;

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

    @Optional
    @OnClick({R.id.btn_drums, R.id.btn_perc, R.id.btn_piano})
    public void onClickChangeInstrumentsButton(View v) {
        mBtnDrums.setImageResource(R.drawable.ic_drums_off);
        mBtnPerc.setImageResource(R.drawable.ic_perc_off);
        mBtnPiano.setImageResource(R.drawable.ic_piano_off);
        switch (v.getId()) {
            case R.id.btn_drums:
                mCurrentSoundManager = mDrumsSoundManager;
                mBtnDrums.setImageResource(R.drawable.ic_drums_on);
                break;
            case R.id.btn_perc:
                mCurrentSoundManager = mPercSoundManager;
                mBtnPerc.setImageResource(R.drawable.ic_perc_on);
                break;
            case R.id.btn_piano:
                mCurrentSoundManager = mPianoSoundManager;
                mBtnPiano.setImageResource(R.drawable.ic_piano_on);
                break;
        }
        mButtonSoundManager.play(SE_REPLACE);
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

        mButtonSoundManager = new SoundManager(getActivity().getApplicationContext());
        mButtonSoundManager.load(SE_REPLACE, R.raw.replace);
        mBtnDrums.setImageResource(R.drawable.ic_drums_on);
        int height = - DisplayUtils.getDisplayHeight(getActivity()) / 8;
        mBtnDrums.setTranslationY(height);
        mBtnPerc.setTranslationY(height);
        mBtnPiano.setTranslationY(height);
    }

    // TODO: bad practice
    public void setBluetoothHelper(BluetoothHelper helper) {
        mBluetoothHelper = helper;
    }
}
