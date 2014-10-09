package info.izumin.android.tlperc.loader.task;

import android.bluetooth.BluetoothSocket;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothConnectTask extends AsyncTaskLoader<Void> {
    public static final String TAG = BluetoothConnectTask.class.getSimpleName();

    private BluetoothSocket mSocket;

    public BluetoothConnectTask(Context context, BluetoothSocket socket) {
        super(context);
        mSocket = socket;
    }

    @Override
    public Void loadInBackground() {
        try {
            mSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(Void data) {
        if (isStarted()) super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
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
