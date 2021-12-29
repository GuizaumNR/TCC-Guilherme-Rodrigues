package com.gnrstudio.world;

public class Camera {

	public static int x;
	public static int y;
	
	public static int clamp(int Atual, int Min, int Max) { // LIMITAR A CAMERA DE N MOSTRAR O "NULL"
		if(Atual < Min) {
			Atual = Min;
		}
		if(Atual > Max) {
			Atual = Max;
		}
		return Atual ;
	}
}
