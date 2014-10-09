package info.izumin.android.tlperc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import info.izumin.android.tlperc.R;
import info.izumin.android.tlperc.ui.helper.BluetoothHelper;

/**
 * Created by izumin on 2014/10/08.
 */
public class FindKonashiFragment extends Fragment {
    public static final String TAG = FindKonashiFragment.class.getSimpleName();

    private BluetoothHelper mBluetoothHelper;

    public static FindKonashiFragment newInstance() {
        FindKonashiFragment f = new FindKonashiFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_konashi, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.find_konashi_view)
    public void onClickFindKonashiView() {
        mBluetoothHelper.connect();
    }

    // TODO: bad practice
    public void setBluetoothHelper(BluetoothHelper helper) {
        mBluetoothHelper = helper;
    }
}
