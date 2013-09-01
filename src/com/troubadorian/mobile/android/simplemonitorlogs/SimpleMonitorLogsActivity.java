package com.troubadorian.mobile.android.simplemonitorlogs;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class SimpleMonitorLogsActivity extends Activity {
    private SimpleMonitorLogsService s;

    private ArrayList<String> values;
    
    private boolean mIsBound;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        doBindService();
        values = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        // List<String> wordList = s.getWordList();
        // Toast.makeText(this, wordList.get(0), Toast.LENGTH_LONG).show();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            s = ((SimpleMonitorLogsService.MyBinder) binder).getService();
            Toast.makeText(SimpleMonitorLogsActivity.this, "Simple Monitor v1.0 connected", Toast.LENGTH_SHORT)
                    .show();
        }
        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };

    private ArrayAdapter<String> adapter;
    void doBindService() {
        bindService(new Intent(this, SimpleMonitorLogsService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    
    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
    
    public void showServiceData(View view) {
        if (s != null) {
            List<String> wordList = s.getWordList();
            values.clear();
            values.addAll(wordList);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
        
    }
}
