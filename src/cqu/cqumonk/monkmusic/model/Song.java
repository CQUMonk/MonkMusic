package cqu.cqumonk.monkmusic.model;

public class Song {
	//歌曲文件名称
	private String mFileName;
	//名称长度
	private int mNameLength;
	//歌曲名称
	private String mSongName;
	
	
	
	public Song(String file,String name){
		this.mFileName=file;
		this.mSongName=name;
		this.mNameLength=this.mSongName.length();
		
	}
	public String getFileName() {
		return mFileName;
	}
	public int getNameLength() {
		return mNameLength;
	}
	public String getSongName() {
		return mSongName;
	}
	public void setFileName(String FileName) {
		this.mFileName = FileName;
	}
	public void setSongName(String SongName) {
		this.mSongName = SongName;
		this.mNameLength=this.mSongName.length();
	}
	
	public char[] getNameCharacters() {
		return this.mSongName.toCharArray();
	}

}
