/*******************************************************************************
 * Copyright (c) 2012 Akihiro Komori
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.blogspot.comolog.emptytub;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EmptyTubActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mViewPlayList = (ListView)findViewById(R.id.listViewPlaylist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                	TextView view = (TextView)super.getView(position, convertView, parent);
                    view.setTextSize(14.0f);
                    return view;
                }        		
        };
        mViewPlayList.setAdapter(adapter);
        mViewPlayList.setOnItemClickListener(mViewPlayListClickedListener);
        initializeAudioFileList();
        startWatchingExternalStorage();
    }

	@Override
	protected void onDestroy() {
        stopWatchingExternalStorage();
		super.onDestroy();
	}

	/**
	 * Initialize the audio file list.
	 * @see <a href="https://groups.google.com/group/android-group-japan/browse_thread/thread/9e5884d86221898d?hl=ja">ContentProviderから音楽のAlbum_Artを 取得したいのです。</a>
	 */
	private void initializeAudioFileList() {
		mAudios = new ArrayList<AudioMediaMetadata>();
        ContentResolver resolver = getContentResolver();
        
        Cursor cursor = resolver.query(
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI , 
        		new String[]{
        				MediaStore.Audio.Media.ALBUM ,
        				MediaStore.Audio.Media.ARTIST ,
        				MediaStore.Audio.Media.TITLE,
        				MediaStore.Audio.Media.DATA
        		},
        		null,
        		null,
        		null
        );
        
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)mViewPlayList.getAdapter();
        while( cursor.moveToNext() ){
        	final String album = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM ) );
        	final String artist = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST ) );
        	final String title = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.TITLE ) );
        	final String data = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.DATA ) );
        	Log.d("TEST" , "====================================");
        	Log.d("TEST" , album);
        	Log.d("TEST" , artist);
        	Log.d("TEST" , title);
        	Log.d("TEST" , data);
        	mAudios.add(new AudioMediaMetadata(album, artist, title, data));
        	adapter.add(album + " : " + artist + " : " + title);
        }		
	}

	private void handleStorageState() {
		if (isExternalStorageWritable()) {
	        String path = Environment.getExternalStorageDirectory().getPath() + getString(R.string.data_file_path);        
	        DataManager.get().initialize(path);			
		} else {
	        String path = Environment.getDataDirectory().getPath() + getString(R.string.data_file_path);        
	        DataManager.get().initialize(path);
		}
	}
	
	private boolean isExternalStorageWritable() {
		return mExternalStorageWriteable;
	}

	private void updateExternalStorageState() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	    } else {
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	    }
	    
	    handleStorageState();
	}

	private void startWatchingExternalStorage() {
	    mExternalStorageReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            updateExternalStorageState();
	        }
	    };
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
	    filter.addAction(Intent.ACTION_MEDIA_REMOVED);
	    registerReceiver(mExternalStorageReceiver, filter);
	    updateExternalStorageState();
	}

	private void stopWatchingExternalStorage() {
	    unregisterReceiver(mExternalStorageReceiver);
	}
	
	private BroadcastReceiver mExternalStorageReceiver;
	@SuppressWarnings("unused")
	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
    
	private ArrayList<AudioMediaMetadata> mAudios;
	private ListView mViewPlayList;
	
	private OnItemClickListener mViewPlayListClickedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getApplicationContext(), PlayerRecorderActivity.class);
			i.putExtra(PlayerRecorderActivity.ARG_0, mAudios.get(arg2).mData);
			startActivity(i);
			
			
		}
	};
}
