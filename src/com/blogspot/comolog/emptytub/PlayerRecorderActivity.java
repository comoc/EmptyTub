package com.blogspot.comolog.emptytub;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerRecorderActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.playerrecorder);
        Intent i = getIntent();
        String album = i.getStringExtra(ARG_ALBUM);
        String artist = i.getStringExtra(ARG_ARTIST);
        String title = i.getStringExtra(ARG_TITLE);
        String data = i.getStringExtra(ARG_DATA);
        
        TextView tv;
        tv = (TextView)findViewById(R.id.textViewAlbum);
        tv.setText(album);
        tv = (TextView)findViewById(R.id.textViewArtist);
        tv.setText(artist);
        tv = (TextView)findViewById(R.id.textViewTitle);
        tv.setText(title);
        tv = (TextView)findViewById(R.id.textViewData);
        tv.setText(data);
        
        mMediaPlayer = new MediaPlayer();//.create(this, Uri.fromFile(new File(keyword)));
        try {
			mMediaPlayer.setDataSource(data);
			mMediaPlayer.prepare();
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
        
        ImageButton ib = (ImageButton)findViewById(R.id.imageButtonPlay);
        ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMediaPlayer.isPlaying()) {
			        ImageButton ib = (ImageButton)findViewById(R.id.imageButtonPlay);
			        ib.setImageResource(android.R.drawable.ic_media_play);
					mMediaPlayer.pause();
				} else {
			        ImageButton ib = (ImageButton)findViewById(R.id.imageButtonPlay);
			        ib.setImageResource(android.R.drawable.ic_media_pause);
					mMediaPlayer.start();
				}
				
			}
        });
    }
    
	@Override
	protected void onDestroy() {
		mMediaPlayer.stop();
		super.onDestroy();
	}

    public static final String ARG_ALBUM = "ARG0";
    public static final String ARG_ARTIST = "ARG1";
    public static final String ARG_TITLE = "ARG2";
    public static final String ARG_DATA = "ARG3";
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
}
