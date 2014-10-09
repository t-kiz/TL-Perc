package info.izumin.android.tlperc.ui;

import android.os.Bundle;

import com.amalgam.app.ProgressDialogFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.uxxu.konashi.lib.KonashiActivity;
import com.uxxu.konashi.lib.KonashiEvent;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import info.izumin.android.tlperc.R;
import info.izumin.android.tlperc.event.TLPercEvent;
import info.izumin.android.tlperc.model.BusProvider;
import info.izumin.android.tlperc.model.SoundManager;
import info.izumin.android.tlperc.model.TLPercKonashiObserver;
import info.izumin.android.tlperc.ui.helper.BluetoothHelper;

public class TLPercActivity extends KonashiActivity {
    public static final String TAG = TLPercActivity.class.getSimpleName();

    private static final String DIALOG_FRAGMENT = "dialog fragment";
    private static final String SE_CONNECTED = "Connected", SE_DISCONNECTED = "Disconnected";

    private SoundManager mSoundManager;
    private TLPercKonashiObserver mKonashiObserver;
    private ProgressDialogFragment mConnectingProcessDialog;
    private Crouton mConnectedToast, mDisconnectedToast;

    private BluetoothHelper mBluetoothHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlperc);
        initialize();
        transitionFindKonashiFragment();
    }

    @Override
    protected void onDestroy() {
        mBluetoothHelper.disconnect();
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onTLPercEventCallback(TLPercEvent event) {
        switch (event) {
            case CONNECTED:
                mConnectedToast.show();
                mSoundManager.play(SE_CONNECTED);
                transitionPercussionFragment();
                break;
            case DISCONNECTED:
                mSoundManager.play(SE_DISCONNECTED);
                mDisconnectedToast.show();
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Subscribe
    public void onKonashiCallback(KonashiEvent event) {
        Crouton.cancelAllCroutons();
        switch(event) {
            case CONNECTED:
                break;
            case READY:
                mSoundManager.play(SE_CONNECTED);
                break;
            case DISCONNECTED:
                break;
        }
    }

    private void initialize() {
        Bus bus = BusProvider.getInstance();
        bus.register(this);
        mBluetoothHelper = new BluetoothHelper(this, bus);
        mConnectingProcessDialog = ProgressDialogFragment.newInstance("接続中…", "Konashiに接続しています", true);
        mConnectedToast = Crouton.makeText(this, "接続できたでｗ", Style.INFO);
        mDisconnectedToast = Crouton.makeText(this, "接続切れたでｗ", Style.ALERT);
        mSoundManager = new SoundManager(getApplicationContext());
        mSoundManager.load(SE_CONNECTED, R.raw.connect);
        mSoundManager.load(SE_DISCONNECTED, R.raw.disconnect);
    }

    private void transitionFindKonashiFragment() {
        FindKonashiFragment f = FindKonashiFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container, f, FindKonashiFragment.TAG)
                .commit();
        f.setBluetoothHelper(mBluetoothHelper);
    }

    private void transitionPercussionFragment() {
        PercussionFragment f = PercussionFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(FindKonashiFragment.TAG)
                .replace(R.id.root_container, f, PercussionFragment.TAG)
                .commit();
        f.setBluetoothHelper(mBluetoothHelper);
    }
}
