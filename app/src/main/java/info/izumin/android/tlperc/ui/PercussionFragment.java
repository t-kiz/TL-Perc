package info.izumin.android.tlperc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import info.izumin.android.tlperc.R;
import info.izumin.android.tlperc.event.BluetoothReadEvent;
import info.izumin.android.tlperc.media.DrumsSound;
import info.izumin.android.tlperc.model.BusProvider;
import info.izumin.android.tlperc.model.SoundManager;
import info.izumin.android.tlperc.ui.helper.BluetoothHelper;

/**
 * Created by izumin on 2014/10/08.
 */
public class PercussionFragment extends Fragment {
    public static final String TAG = PercussionFragment.class.getSimpleName();

    private BluetoothHelper mBluetoothHelper;
    private SoundManager mDrumsSoundManager;

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
        super.onDestroyView();
    }

    @Subscribe
    public void onBluetoothReadEvent(BluetoothReadEvent event) {
        mBluetoothHelper.asyncRead();
    }

    private void initialize() {
        BusProvider.getInstance().register(this);
        mDrumsSoundManager = new SoundManager(getActivity().getApplicationContext());
        for (DrumsSound sound : DrumsSound.values()) {
            mDrumsSoundManager.load(sound.name(), sound.getRawId());
        }
    }

    // TODO: bad practice
    public void setBluetoothHelper(BluetoothHelper helper) {
        mBluetoothHelper = helper;
    }
}
