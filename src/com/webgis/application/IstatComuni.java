package com.webgis.application;

import util.Consts;

public class IstatComuni extends IstatBase {

		
	public String getIstatQuery(String code) {
		
		String s = Consts.queryIstatComuni + code;
		
		return s;
	}
			
}
