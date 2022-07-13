package com.baidu.idl.face.platform.ui.task;

public interface MediaListener {
    int doInBackground(boolean isStart);

    void onPostExecute(Integer result);
}
