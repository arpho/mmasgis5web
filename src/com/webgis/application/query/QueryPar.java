package com.webgis.application.query;

import util.Consts;


/**
 * genera la sottoquery del filtro per i  criteri parametri del filtro
 * @author giuseppe
 *
 */
public class QueryPar extends Query{

	@Override
	public String getQuery(String id,String categoria) {
		
		new Consts();
		String query=String.format(Consts.queryValuesClassPar,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,id,categoria);

		return query;
	}
}