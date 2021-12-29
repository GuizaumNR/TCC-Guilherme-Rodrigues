package com.gnrstudio.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/music.wav");
	public static final Sound hurtEfecct = new Sound("/hurt.wav");
	public static final Sound shootEfecct = new Sound("/shoot.wav");
	public static final Sound selectEfecct = new Sound("/select.wav");
	public static final Sound deadEfecct = new Sound("/dead.wav");
	
	private Sound(String name) {
		try{
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e){
			
		}
	}
	
	public void play(){
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e) {
			
		}
	}
	
	public void loop(){
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {
			
		}
	}
	
	
}
