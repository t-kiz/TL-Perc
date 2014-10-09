package info.izumin.android.tlperc.ui.helper;

import android.app.Activity;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Loader;
import android.os.Bundle;

import com.amalgam.os.HandlerUtils;
import com.squareup.otto.Bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import info.izumin.android.tlperc.event.BluetoothReadEvent;
import info.izumin.android.tlperc.event.TLPercEvent;
import info.izumin.android.tlperc.loader.task.BluetoothConnectTask;
import info.izumin.android.tlperc.loader.task.BluetoothDisconnectTask;
import info.izumin.android.tlperc.loader.task.BluetoothReadTask;
import info.izumin.android.tlperc.ui.BluetoothDeviceListDialogFragment;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothHelper implements BluetoothDeviceListDialogFragment.Callback {
    public static final String TAG = BluetoothHelper.class.getSimpleName();
    private BluetoothHelper self = this;

    private static final String DEVICE_LIST_FRAGMENT = "device list fragment";

    private Activity mActivity;
    private Bus mBus;
    private BluetoothDeviceListDialogFragment mBluetoothDeviceListDialogFragment;

    private BluetoothAdapter mAdapter;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    private BufferedReader mReader;

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
        mActivity.getLoaderManager().initLoader(1, null, mDisconnectCallback);
    }

    public void asyncRead() {
        mActivity.getLoaderManager().restartLoader(2, null, mReadCallback);
    }

    /* ================================================================
     *  Callback from DeviceListDialog
     * ================================================================ */

    @Override
    public void onDeviceDecided(BluetoothDevice device) throws IOException {
        mDevice = device;
        mSocket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
        mBus.post(TLPercEvent.DECIDED);
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
                    try {
                        mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                        HandlerUtils.postOnMain(new Runnable() {
                            @Override
                            public void run() {
                                mBus.post(TLPercEvent.CONNECTED);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private final LoaderManager.LoaderCallbacks<CharSequence> mReadCallback =
            new LoaderManager.LoaderCallbacks<CharSequence>() {
                @Override
                public Loader<CharSequence> onCreateLoader(int id, Bundle args) {
                    return new BluetoothReadTask(mActivity, mReader);
                }

                @Override
                public void onLoadFinished(Loader<CharSequence> loader, final CharSequence data) {
                    HandlerUtils.postOnMain(new Runnable() {
                        @Override
                        public void run() {
                            if (data == null) {
                                disconnect();
                                mBus.post(TLPercEvent.DISCONNECTED);
                            } else {
                                mBus.post(new BluetoothReadEvent(self, data));
                            }
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<CharSequence> loader) {

                }
            };
}
