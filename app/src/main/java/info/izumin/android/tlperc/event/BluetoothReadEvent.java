package info.izumin.android.tlperc.event;

import java.util.EventObject;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothReadEvent extends EventObject {
    public static final String TAG = BluetoothReadEvent.class.getSimpleName();

    private CharSequence mData;

    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public BluetoothReadEvent(Object source, CharSequence data) {
        super(source);
        mData = data;
    }

    public CharSequence getData() {
        return mData;
    }
}
