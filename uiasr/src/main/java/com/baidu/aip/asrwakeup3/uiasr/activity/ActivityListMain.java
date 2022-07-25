package com.baidu.aip.asrwakeup3.uiasr.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityListMain extends ListActivity {

    public static final String CATEGORY_SAMPLE_CODE = "com.baidu.speech.recognizerdemo.intent.category.SAMPLE_CODE";

    private static final String TAG = "ActivityListMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // sp.edit().remove(SpeechConstant.IN_FILE).commit();
        // infile参数用于控制识别一个PCM音频流（或文件），每次进入程序都将该值清除，以避免体验时没有使用录音的问题
        setListAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1}));
    }

    /**
     * 获取Demo列表
     *
     * @return
     */
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(CATEGORY_SAMPLE_CODE);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list) {
            return myData;
        }

        int len = list.size();

        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            if (!getPackageName().equalsIgnoreCase(info.activityInfo.packageName)) {
                continue;
            }
            CharSequence labelSeq = info.loadLabel(pm);
            CharSequence description = null;
            if (info.activityInfo.descriptionRes != 0) {
                description = pm.getText(info.activityInfo.packageName,
                        info.activityInfo.descriptionRes, null);
            }

            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            addItem(myData,
                    label,
                    activityIntent(info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name), description);
        }
        return myData;
    }

    private void addItem(List<Map<String, Object>> data, String name, Intent intent,
                         CharSequence description) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        if (description != null) {
            temp.put("description", description.toString());
        }
        temp.put("intent", intent);
        data.add(temp);
    }

    private Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.putExtra("name1", componentName);
        result.putExtra("name2", componentName);
        result.setClassName(pkg, componentName);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}