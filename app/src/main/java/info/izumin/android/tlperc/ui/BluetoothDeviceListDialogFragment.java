package info.izumin.android.tlperc.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by izumin on 2014/10/10.
 */
public class BluetoothDeviceListDialogFragment extends DialogFragment {
    public static final String TAG = BluetoothDeviceListDialogFragment.class.getSimpleName();

    private Callback mCallback;
    private BluetoothDevice[] mDevices;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> uuids = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            uuids.add(device.getName());
        }

        if (mCallback == null && getTargetFragment() instanceof Callback) {
            mCallback = (Callback) getTargetFragment();
        } else if (mCallback == null && getActivity() instanceof Callback) {
            mCallback = (Callback) getActivity();
        }

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity())
                .setItems(uuids.toArray(new CharSequence[uuids.size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCallback != null) {
                                    try {
                                        mCallback.onDeviceDecided(mDevices[which]);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

        return mDialogBuilder.create();
    }

    public void setBluetoothDevices(Set<BluetoothDevice> devices) {
        mDevices = devices.toArray(new BluetoothDevice[devices.size()]);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onDeviceDecided(BluetoothDevice device) throws IOException;
    }
}
