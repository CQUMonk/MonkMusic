package cqu.cqumonk.monkmusic.myui;

import java.util.ArrayList;

import cqu.cqumonk.monkmusic.R;
import cqu.cqumonk.monkmusic.model.IWordButtonClickListener;
import cqu.cqumonk.monkmusic.model.WordButton;
import cqu.cqumonk.monkmusic.util.Util;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView {
	//文字的个数
	public final static int COUNT=24;
	//声明一个放wordButton的容器
	private ArrayList<WordButton> mArrayList=new ArrayList<WordButton>();
	//声明自定义的adapter对象
	private MyGridAdapter mAdapter;
	
	private Context mContext;
	//缩放动画
	private Animation mScaleAnimation;
	
	private IWordButtonClickListener mButtonClickListener;
	
	
	//因为要放入布局管理器中所以选择这个构造函数，如果无需放入布局管理器，则选择第一个也可以
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//设置自定义gridview的adapter
		mAdapter=new MyGridAdapter();
		this.setAdapter(mAdapter);
		
		mContext=context;
		
	}
	public  void updateData(ArrayList<WordButton> list) {
		mArrayList=list;
		
		//更新数据源
		this.setAdapter(mAdapter);
		
		
	}
	class MyGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			//返回数据源中已经定义的数据总量，即容器中view的数目
			return mArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			//返回当前选择的控件
			return mArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// 返回当前控件索引
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 取得目前需要显示的view
			final WordButton holder;
			if(convertView==null)
			{
				//获得自定义的view布局
				convertView=Util.getView(mContext, R.layout.ui_gridview_item);
				//从数据源中取出需要显示的button，设置自定义的button的属性
				holder=mArrayList.get(position);
				holder.setmIndex(position);
				//判断wordButton中的button是否为null
				if (holder.getmViewButton()==null) {
					//如果为null，从view布局中找到button，将布局中的button与wordbutton关联，并设置其属性
					
					holder.setmViewButton((Button)convertView.findViewById(R.id.btn_item));
					
					
					holder.getmViewButton().setText(holder.getmWord());
					holder.getmViewButton().setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mButtonClickListener.onWordButtonClick(holder);
						}
					});
				}

				//将该button设置为该布局view的tag，这样方便直接取出
				convertView.setTag(holder);
				
				//加载动画
				mScaleAnimation=AnimationUtils.loadAnimation(mContext, R.anim.scale);
				mScaleAnimation.setStartOffset(100*position);
			}else {
				holder=(WordButton) convertView.getTag();
			}

			
			//播放动画
			convertView.startAnimation(mScaleAnimation);
			
			return convertView;
		}
		
	}
	
	//注册监听器
	public void registerOnWordButtonClick(IWordButtonClickListener Listener)
	{
		mButtonClickListener=Listener;
		
	}

}
