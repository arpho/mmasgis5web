package com.webgis.application.query;

import util.Consts;

import com.webgis.application.query.Query;

/**
 * genera la sottoquery del filtro per i criteri marca del filtro
 * @author giuseppe
 *
 */
public class QueryMar extends Query{
/**
* ritorna la lista di marche appartenenti a tc_cl_mar_id 
* @param string id valora di tc_cl_mar_id su cui fare il join
* @param String categoria è un parametro dummy per rendere compatibile il metodo con quello delle altre classi gemelle  
*  */
public String getQuery(String id,String categoria) {
		
		new Consts();
		String query=String.format(Consts.queryValueClassMar,id);

		return query;
	}
}