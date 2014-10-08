package info.izumin.android.tlperc.event;

import java.util.EventObject;

/**
 * Created by izumin on 3/30/14.
 */
public class UpdatePioInputEvent extends EventObject {
    private static final String TAG = UpdatePioInputEvent.class.getSimpleName();
    private final UpdatePioInputEvent self = this;

    private byte mValue;

    public UpdatePioInputEvent(Object source, byte value) {
        super(source);
        mValue = value;
    }

    public byte getValue() { return mValue; }
}
