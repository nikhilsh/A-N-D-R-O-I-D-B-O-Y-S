package com.androidboys.spellarena.ggps;

public interface IGoogleServices {
	
	public void signIn();
	public void signOut();
	
	public void rateGame();
	public void submitScore(long score);
	public void showScores();
	public boolean isSignedIn();
	
}
