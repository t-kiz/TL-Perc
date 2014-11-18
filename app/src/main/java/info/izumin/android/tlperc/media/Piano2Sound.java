package info.izumin.android.tlperc.media;

import info.izumin.android.tlperc.R;

/**
 * Created by izumin on 2014/10/09.
 */
public enum Piano2Sound implements Sound {

    G4(R.raw.piano2_0),
    A4(R.raw.piano2_1),
    B4(R.raw.piano2_2),
    C5(R.raw.piano2_3),
    D5(R.raw.piano2_4),
    E5(R.raw.piano2_5),
    F5(R.raw.piano2_6),
    G5(R.raw.piano2_7);

    private final int mRawId;

    Piano2Sound(int rawId) {
        mRawId = rawId;
    }

    @Override
    public int getRawId() {
        return mRawId;
    }
}
