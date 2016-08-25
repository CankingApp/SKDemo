package com.canking.scdemo;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.IntDef;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by changxing on 16-8-25.
 */
public class RingtoneSetting extends SysSettingBase {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AudioManager.RINGER_MODE_NORMAL,
            AudioManager.RINGER_MODE_VIBRATE,
            AudioManager.RINGER_MODE_SILENT
    })
    @interface RingerCode {
    }

    public RingtoneSetting(Context ctx) {
        mAppCtx = ctx;
    }

    @Override
    public int getState() {
        AudioManager audioMgr = (AudioManager) mAppCtx.getSystemService(Context.AUDIO_SERVICE);
        if (audioMgr != null) {
            return audioMgr.getRingerMode();
        }
        return AudioManager.RINGER_MODE_NORMAL;
    }

    @Override
    public void setState(@RingerCode int state) {
        AudioManager audioMgr = (AudioManager) mAppCtx.getSystemService(Context.AUDIO_SERVICE);
        audioMgr.setRingerMode(state);
    }

    @Override
    public void toggleState() {
        AudioManager audioMgr = (AudioManager) mAppCtx.getSystemService(Context.AUDIO_SERVICE);
        String msgid = "no_access_of_ringer";
        int silentMode = audioMgr.getRingerMode();
        boolean tryNormal = false;
        if (silentMode == AudioManager.RINGER_MODE_SILENT) {
            audioMgr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if (silentMode == audioMgr.getRingerMode()) {
                tryNormal = true;
            } else {
                msgid = "sound_vibrate";
            }
        }
        if (silentMode == AudioManager.RINGER_MODE_VIBRATE || tryNormal) {
            audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if (silentMode != audioMgr.getRingerMode()) {
                msgid = "sound_normal";
            }
        }
        if (silentMode == AudioManager.RINGER_MODE_NORMAL) {
            audioMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            if (silentMode != audioMgr.getRingerMode()) {
                msgid = "sound_silent";
            }
        }
        boolean modeNotSupport = silentMode != AudioManager.RINGER_MODE_SILENT
                && silentMode != AudioManager.RINGER_MODE_VIBRATE
                && silentMode != AudioManager.RINGER_MODE_NORMAL;
        if (modeNotSupport) {
            audioMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            if (silentMode != audioMgr.getRingerMode()) {
                msgid = "sound_silent";
            }
        }
        Toast.makeText(mAppCtx, "sound:" + msgid, Toast.LENGTH_LONG).show();
    }
}
