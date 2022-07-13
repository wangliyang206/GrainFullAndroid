package com.baidu.idl.face.platform.ui.task;

import android.os.AsyncTask;

public class MediaPrepareTask extends AsyncTask<Boolean, Void, Integer> {

    private MediaListener mMediaListener;

    public MediaPrepareTask(MediaListener mediaListener) {
        mMediaListener = mediaListener;
    }

    @Override
    protected Integer doInBackground(Boolean... isStarts) {
        if (isCancelled()) {
            return -1;
        }

        return mMediaListener.doInBackground(isStarts[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        mMediaListener.onPostExecute(result);
    }
}
