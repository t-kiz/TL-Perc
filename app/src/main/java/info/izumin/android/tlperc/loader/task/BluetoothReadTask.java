package info.izumin.android.tlperc.loader.task;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothReadTask extends AsyncTaskLoader<CharSequence> {
    public static final String TAG = BluetoothReadTask.class.getSimpleName();

    private BufferedReader mReader;
    private CharSequence mCachedData;

    public BluetoothReadTask(Context context, BufferedReader reader) {
        super(context);
        mReader = reader;
    }

    @Override
    public CharSequence loadInBackground() {
        try {
            return mReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "00";
        }
    }

    @Override
    public void deliverResult(CharSequence data) {
        if (isReset()) {
            if (mCachedData != null) mCachedData = null;
            return;
        }
        mCachedData = data;
        if (isStarted()) super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mCachedData != null) {
            deliverResult(mCachedData);
            return;
        }
        if (takeContentChanged() || mCachedData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        super.onReset();
    }
}
