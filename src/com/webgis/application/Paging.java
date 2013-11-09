package com.webgis.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Paging {

	HashMap<String, String> headQuery;
	String limit, start;
	
	
	/**
	 * gestisce la richiesta di paging costruendo la query da inviare a pv_list
	 */
	public Paging() {
		this.headQuery = new HashMap<String, String>();
	}

	
	/**
	 * esegue il parsing delle selezioni geografiche fatte dal client, generando
	 * la lista delle utb
	 * 
	 * @param selections
	 * @note il parametro selections ricevuto dal client è nel formato
	 *       utb1|utb2|..
	 * @return ArrayList<String>
	 */
	
	public ArrayList<String> extractUTB(String selections) {
		//System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<String> utbList = new ArrayList<String>();
		StringTokenizer st2 = new StringTokenizer(selections, ",");
		while (st2.hasMoreElements()) {
			utbList.add(st2.nextElement().toString());
		}

		return utbList;
	}

	/**
	 * costruisce la query sulla base delle utbs presenti nell'arrayList
	 * ritorna solo i pv_id, usata in paging
	 * @param utb_cap 
	 * @param utb_com 
	 * @param utb_pro 
	 * @param Arraylist<String> lista delle utb selezionate nella forma 'layer,nome'::=<'comune,nome','regione,nome','province,nome'>
	 * @return String:query completa relativa ai soli pv_id
	 **/
	public String getGeographicSection(ArrayList<String> utb_reg, ArrayList<String> utb_pro, ArrayList<String> utb_com, ArrayList<String> utb_cap) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
	
		HashMap<String, IstatBase> taskActions = new HashMap<String, IstatBase>();
		taskActions.put("REG", new IstatRegioni());
		taskActions.put("PRO", new IstatProvince());
		taskActions.put("COM", new IstatComuni());
		//taskActions.put("CAP", new IstatCap());
	
		//System.out.println("sono in getgeographicsection: ");
		//System.out.println(utbs.get(0));
		boolean b = false;
		boolean a = false;
		StringBuilder query = new StringBuilder("");
		StringBuilder subQuery = new StringBuilder("");
		StringBuilder subQueryCap = new StringBuilder("");
		Iterator<String> itr_reg = utb_reg.iterator();
		Iterator<String> itr_pro = utb_pro.iterator();
		Iterator<String> itr_com = utb_com.iterator();
		Iterator<String> itr_cap = utb_cap.iterator();
		
		while (itr_reg.hasNext()) {
			b = true;
			String utb = itr_reg.next().toString();
			String utb_query = taskActions.get("REG").getIstatQuery(utb);
			subQuery.append(utb_query);
			//if (itr_reg.hasNext()) {
			subQuery.append(" UNION ");
			//}
		}
		
		while (itr_pro.hasNext()) {
			b = true;
			String utb = itr_pro.next().toString();
			
			String utb_query = taskActions.get("PRO").getIstatQuery(utb);
			subQuery.append(utb_query);
			//if (itr_pro.hasNext()) {
			subQuery.append(" UNION ");
			//}
		}
		
		while (itr_com.hasNext()) {
			b = true;
			String utb = itr_com.next().toString();
			String utb_query = taskActions.get("COM").getIstatQuery(utb);
			subQuery.append(utb_query);
			//if (itr_com.hasNext()) {
			subQuery.append(" UNION ");
			//}
		}
		
		while(itr_cap.hasNext()) {
			a = true;
			subQueryCap.append("cap = ");
			subQueryCap.append(itr_cap.next().toString());
			if (itr_cap.hasNext()) {
				subQueryCap.append(" OR ");
			}
		}
			

		query.append("(");
		if(b) {
			query.append("tc_istat_id IN (");
			query.append(subQuery.toString().substring(0, subQuery.toString().length()-6));
			query.append(")");	
		}
		if (a && b) {
			query.append(" OR ( ");
			query.append(subQueryCap);
			query.append(")");
		} 
		else if(a){
			query.append("(");
			query.append(subQueryCap);
			query.append(")");
		}

		query.append(")");		
	
		return query.toString();
	}

	/**
	 * ritorna la lista delle queries inserite nella richiesta paging, queste
	 * sono: pv_list,filter,query per il cerca
	 * 
	 * @return
	 */
	public HashMap<String, String> getHeadQuery() {
		return headQuery;
	}
	
	public String getLimit() {
		return limit;
	}

	public String getStart() {
		return start;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public void setStart(String start) {
		this.start = start;
	}

}
