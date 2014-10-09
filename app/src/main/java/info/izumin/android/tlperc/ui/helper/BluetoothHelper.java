package info.izumin.android.tlperc.ui.helper;

import android.app.Activity;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.amalgam.os.HandlerUtils;
import com.squareup.otto.Bus;

import java.io.IOException;

import info.izumin.android.tlperc.event.TLPercEvent;
import info.izumin.android.tlperc.loader.task.BluetoothConnectTask;
import info.izumin.android.tlperc.loader.task.BluetoothDisconnectTask;
import info.izumin.android.tlperc.ui.BluetoothDeviceListDialogFragment;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothHelper implements BluetoothDeviceListDialogFragment.Callback {
    public static final String TAG = BluetoothHelper.class.getSimpleName();

    private static final String DEVICE_LIST_FRAGMENT = "device list fragment";

    private Activity mActivity;
    private Bus mBus;
    private BluetoothDeviceListDialogFragment mBluetoothDeviceListDialogFragment;

    private BluetoothAdapter mAdapter;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    public BluetoothHelper(Activity activity, Bus bus) {
        mActivity = activity;
        mBus = bus;
    }

    public void connect() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothDeviceListDialogFragment = new BluetoothDeviceListDialogFragment();
        mBluetoothDeviceListDialogFragment.setCallback(this);
        mBluetoothDeviceListDialogFragment.setBluetoothDevices(mAdapter.getBondedDevices());
        mBluetoothDeviceListDialogFragment.show(mActivity.getFragmentManager(), DEVICE_LIST_FRAGMENT);
    }

    public void disconnect() {
        mActivity.getLoaderManager().initLoader(0, null, mDisconnectCallback);
    }

    /* ================================================================
     *  Callback from DeviceListDialog
     * ================================================================ */

    @Override
    public void onDeviceDecided(BluetoothDevice device) throws IOException {
        Log.d(TAG, device.getName());
        mDevice = device;
        mSocket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
        mActivity.getLoaderManager().initLoader(0, null, mConnectCallback);
    }

    /* ================================================================
     *  LoaderCallbacks
     * ================================================================ */

    private final LoaderManager.LoaderCallbacks<Void> mConnectCallback =
            new LoaderManager.LoaderCallbacks<Void>() {
                @Override
                public Loader<Void> onCreateLoader(int id, Bundle args) {
                    return new BluetoothConnectTask(mActivity, mSocket);
                }

                @Override
                public void onLoadFinished(Loader<Void> loader, Void data) {
                    HandlerUtils.postOnMain(new Runnable() {
                        @Override
                        public void run() {
                            mBus.post(TLPercEvent.CONNECTED);
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<Void> loader) {

                }
            };

    private final LoaderManager.LoaderCallbacks<Void> mDisconnectCallback =
            new LoaderManager.LoaderCallbacks<Void>() {
                @Override
                public Loader<Void> onCreateLoader(int id, Bundle args) {
                    return new BluetoothDisconnectTask(mActivity, mSocket);
                }

                @Override
                public void onLoadFinished(Loader<Void> loader, Void data) {

                }

                @Override
                public void onLoaderReset(Loader<Void> loader) {

                }
            };
}
