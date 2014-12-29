package cqu.cqumonk.monkmusic.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cqu.cqumonk.monkmusic.R;
import cqu.cqumonk.monkmusic.R.integer;
import cqu.cqumonk.monkmusic.data.DataSource;
import cqu.cqumonk.monkmusic.model.IAlertDialogButtonClickListener;
import cqu.cqumonk.monkmusic.myui.MyPlayer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Util {
	private static  AlertDialog mAlertDialog;
	//获取到自定义的layout
	public static View getView(Context context,int layoutId)
	{
		LayoutInflater inflater=(LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//LayoutInflater inflater=getLayoutInflater();
		View layout=inflater.inflate(layoutId, null);
		
		return layout;
	}
	
	public static char getRandomChar()
	{
		//生成随机汉字
		String str = "";
		int highPos;
		int lowPos;
		
		Random random=new Random();
		
		highPos=(176+(Math.abs(random.nextInt(39))));
		lowPos=(161+(Math.abs(random.nextInt(93))));
		
		byte[] b=new byte[2];
		b[0]=(Integer.valueOf(highPos)).byteValue();
		b[1]=(Integer.valueOf(lowPos)).byteValue();
		try {
			str=new String(b,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.charAt(0);
	}
	/**
	 * activity跳转
	 * @param from
	 * @param to
	 */
	public static void StartActivity(Context from,Class to)
	{
		Intent intent=new Intent();
		intent.setClass(from , to);
		from.startActivity(intent);
		//关闭当前activity
		((Activity) from).finish();
	}
	/**
	 * 显示自定义对话框
	 * @param context
	 * @param msg
	 * @param listener
	 */
	public static void showDialog
	(final Context context,String msg,final IAlertDialogButtonClickListener listener){
		//自定义的View
		View dialogView=null;
		

		//获取到自定义的view
		dialogView=getView(context, R.layout.dialog_view);
		//获取到自定义view中的控件，并进行设置
		ImageButton btn_cancel=(ImageButton) dialogView.findViewById(R.id.btn_cancel);
		ImageButton btn_ok=(ImageButton) dialogView.findViewById(R.id.btn_ok);
		TextView txt_msg=(TextView) dialogView.findViewById(R.id.txt_msg);
		txt_msg.setText(msg);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mAlertDialog!=null) {
					mAlertDialog.cancel();
				}
				//播放音效
				MyPlayer.playTone(context, DataSource.TONE_CANCEL);
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mAlertDialog!=null) {
					mAlertDialog.cancel();
				}
				//回调
				if (listener!=null) {
					listener.onDialogButtonClick();
				}
				
			}
		});
		//创建Android的对话框，并设置对话框中的view
		AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.Theme_Transparent);
		builder.setView(dialogView);
		mAlertDialog=builder.create();
		mAlertDialog.show();
		//播放音效
		MyPlayer.playTone(context, DataSource.TONE_ENTER);
		
	}
	/**
	 * 在start和end之间（不包括end）产生count个不重复的随机数
	 */
	public static Set<Integer> genDiffRandom(int start,int end,int count){
		HashSet<Integer> set=new HashSet<Integer>();
		if(end-start<count)
		{
			return set;
		}
		Random random=new Random();
		for(int i=0;i<count;++i){
			while(!set.add(random.nextInt(end-start)+start));
		}
		return set;
	}
	/**
	 * 将金币数和当前关卡存入文件
	 * @param context
	 * @param coins
	 * @param stageIndex
	 */
	public static void saveData(Context context,int coins,int stageIndex){
		FileOutputStream fos=null;
		
		try {
			fos=context.openFileOutput(DataSource.FILE_NAME, Context.MODE_PRIVATE);
			DataOutputStream dos=new DataOutputStream(fos);
			dos.writeInt(coins);
			dos.writeInt(stageIndex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	}
	public static int[] readData(Context context){
		FileInputStream fis=null;
		int data[]={DataSource.TOTAL_COINS,0};
		try {
			fis=context.openFileInput(DataSource.FILE_NAME);
			DataInputStream dis=new DataInputStream(fis);
			data[DataSource.INDEX_COIN]=dis.readInt();
			data[DataSource.INDEX_STAGE]=dis.readInt();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return data;
		
	}

}
