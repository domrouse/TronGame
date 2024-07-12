package com.mygdx.trongame;

import com.badlogic.gdx.Game;

public class Orchestrator extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private SetUpScreen setUpScreen;
	private MainScreen_2 mainScreen_2;
	private EndScreen_2 endScreen_2;
	private ControlsScreen controlsScreen;
	private appPreferences preferences;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int SETUP = 2;
	public final static int CONTROLS = 3;
	public final static int APPLICATION2 = 4;
	public final static int ENDGAME2 = 5;



	@Override
	public void create() {
		//set initial screen
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		preferences = new appPreferences();

	}

	// screen changer
	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case SETUP:
				if(setUpScreen == null) setUpScreen = new SetUpScreen(this);
				this.setScreen(setUpScreen);
				break;
			case APPLICATION2:
				if(mainScreen_2 == null) mainScreen_2 = new MainScreen_2(this);
				this.setScreen(mainScreen_2);
				break;
			case CONTROLS:
				if(controlsScreen == null) controlsScreen = new ControlsScreen(this);
				this.setScreen(controlsScreen);
				break;
			case ENDGAME2:
				if(endScreen_2 == null) endScreen_2 = new EndScreen_2(this);
				this.setScreen(endScreen_2);
				break;
		}
	}

	public appPreferences getPreferences() {
		return this.preferences;
	}


}
