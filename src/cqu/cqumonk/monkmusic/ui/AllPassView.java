package cqu.cqumonk.monkmusic.ui;

import cqu.cqumonk.monkmusic.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 通关界面
 * @author Administrator
 *
 */
public class AllPassView extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_pass_view);
		FrameLayout frameLayout=(FrameLayout) findViewById(R.id.layout_bar_coin);
		frameLayout.setVisibility(View.INVISIBLE);
	}

}
