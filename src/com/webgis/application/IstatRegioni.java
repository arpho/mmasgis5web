package com.webgis.application;

import util.Consts;

public class IstatRegioni extends IstatBase{

	
	public String getIstatQuery(String code) {
	
		String s = Consts.queryIstatRegioni + code;
		
		return s;
	}
		
}
