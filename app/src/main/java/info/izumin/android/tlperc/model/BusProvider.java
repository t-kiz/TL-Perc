package info.izumin.android.tlperc.model;

import com.squareup.otto.Bus;

/**
 * Created by izumin on 1/23/14.
 */
public final class BusProvider {
    private static final String TAG = BusProvider.class.getSimpleName();
    private final BusProvider self = this;

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {

    }
}
