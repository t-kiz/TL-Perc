package info.izumin.android.tlperc.media;

import info.izumin.android.tlperc.R;

/**
 * Created by izumin on 2014/10/09.
 */
public enum PianoSound implements Sound {

    C(R.raw.piano0),
    D(R.raw.piano1),
    E(R.raw.piano2),
    F(R.raw.piano3),
    G(R.raw.piano4),
    A(R.raw.piano5),
    H(R.raw.piano6),
    HIGH_C(R.raw.piano7);

    private final int mRawId;

    PianoSound(int rawId) {
        mRawId = rawId;
    }

    @Override
    public int getRawId() {
        return mRawId;
    }
}
