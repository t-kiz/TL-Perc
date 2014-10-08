package info.izumin.android.tlperc.model;

import android.app.Activity;

import com.squareup.otto.Bus;
import com.uxxu.konashi.lib.KonashiActivity;
import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.KonashiEvent;
import com.uxxu.konashi.lib.KonashiObserver;

import info.izumin.android.tlperc.event.UpdatePioInputEvent;

/**
 * Created by izumin on 3/30/14.
 */
public class TLPercKonashiObserver extends KonashiObserver {
    private static final String TAG = TLPercKonashiObserver.class.getSimpleName();
    private final TLPercKonashiObserver self = this;

    private Bus mBus;

    /**
     * コンストラクタ
     * @param activity Activity
     */
    public TLPercKonashiObserver(Activity activity, Bus bus) {
        super(activity);
        ((KonashiActivity) activity).getKonashiManager().addObserver(this);
        mBus = bus;
    }

    @Override
    public void onConnected() {
        mBus.post(KonashiEvent.CONNECTED);
    }

    @Override
    public void onDisconncted() {
        mBus.post(KonashiEvent.DISCONNECTED);
    }

    @Override
    public void onReady() {
        mBus.post(KonashiEvent.READY);
    }

    @Override
    public void onUpdatePioInput(byte value) {
        mBus.post(new UpdatePioInputEvent(this, value));
    }

    @Override
    public void onCancelSelectKonashi() {
        mBus.post(KonashiEvent.CANCEL_SELECT_KONASHI);
    }

    @Override
    public void onError(KonashiErrorReason errorReason, String message) {
        super.onError(errorReason, message);
        mBus.post(errorReason);
    }
}
