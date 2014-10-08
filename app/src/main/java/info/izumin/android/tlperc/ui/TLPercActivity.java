package info.izumin.android.tlperc.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.uxxu.konashi.lib.KonashiActivity;
import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.KonashiEvent;

import info.izumin.android.tlperc.R;
import info.izumin.android.tlperc.model.BusProvider;
import info.izumin.android.tlperc.model.TLPercKonashiObserver;

public class TLPercActivity extends KonashiActivity {
    public static final String TAG = TLPercActivity.class.getSimpleName();

    private TLPercKonashiObserver mKonashiObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlperc);
        transitionFindKonashiFragment();
        Bus bus = BusProvider.getInstance();
        mKonashiObserver = new TLPercKonashiObserver(this, bus);
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        if (getKonashiManager().isConnected()) getKonashiManager().disconnect();
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onKonashiCallback(KonashiEvent event) {
        Log.d(TAG, event.name());
        switch(event) {
            case READY:
                transitionPercussionFragment();
                break;
            case DISCONNECTED:
                Toast.makeText(this, "接続切れたでｗ", Toast.LENGTH_LONG).show();
                transitionFindKonashiFragment();
                break;
        }
    }

    @Subscribe
    public void onKonashiError(KonashiErrorReason errorReason) {
        Log.d(TAG, errorReason.name());
    }

    private void transitionFindKonashiFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container, FindKonashiFragment.newInstance())
                .commit();
    }

    private void transitionPercussionFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container, PercussionFragment.newInstance())
                .commit();
    }
}
