package cqu.cqumonk.monkmusic.data;

import java.util.ArrayList;
import java.util.HashMap;

import cqu.cqumonk.monkmusic.model.Song;


public class DataSource {
	public static final int TOTAL_COINS=1000;
	
	public static final int TONE_ENTER=0;
	public static final int TONE_COIN=1;
	public static final int TONE_CANCEL=2;
	
	public static final String FILE_NAME="data.dat";
	
	public static final int INDEX_STAGE=1;
	public static final int INDEX_COIN=0;
	public static final HashMap<Integer, Song> SONG_INFO=new HashMap<Integer, Song>(){
		{
			
			put(1, new Song("__00001.m4a","童话"));
			put(2, new Song("__00002.m4a","同桌的你"));
			put(3, new Song("__00003.m4a","七里香"));
			put(4, new Song("__00004.m4a","传奇"));
			put(5, new Song("__00005.m4a","大海"));
			put(6, new Song("__00006.m4a","后来"));
			put(7, new Song("__00007.m4a","你的背包"));
			put(8, new Song("__00008.m4a","再见"));
			put(9, new Song("__00009.m4a","老男孩"));
			put(10, new Song("__00010.m4a","龙的传人"));
			put(11, new Song("__00000.m4a","征服"));
		}
		
	};
	public static final ArrayList<String> TONES=new ArrayList<String>(){
		{
			add(0,"enter.mp3");
			add(1,"coin.mp3");
			add(2,"cancel.mp3");
		}

	};
			

}
