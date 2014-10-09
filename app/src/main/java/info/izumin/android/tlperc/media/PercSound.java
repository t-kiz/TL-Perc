package info.izumin.android.tlperc.media;

import info.izumin.android.tlperc.R;

/**
 * Created by izumin on 2014/10/09.
 */
public enum PercSound implements Sound {

    TAMBOURINE(R.raw.perc0),
    VIBRASLAP(R.raw.perc1),
    CHIME(R.raw.perc2),
    BELL(R.raw.perc3),
    COWBELL(R.raw.perc4),
    MARACAS(R.raw.perc5),
    SMALL_HAND_DRUM(R.raw.perc6),
    TOM(R.raw.perc7);

    private final int mRawId;

    PercSound(int rawId) {
        mRawId = rawId;
    }

    @Override
    public int getRawId() {
        return mRawId;
    }
}
