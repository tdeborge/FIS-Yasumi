package com.rhemea.puzzler.blocks.gui;

public interface IPuzzleCallBack {
	public void beginBlokNr(int i);
	public void setResult(java.util.ArrayList <char[][]>res);
	public void setLastResult(java.util.ArrayList<char[][]> res);
}