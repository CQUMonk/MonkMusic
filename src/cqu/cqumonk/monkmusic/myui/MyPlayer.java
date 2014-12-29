package cqu.cqumonk.monkmusic.myui;

import java.io.IOException;

import cqu.cqumonk.monkmusic.data.DataSource;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
 * 音乐播放类，单例懒汉
 * @author Administrator
 *
 */
public class MyPlayer {
	

	private MyPlayer(){}
	private static final MediaPlayer songPlayer=new MediaPlayer();
	private static final MediaPlayer tonePlayer=new MediaPlayer();
	
	public static void  playTone(Context context,int index) {
		//强制重置，否则数据源只能设置一次，会报错
		tonePlayer.reset();
		AssetManager assetManager=context.getAssets();
		String fileName=DataSource.TONES.get(index);
		try {
			AssetFileDescriptor descriptor=assetManager.openFd(fileName);
			tonePlayer.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(),
					descriptor.getLength());
			tonePlayer.prepare();
			tonePlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 音乐播放
	 * @param context
	 * @param fileName
	 */
	public static void playSong(Context context,String fileName){
		
		//强制重置，否则数据源只能设置一次，会报错
		songPlayer.reset();
		//加载音乐文件
		AssetManager assetManager=context.getAssets();
		try {
			AssetFileDescriptor descriptor=assetManager.openFd(fileName);
			songPlayer.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), 
					descriptor.getLength());
			songPlayer.prepare();
			//播放
			songPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void stopSong(Context context){
		songPlayer.stop();
	}
	
}
