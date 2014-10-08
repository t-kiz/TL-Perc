package info.izumin.android.tlperc.ui.helper;

import dagger.Module;
import info.izumin.android.tlperc.ui.TLPercActivity;

/**
 * Created by izumin on 6/14/14.
 */
@Module(
        injects = {
                TLPercActivity.class
        },
        library = true,
        complete = false
)
public class TLPercActivityModule {
    public static final String TAG = TLPercActivityModule.class.getSimpleName();

    public TLPercActivityModule() {
    }
}
