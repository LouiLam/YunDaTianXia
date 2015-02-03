package com.tlz.utils;

import android.content.Context;

public class ResIdentifier {
	public  static int getIDByName(Context con,String name) 
	{		
	return con.getResources().getIdentifier(name, "id",con.getPackageName());
	}
	public  static int getDrawbleIDByName(Context con,String name) 
	{		
	return con.getResources().getIdentifier(name, "drawable",con.getPackageName());
	}
	public static  int getRawIDByName(Context con,String name) 
	{		
	return con.getResources().getIdentifier(name, "raw",con.getPackageName());
	}
	public static int getAnimIDByName(Context con,String name) 
	{		
	return con.getResources().getIdentifier(name, "anim",con.getPackageName());
	}
}
