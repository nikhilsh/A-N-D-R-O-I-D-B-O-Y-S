package com.androidboys.spellarena.ggps;

public interface ActionResolver {
	
    public void showToast(CharSequence text);
    public void broadcastMsg(byte[] msg);
    public void onGameEnded();
    public void setMeAsReady();
    public void signIn();
	
}
