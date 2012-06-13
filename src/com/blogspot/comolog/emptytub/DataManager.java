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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.*;

/**
 * DataManager
 * @author komori
 * The DataManager class manage Audio file and it's recoded meta-data.
 */
public class DataManager {
	public static DataManager get() {
		// DCL for thread safe.
		if (mInstance == null) {
			synchronized(DataManager.class) {
				if (mInstance == null) {
					mInstance = new DataManager();					
				}
			}
		}
		return mInstance;
	}
	
	public DataManager() {
		mMetas = new ArrayList<RecordingMetadata>();
	}
	
	public int initialize(String dataFilePath) {
		if (dataFilePath == null)
			return 0;
		mDataFilePath = dataFilePath;
		return load();
	}
	
	public void terminate() {
		save();
	}

	public String getDataFilePath() {
		return mDataFilePath;
	}
	
	public int getRecodingMetadataCount() {
		if (mMetas != null)
			return mMetas.size();
		return 0;
	}
	
	public RecordingMetadata getRecodingMetadata(int n) {
		if (n < 0 || n >= mMetas.size())
			return null;
		return mMetas.get(n);
	}
	
	public void startRecoding(String audioFilePath) {
		//mMetas.;
		mCurrentRecodingIndex = -1;
		for (RecordingMetadata meta : mMetas) {
			mCurrentRecodingIndex ++;
			if (meta.getAudioFilePath().equals(audioFilePath))
				break;			
		}
		
		// create new recoding file
		if (mCurrentRecodingIndex == -1) {
			mMetas.add(new RecordingMetadata(audioFilePath, null, 0, new Date(), null));
		}
	}

	public int load() {
        try {
        	BufferedReader br = new BufferedReader(new FileReader(mDataFilePath));
        	StringBuilder total = new StringBuilder();
        	String line;
        	while ((line = br.readLine()) != null) {
        	    total.append(line);
        	}
        	
        	mMetas = new ArrayList<RecordingMetadata>();
        	JSONArray jsonArray = new JSONArray(total.toString());
        	for (int i = 0; i < jsonArray.length(); i++) {
        		JSONObject json = (JSONObject) jsonArray.get(i);        		
        		//XXX check whether audio file and recorded file are existing or not.
        		mMetas.add(new RecordingMetadata(
        				json.optString(KEY_AUDIO_FILE_PATH),
        				json.optString(KEY_RECORDED_FILE_PATH),
        				json.optInt(KEY_BEGIN_TIMESTAMP),
        				ISO8601DateParser.parse(json.optString(KEY_RECORDED_DATE)),
        				json.optString(KEY_COMMENT)));
        	}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();			
        } catch (IOException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();			
        } catch (java.text.ParseException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();			
        }
        
		return getRecodingMetadataCount();
	}
	
	public void save() {
		JSONArray jsonArray = new JSONArray();
		for (RecordingMetadata meta : mMetas) {
			JSONObject tmp = new JSONObject();
    		//XXX check whether audio file and recorded file are existing or not.			
			try {
				tmp.put(KEY_AUDIO_FILE_PATH, meta.getAudioFilePath());
				tmp.put(KEY_RECORDED_FILE_PATH, meta.getRecordedFilePath());
				tmp.put(KEY_BEGIN_TIMESTAMP, meta.getBeginTimestamp());
				tmp.put(KEY_RECORDED_DATE, ISO8601DateParser.toString(meta.getRecordedDate()));
				tmp.put(KEY_COMMENT, meta.getComment());
				jsonArray.put(tmp);
			} catch (JSONException e) {
				if (BuildConfig.DEBUG)
					e.printStackTrace();
			}
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(mDataFilePath));
			bw.write(jsonArray.toString());
			bw.close();			
		} catch (IOException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();			
		}
	}
	
	private static DataManager mInstance;
	private ArrayList<RecordingMetadata> mMetas;
	private String mDataFilePath;
	private static final String KEY_AUDIO_FILE_PATH = "AudioFilePath";
	private static final String KEY_RECORDED_FILE_PATH = "RecordedFilePath";
	private static final String KEY_BEGIN_TIMESTAMP = "BeginTimestamp";
	private static final String KEY_RECORDED_DATE = "RecordedDate";
	private static final String KEY_COMMENT = "Comment";
	private int mCurrentRecodingIndex;
}
