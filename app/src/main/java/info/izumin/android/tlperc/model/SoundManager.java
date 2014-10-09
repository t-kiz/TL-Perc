package info.izumin.android.tlperc.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by izumin on 2014/10/09.
 */
public class SoundManager {
    public static final String TAG = SoundManager.class.getSimpleName();

    private static final int MAX_STREAMS = 8;

    private Context mContext;
    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private Map<String, Integer> mLoadedSounds;

    private int mSoundVolume;

    public SoundManager(Context context) {
        mContext = context;
        initialize();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mSoundVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void load(String key, int rawId) {
        int soundId = mSoundPool.load(mContext, rawId, 0);
        mLoadedSounds.put(key, soundId);
    }

    public int play(String key) {
        int soundId = mLoadedSounds.get(key);
        return mSoundPool.play(soundId, mSoundVolume, mSoundVolume, 0, 0, 1.0F);
    }

    public void initialize() {
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mLoadedSounds = new HashMap<String, Integer>();
    }

    public void release() {
        mSoundPool.release();
    }

    public Set<String> getLoadedSoundKeys() {
        return mLoadedSounds.keySet();
    }
}
