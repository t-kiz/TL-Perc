package info.izumin.android.tlperc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiActivity;
import com.uxxu.konashi.lib.KonashiManager;

import butterknife.ButterKnife;
import info.izumin.android.tlperc.R;

/**
 * Created by izumin on 2014/10/08.
 */
public class PercussionFragment extends Fragment {
    public static final String TAG = PercussionFragment.class.getSimpleName();

    private KonashiActivity mActivity;
    private KonashiManager mKonashiManager;

    public static PercussionFragment newInstance() {
        PercussionFragment f = new PercussionFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percussion, container, false);
        ButterKnife.inject(this, view);
        mActivity = (KonashiActivity) getActivity();
        mKonashiManager = mActivity.getKonashiManager();
        mKonashiManager.pinMode(Konashi.S1, Konashi.INPUT);
        mKonashiManager.pinMode(Konashi.PIO0, Konashi.INPUT);
        mKonashiManager.pinMode(Konashi.PIO1, Konashi.INPUT);
        mKonashiManager.pinMode(Konashi.PIO2, Konashi.INPUT);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (mKonashiManager.isConnected()) mKonashiManager.disconnect();
        super.onDestroyView();
    }
}
