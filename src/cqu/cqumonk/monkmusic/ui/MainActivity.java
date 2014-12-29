package cqu.cqumonk.monkmusic.ui;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cqu.cqumonk.monkmusic.R;
import cqu.cqumonk.monkmusic.R.integer;
import cqu.cqumonk.monkmusic.data.DataSource;
import cqu.cqumonk.monkmusic.model.IAlertDialogButtonClickListener;
import cqu.cqumonk.monkmusic.model.IWordButtonClickListener;
import cqu.cqumonk.monkmusic.model.Song;
import cqu.cqumonk.monkmusic.model.WordButton;
import cqu.cqumonk.monkmusic.myui.MyGridView;
import cqu.cqumonk.monkmusic.myui.MyPlayer;
import cqu.cqumonk.monkmusic.util.Util;
import android.R.color;
import android.R.layout;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements IWordButtonClickListener{

	//盘片控件
	private Animation mDiscAnim;
	private LinearInterpolator mDiscLin;
	private ImageView mIvDisc;
	private ImageView mIvPin;
	//pin in
	private Animation mPinInAnim;
	private LinearInterpolator mPinInLin;
	
	//pin out
	private Animation mPinOutAnim;
	private LinearInterpolator mPinOutLin;
	//playstart button
	private ImageButton mBtnPlayStart;
	//是否正在运行
	private boolean isRunning=false;
	
	//待选文字框容器
	private ArrayList<WordButton> mAllWord;
	private MyGridView mMyGridView;
	//已选文字框容器
	private ArrayList<WordButton> mWordSelected;
	private LinearLayout mWordSelectedContainer;
	
	
	//当前关卡索引
	private int mStageIndex=0;
	private TextView mLevelView;
	//当前歌曲
	private Song mCurrentSong;
	//过关界面
	private View mPassView;
	//当前金币数量
	private int mCurrentCoins=DataSource.TOTAL_COINS;
	//显示金币的View
	private TextView mViewCurrentCoins;
	
	//过关界面的控件
	//当前关索引显示textView
	private TextView mStageTextView;
	//当前歌曲名称textview
	private TextView mSongTextView;
	//下一题按钮和分享到微信button
	private ImageButton mNextButton;
	private ImageButton mWeixinButton;
	/**
	 * 答案状态
	 */
	public static final int STATUS_ANSWER_RIGHT=1;
	public static final int STATUS_ANSWER_WRONG=2;
	public static final int STATUS_ANSWER_LACK=3;
	/**
	 * 对话框种类
	 * 
	 */
	public static final int ID_DEL_WORD=1;
	public static final int ID_TIP=2;
	public static final int ID_LACK_COIN=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//从文件中读取数据
		int[] datas=Util.readData(MainActivity.this);
		mCurrentCoins=datas[DataSource.INDEX_COIN];
		mStageIndex=datas[DataSource.INDEX_STAGE];
		//initate the animation
		InitAnim();
		InitView();
		initCurrentStateData();
		//去掉一个错误答案btn
		handleDeleteWrong();
		//提示一个正确答案
		handleTipAnswer();


	}
	@Override
	protected void onPause() {
		mIvDisc.clearAnimation();
		MyPlayer.stopSong(MainActivity.this);
		Util.saveData(MainActivity.this, mCurrentCoins, mStageIndex-1);
		super.onPause();
		
	}
	/*
	 * 初始化控件
	 */
	private void InitView() {
		//播放按钮
		mBtnPlayStart=(ImageButton) findViewById(R.id.play_start);
		mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				handlePlayButton();
			}
		});		
		//初始化盘片和磁头的imageview
		mIvDisc=(ImageView) findViewById(R.id.iv1);
		mIvPin=(ImageView) findViewById(R.id.iv2);
		//初始化MyGridView,以及监听
		mMyGridView=(MyGridView) findViewById(R.id.gridview);
		mMyGridView.registerOnWordButtonClick(this);
		
		//初始化已选文字框容器
		mWordSelectedContainer=(LinearLayout) findViewById(R.id.word_select_container);
		//初始化金币显示testview
		mViewCurrentCoins=(TextView) findViewById(R.id.txt_bar_coins);
		mViewCurrentCoins.setText(mCurrentCoins+"");
		//当前关卡索引显示
		mLevelView=(TextView) findViewById(R.id.level_title);
	}
	/*
	 * 播放按钮事件处理
	 */
	private void handlePlayButton()
	{
		if (mIvPin!=null) {
			if (!isRunning) {
				isRunning = true;
				mIvPin.startAnimation(mPinInAnim);
				mBtnPlayStart.setVisibility(View.INVISIBLE);
				//播放音乐
				MyPlayer.playSong(MainActivity.this, mCurrentSong.getFileName());

			}
		}
		
	}
	//初始化动画以及其监听事件
	private void InitAnim() {
		//盘片

		mDiscAnim=AnimationUtils.loadAnimation(this,R.anim.rotate);
		mDiscLin=new LinearInterpolator();
		mDiscAnim.setInterpolator(mDiscLin);
		mDiscAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// 盘片旋转完毕，磁头移开
				mIvPin.startAnimation(mPinOutAnim);
				
				
			}
		});
		//磁头进入
		mPinInAnim=AnimationUtils.loadAnimation(this,R.anim.rotate_45);
		mPinInLin=new LinearInterpolator();
		mPinInAnim.setInterpolator(mPinInLin);
		mPinInAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// 磁头进入完毕，盘片开始旋转
				mIvDisc.startAnimation(mDiscAnim);
				
			}
		});
		//磁头移开
		mPinOutAnim=AnimationUtils.loadAnimation(this,R.anim.rotate_d45);
		mPinOutLin=new LinearInterpolator();
		mPinOutAnim.setInterpolator(mPinOutLin);
		mPinOutAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// 磁头移开，播放结束
				isRunning=false;
				mBtnPlayStart.setVisibility(View.VISIBLE);
				
			}
		});
		
	}
	//初始化当前关卡的数据
	private void initCurrentStateData() {
		//获取歌曲信息
		mCurrentSong=DataSource.SONG_INFO.get(++this.mStageIndex);
		//音乐播放
		handlePlayButton();
		
		//获取数据
		mAllWord=initAllWordButtons();
		
		
		//更新Mygridview数据
		mMyGridView.updateData(mAllWord);
		//初始化已选文字框
		mWordSelectedContainer.removeAllViews();
		mWordSelected=initWordSelected();
		LayoutParams params=new LayoutParams(150, 150);
		for(int i=0;i<mWordSelected.size();i++)
		{
			mWordSelectedContainer.addView(mWordSelected.get(i).getmViewButton(), params);
		}
		//初始化关卡索引
		mLevelView.setText(""+mStageIndex);
		
	}
	private String[] generateWords()
	{
		String[] words=new String[MyGridView.COUNT];
		Random random=new Random();
		
		//随机生成所有文字
		for (int i = 0; i < MyGridView.COUNT; i++) {
			words[i]=Util.getRandomChar()+"";
		}
		//在0-MyGridView.COUNT之间产生NameLength个随机数
		Set<Integer> set=Util.genDiffRandom(0, MyGridView.COUNT, this.mCurrentSong.getNameLength());
		//用歌名替换
		int index=0;
		for(int i:set)
		{
			words[i]=""+this.mCurrentSong.getNameCharacters()[index++];
		}
		return words;
	}
	/*
	 * 初始化待选文字框中的所有文字按钮
	 */
	
	private ArrayList<WordButton> initAllWordButtons() {
		ArrayList<WordButton> data =new ArrayList<WordButton>();
		
		//获取所有待选文字
		String[] words=generateWords();
		
		for(int i=0;i<MyGridView.COUNT;i++)
		{
			WordButton btn=new WordButton();
			btn.setmWord(words[i]);
			if(mCurrentSong.getSongName().contains(words[i]))
			{
				btn.setIsAnswer(true);
			}
			data.add(btn);
		}
		return data;
		
	}
	/*
	 * 初始化文字选择框
	 */
	private ArrayList<WordButton> initWordSelected() {
		ArrayList<WordButton> data =new ArrayList<WordButton>();
		for(int i=0;i<this.mCurrentSong.getNameLength();i++)
		{
			View view=Util.getView(MainActivity.this, R.layout.ui_gridview_item);
			final WordButton holder=new WordButton();
			Button btn=(Button)view.findViewById(R.id.btn_item);
			btn.setText("");
			btn.setTextColor(Color.WHITE);
			btn.setBackgroundResource(R.drawable.game_wordblank);
			holder.setmViewButton(btn);
			holder.setmIsVisible(false);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					clearSelectedWords(holder);
				}
			});

			
			data.add(holder);
			
		}
		return data;
		
	}
	//清除已选文字框中答案
	private void clearSelectedWords(WordButton wordButton)
	{
		//设置已选文字框中按钮属性
		wordButton.getmViewButton().setText("");
		wordButton.setmWord("");
		wordButton.setmIsVisible(false);
		//设置待选文字框中按钮属性
		WordButton button=mAllWord.get(wordButton.getmIndex());
		button.setmIsVisible(true);
		button.getmViewButton().setVisibility(View.VISIBLE);
	}
	//设置已选文字框中答案
	private void setSelectWord(WordButton wordButton) {
		for(int i=0;i<mWordSelected.size();i++)
		{
			if (mWordSelected.get(i).getmWord()=="") {
				//设置已选文字框中按钮的属性
				mWordSelected.get(i).getmViewButton().setText(wordButton.getmWord());
				mWordSelected.get(i).setmIsVisible(true);
				mWordSelected.get(i).setmWord(wordButton.getmWord());
				mWordSelected.get(i).setmIndex(wordButton.getmIndex());
				
				//设置待选文字框中按钮的属性
				wordButton.getmViewButton().setVisibility(View.INVISIBLE);
				wordButton.setmIsVisible(false);
				
				
				break;
				
			}
		}
	}
	@Override
	public void onWordButtonClick(WordButton wordButton) {
		//待选文字框中button点击事件,选择答案
		setSelectWord(wordButton);
		//获得当前答案状态
		int checkResult=checkTheAnswer();
		//检测答案并作出相关动作
		if(checkResult==STATUS_ANSWER_LACK){
			//答案不完整
			for(int i=0;i<mWordSelected.size();i++){
				mWordSelected.get(i).getmViewButton().setTextColor(Color.WHITE);
			}
		}else if (checkResult==STATUS_ANSWER_RIGHT) {
			//答案正确
			Toast.makeText(this, "pass",Toast.LENGTH_SHORT).show();
			handlePassEvent();
		}else {
		//答案错误，闪烁文字
			sparkSelectedWords();
		}
		
	}

	private int checkTheAnswer()
	{
		//用户答案
		StringBuffer answer=new StringBuffer();
		//如果已选文字框中的button中text属性有空白，说明答案不完整
		for(int i=0;i<mWordSelected.size();++i)
		{
			answer.append(mWordSelected.get(i).getmWord());
			if(mWordSelected.get(i).getmWord().length()==0)
			{
				return STATUS_ANSWER_LACK;
			}
		}
		if(answer.toString().equals(mCurrentSong.getSongName()))
		{
			return STATUS_ANSWER_RIGHT;
		}

		return STATUS_ANSWER_WRONG;
	}
	/**
	 * 当答案错误，闪烁已选文字框
	 */
	private void sparkSelectedWords() {
		//定时器
		TimerTask task=new TimerTask() {
			//颜色切换控制
			boolean mChange=false;
			//当前闪烁的次数
			int mSpardTimes=0;
			@Override
		//实现timertask中的任务
			public void run() {
				runOnUiThread(new Runnable() {
					//任何有关界面刷新的任务都要放在UI线程中执行
					@Override
					public void run() {
						//闪烁6次
						if(++mSpardTimes>5) return;
						//设置button颜色
						for(int i=0;i<mWordSelected.size();i++){
							mWordSelected.get(i).getmViewButton().setTextColor(
									mChange?Color.RED:Color.WHITE);
						}
						mChange=!mChange;
					}
				});
				
				
			}
		};
		Timer timer= new Timer();
		timer.schedule(task, 1, 150);
	}
	/**
	 * 处理过关事件
	 */
	private void handlePassEvent() {
		mPassView=(LinearLayout)this.findViewById(R.id.pass_view);
		mPassView.setVisibility(View.VISIBLE);
		//停止动画
		mIvDisc.clearAnimation();
		//停止音乐
		MyPlayer.stopSong(MainActivity.this);
		//播放音效
		MyPlayer.playTone(MainActivity.this, DataSource.TONE_COIN);
		//当前关索引
		mStageTextView=(TextView) findViewById(R.id.txt_current_stage_pass);
		if (mStageTextView!=null) {
			mStageTextView.setText(""+mStageIndex);
		}
		//歌曲名称
		mSongTextView=(TextView) findViewById(R.id.txt_songname_pass);
		if(mSongTextView!=null){
			mSongTextView.setText(mCurrentSong.getSongName());
		}
		//下一关按钮
		mNextButton=(ImageButton) findViewById(R.id.btn_next);
		if (mNextButton!=null) {
			mNextButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//处理跳转到下一关的事务
					

					if (isPassAllStage()) {
						//进入通关界面
						Util.StartActivity(MainActivity.this, AllPassView.class);
					} else {
						//进入下一关
						handlePassState();

					}
				}
			});
		}
		//分享到微信的按钮
		mWeixinButton=(ImageButton) findViewById(R.id.btn_share);
		if (mWeixinButton!=null) {
			mWeixinButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//处理分享到微信的事务
				}
			});
		}
		
	}
	/**
	 * 处理跳转到下一关事务
	 */
	private void handlePassState(){
		//过关奖励
		int coins=this.getResources().getInteger(R.integer.add_award);
		if (mViewCurrentCoins!=null) {
			mCurrentCoins+=coins;
			mViewCurrentCoins.setText(""+mCurrentCoins);
		}
		//隐藏过关画面
		mPassView.setVisibility(View.GONE);
		//加载下一关数据
		initCurrentStateData();
	}
	
	/**
	 * 判断是否通关
	 */
	public boolean isPassAllStage(){
		return mStageIndex==(DataSource.SONG_INFO.size());
	}
	/**
	 * 处理删除错误答案业务
	 */
	private void handleDeleteWrong()
	{
		ImageButton btn=(ImageButton) findViewById(R.id.btn_delete_word);
		final int coins=this.getResources().getInteger(R.integer.pay_delete_answer);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckCoins(-coins))
				{//金币足够
					showConfirmDialog(ID_DEL_WORD);
					
				}else {
					//金币不够
					showConfirmDialog(ID_LACK_COIN);
					return;
				}
				
			}
		});
	}
	/**
	 * 处理提示答案业务
	 */
	private void handleTipAnswer()
	{
		ImageButton btn=(ImageButton) findViewById(R.id.btn_tip);
		final int coins=this.getResources().getInteger(R.integer.pay_tip_answer);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckCoins(-coins)){
					//金币足够,显示扣除金币对话框,提示一个正确答案
					showConfirmDialog(ID_TIP);


				}else {
					//金币不够
					showConfirmDialog(ID_LACK_COIN);
					return;
				}
				
			}
		});
	}
	/**
	 * 检查增加或者减少金币，是否合法
	 * @param coins 金币数量
	 * @return 
	 */
	private boolean CheckCoins(int coins)
	{
		if(mCurrentCoins+coins>=0)
		{

			return true;
		}
		return false;
	}
	/**
	 * 删除一个错误答案，并扣除金币
	 */
	private void delWord(){
		int coins=this.getResources().getInteger(R.integer.pay_delete_answer);
		WordButton btn=findWrongAnswer();
		btn.getmViewButton().setVisibility(View.INVISIBLE);
		btn.setmIsVisible(false);
		
		mCurrentCoins-=coins;
		mViewCurrentCoins.setText(mCurrentCoins+"");
	}
	/**
	 * 提示一个正确答案，并扣除金币
	 */
	private void giveTip(){
		int coins=this.getResources().getInteger(R.integer.pay_tip_answer);
		if(findOneWord())
		{
			mCurrentCoins-=90;
			mViewCurrentCoins.setText(mCurrentCoins+"");
		}
	}
	/**
	 * 找到一个错误答案的词，且当前可见
	 * @return
	 */
	private WordButton findWrongAnswer() {
		Random random=new Random();
		WordButton btn=null;

		while (true) {
			
			int index=Math.abs(random.nextInt(mAllWord.size()));
			btn=mAllWord.get(index);
			//如果可见且为错误答案，则返回
			if (btn.ismIsVisible()&&!btn.IsAnswer()) {
				return mAllWord.get(index);
				
			}
		}
	}
	/**
	 * 提示一个正确答案
	 */
	private boolean findOneWord()
	{
		WordButton btn=null;
		String word=null;
		for(int i=0;i<mWordSelected.size();++i)
		{
			//遍历文字选择框按钮
			btn=mWordSelected.get(i);
			//正确答案
			word=""+mCurrentSong.getNameCharacters()[i];
			//如果当前文字选择框为空或者错误
			if(!word.equals(btn.getmWord()))
			{
				clearSelectedWords(btn);
				//从待选文字框中找到该答案按钮，触发点击事件
				for(WordButton b:mAllWord)
				{
					if (word.equals(b.getmWord())) {
						onWordButtonClick(b);
						return true;
					}
				}
				
			}
			
		}
		return false;
	}
	//自定义dialog中按钮事件相应
	
	//删除一个错误答案
	private IAlertDialogButtonClickListener mBtnOkDelWordListener=
			new IAlertDialogButtonClickListener() {
				
				@Override
				public void onDialogButtonClick() {
					//点击OK后执行的业务，去掉一个错误答案
					delWord();
				}
			};
	//提示一个正确答案
	private IAlertDialogButtonClickListener mBtnOkTipListener=
			new IAlertDialogButtonClickListener() {
				
				@Override
				public void onDialogButtonClick() {
					//点击OK后执行的业务，提示一个正确答案
					giveTip();
				}
			};
	//提示金币不足
	private IAlertDialogButtonClickListener mBtnOkStoreListener=
			new IAlertDialogButtonClickListener() {
				
				@Override
				public void onDialogButtonClick() {
					//点击OK后，进入商店
				}
			};
	/**
	 * 显示对话框
	 */
	private void showConfirmDialog(int id){
		switch (id) {
		case ID_DEL_WORD:
			Util.showDialog(MainActivity.this, "确认支付30金币去掉一个错误答案？", mBtnOkDelWordListener);
			break;
		case ID_TIP:
			Util.showDialog(MainActivity.this, "确认支付90金币提示一个正确答案？", mBtnOkTipListener);
			break;
		case ID_LACK_COIN:
			Util.showDialog(MainActivity.this, "金币不足？", mBtnOkStoreListener);
			break;

		}
	}
}
