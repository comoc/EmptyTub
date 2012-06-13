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

import java.util.Date;

public class RecordingMetadata {
	public RecordingMetadata(String audioFilePath, String recordedFilePath, int beginTimestamp, Date recordedDate, String comment) {
		mAudioFilePath = audioFilePath;
		mRecordedFilePath = recordedFilePath;
		mBeginTimestamp = beginTimestamp;
		mRecordedDate = recordedDate;
		mComment = comment;
	}

	public final String getAudioFilePath() {
		return mAudioFilePath;
	}

	public final String getRecordedFilePath() {
		return mRecordedFilePath;
	}
	
	public final int getBeginTimestamp() {
		return mBeginTimestamp;
	}
	
	public final Date getRecordedDate() {
		return mRecordedDate;
	}
	
	public final String getComment() {
		return mComment;
	}
	
	private String mAudioFilePath;
	private String mRecordedFilePath;
	private int mBeginTimestamp;
	private Date mRecordedDate;
	private String mComment;
}
