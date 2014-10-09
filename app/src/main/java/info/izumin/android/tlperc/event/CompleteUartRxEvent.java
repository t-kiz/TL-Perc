package info.izumin.android.tlperc.event;

import java.util.EventObject;

/**
 * Created by izumin on 2014/10/09.
 */
public class CompleteUartRxEvent extends EventObject {
    public static final String TAG = CompleteUartRxEvent.class.getSimpleName();

    private byte mData;

    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public CompleteUartRxEvent(Object source, byte data) {
        super(source);
        mData = data;
    }

    public byte getData() {
        return mData;
    }
}
