package com.blogspot.comolog.emptytub;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class PlayerRecorderActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.playerrecorder);
        Intent i = getIntent();
        String keyword = i.getStringExtra(ARG_0);
        
        TextView tv = (TextView)findViewById(R.id.textViewTitle);
        tv.setText(keyword);
        
        mMediaPlayer = new MediaPlayer();//.create(this, Uri.fromFile(new File(keyword)));
        try {
			mMediaPlayer.setDataSource(keyword);
			mMediaPlayer.prepare();
	        mMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	@Override
	protected void onDestroy() {
		mMediaPlayer.stop();
		super.onDestroy();
	}

	
    public static final String ARG_0 = "ARG_0";
    private MediaPlayer mMediaPlayer;
}
