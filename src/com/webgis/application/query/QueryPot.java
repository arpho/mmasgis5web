package com.webgis.application.query;

import util.Consts;



/**
 * genera la sottoquery del filtro per i criteri potenziale del filtro 
 * @author giuseppe
 *
 */
public class QueryPot extends Query{
	@Override
	public String getQuery(String id,String categoria) {
		
		new Consts();
		String query=String.format(Consts.queryValuesClassPar,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,id,categoria);

		return query;
	}
}