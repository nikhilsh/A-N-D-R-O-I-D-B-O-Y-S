package com.androidboys.spellarena.game.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.sun.xml.internal.ws.resources.ManagementMessages;
import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.game.android.MultiPlayerHelper.MultiPlayerUi;
import com.androidboys.spellarena.ggps.ActionResolver;
import com.androidboys.spellarena.ggps.IGoogleServices;

public class AndroidLauncher extends AndroidApplication implements MultiPlayerUi, ActionResolver {

	private static final String TAG = "AndroidLauncher";

	private static final int RC_SIGN_IN = 9001;
	
	public MultiPlayerHelper multiPlayerHelper;

	private ArrayList<Participant> participantsList;

	private String myId;
	
	private SpellArena game;

	private int numReadyParticpants;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(multiPlayerHelper == null){
			multiPlayerHelper = new MultiPlayerHelper(this);
			multiPlayerHelper.onCreate();
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game = new SpellArena(this), config);
	}

	@Override
	public void onStart(){
		Log.d(TAG,"onStart");
		super.onStart();
		multiPlayerHelper.onStart(this, this);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		multiPlayerHelper.onStop();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		multiPlayerHelper.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case RC_SIGN_IN:
				GoogleApiClient gApiClient = multiPlayerHelper.getGameHelper().getApiClient();
				Log.d(TAG,"onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + resultCode + ", intent=" + data);
				if (resultCode == RESULT_OK){
					gApiClient.connect();
				} else {
					GameHelper.showFailureDialog(this, resultCode, R.string.gamehelper_sign_in_failed);
				}
				break;
		}
	}

	//Multiplayer Ui implementation
	
	@Override
	public void showScreen(int screen) {
	}

	@Override
	public void onUpdateReceived(Participant participant, byte[] data) {
		manageMsg(participant,data);
	}

	private void manageMsg(Participant participant, byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParticipantsChanged(ArrayList<Participant> participants) {
		Log.d(TAG, "onParticipantsChanged: "+participants.size());
		participantsList = participants;
	}

	@Override
	public void onSignInStatusChanged(boolean isSignedIn) {
		Log.d(TAG,"onSignInStatusChanged: "+isSignedIn);
		game.onSignedIn(isSignedIn);
	}

	@Override
	public void onGameReady() {
		myId = multiPlayerHelper.getUserId();
	}

	@Override
	public void removePlayers(List<String> peersWhoLeft) {
		// TODO Auto-generated method stub
		
	}

	//ActionResolver implementation
	
	@Override
	public void showToast(final CharSequence text) {
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        Log.v(TAG, "text=" + text);
	}

	@Override
	public void broadcastMsg(byte[] msg) {
		multiPlayerHelper.sendUpdate(msg, false);
	}

	@Override
	public void onGameEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeAsReady() {
		numReadyParticpants++;
		checkIfReady();
	}

	private void checkIfReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signIn() {
		if(!multiPlayerHelper.isSignedIn()){
			multiPlayerHelper.signIn();
		}
	}
}
