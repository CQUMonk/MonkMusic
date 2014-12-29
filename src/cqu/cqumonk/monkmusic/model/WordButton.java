package cqu.cqumonk.monkmusic.model;

import android.widget.Button;

/*
 * 文字按钮
 * 
 */
public class WordButton {
	private int mIndex;

	private boolean mIsVisible;

	private Button mViewButton;

	private String mWord;
	
	private boolean mIsAnswer;

	public WordButton()
	{
		mIsVisible=true;
		setIsAnswer(false);
		mWord="";
	}

	public int getmIndex() {
		return mIndex;
	}

	public Button getmViewButton() {
		return mViewButton;
	}

	public String getmWord() {
		return mWord;
	}

	public boolean ismIsVisible() {
		return mIsVisible;
	}
	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}
	public void setmIsVisible(boolean mIsVisible) {
		this.mIsVisible = mIsVisible;
	}
	
	public void setmViewButton(Button mViewButton) {
		this.mViewButton = mViewButton;
	}
	
	public void setmWord(String mWord) {
		this.mWord = mWord;
	}

	public boolean IsAnswer() {
		return mIsAnswer;
	}

	public void setIsAnswer(boolean mIsAnswer) {
		this.mIsAnswer = mIsAnswer;
	}

}
