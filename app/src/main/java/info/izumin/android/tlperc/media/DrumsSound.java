package info.izumin.android.tlperc.media;

import info.izumin.android.tlperc.R;

/**
 * Created by izumin on 2014/10/09.
 */
public enum DrumsSound implements Sound {

    BASS_DRUM(R.raw.drums0),
    FLOOR_TOM(R.raw.drums1),
    LOW_TOM(R.raw.drums2),
    HIGH_TOM(R.raw.drums3),
    SNARE_DRUM(R.raw.drums4),
    CRUSH_CYMBAL(R.raw.drums5),
    RIDE_CYMBAL(R.raw.drums6),
    HI_HATS(R.raw.drums7);

    private final int mRawId;

    DrumsSound(int rawId) {
        mRawId = rawId;
    }

    @Override
    public int getRawId() {
        return mRawId;
    }
}
