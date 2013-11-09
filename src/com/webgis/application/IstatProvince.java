package com.webgis.application;

import util.Consts;

public class IstatProvince extends IstatBase{

	
	public String getIstatQuery(String code) {
	
		String s = Consts.queryIstatProvince + code;
		
		return s;
	}
		
}