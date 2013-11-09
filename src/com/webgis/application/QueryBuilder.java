package com.webgis.application;
import com.webgis.application.ParametersColumns;
import com.webgis.application.Parameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.ArrayList; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ParametersColumns
{	private String classColumn;
	private String parameterColumn;
	public ParametersColumns(String classColumn,String parameterColumn)
	{
		this.classColumn=classColumn;
		this.parameterColumn=parameterColumn;
	}
	public String getClassColumn() {
		return classColumn;
	}
	public void setClassColumn(String classColumn) {
		this.classColumn = classColumn;
	}
	public String getParameterColumn() {
		return parameterColumn;
	}
	public void setParameterColumn(String parameterColumn) {
		this.parameterColumn = parameterColumn;
	}
}




public class QueryBuilder {
	/**
	 * combina le clausole or delle varie classi con le clausole and
	 * @param hasmap ottenuto da orPackager
	 * @return l'insieme della clausola where da aggiungere alla query
	 */
	public String andPackager(HashMap<Integer, String> lor)
	{
	StringBuilder cl= new StringBuilder();
	Iterator<Integer> it = lor.keySet().iterator();
	while (it.hasNext())
	{
		Integer key= it.next();
		cl.append(lor.get(key));
		if (it.hasNext())
		{
			cl.append(" and ");
		}
	}
	return cl.toString();
	}
	
	public HashMap<Integer, ArrayList<String>> makeFilterSectionParameters(String parametri)
	{
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		// estraggo i parametri del filtro
		//ArrayList<String>  step1=fromString2Array(parametri);
		//genero i parametri
		HashMap<Integer, ArrayList<String>> step2=generateParameters(parametri, "parametri");
		return step2;
	}
	
	public HashMap<Integer, ArrayList<String>> makeFilterSectionBrands(String parametri)
	{
		// estraggo i parametri del filtro
		//ArrayList<String>  step1=fromString2Array(parametri);
		//genero i parametri
		HashMap<Integer, ArrayList<String>> step2=generateParameters(parametri, "marche");
		return step2;
	}
	
	public HashMap<Integer, ArrayList<String>> makeFilterSectionPotentials(String parametri)
	{
		// estraggo i parametri del filtro
		//ArrayList<String>  step1=fromString2Array(parametri);
		//genero i parametri
		HashMap<Integer, ArrayList<String>> step2=generateParameters(parametri, "potenziali");
		return step2;
	}
	
	public String combinesectionParameters(HashMap<Integer, ArrayList<String>> l)
	{
		
		Iterator<Integer> itr= l.keySet().iterator();
		StringBuilder wereparameter= new StringBuilder("(");
		while(itr.hasNext())
		{
			Integer i=itr.next();
			Iterator<String> it= l.get(i).iterator();
			while (it.hasNext())
			{
				String p=it.next();
				wereparameter.append(p);
				if (it.hasNext())
				{
					wereparameter.append(" or ");
				}
			}
			wereparameter.append(')');
			if (itr.hasNext())
			{
				wereparameter.append("and");
			}
		}
		return wereparameter.toString();
	}
	
	public String buildFilterSection(String parametri,String potenziali,String marche)
	{
	
		HashMap<Integer, ArrayList<String>> para = makeFilterSectionParameters(parametri);
		HashMap<Integer, ArrayList<String>> brands = makeFilterSectionBrands(marche);
		HashMap<Integer, ArrayList<String>> pot = makeFilterSectionPotentials(potenziali);
		StringBuilder section= new StringBuilder();
		section.append(combinesectionParameters(para));
		section.append(" and ");
		section.append(combinesectionParameters(brands));
		section.append(" and ");
		section.append(combinesectionParameters(pot));
		return section.toString();
		
	}
	
	public String buildFilterSection(HttpServletRequest request,HttpServletResponse response)
	{
		HashMap<Integer, ArrayList<String>> para = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> brands = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> pot = new HashMap<Integer, ArrayList<String>>();
		ArrayList<HashMap<Integer, ArrayList<String>>> filtri= new ArrayList<HashMap<Integer,ArrayList<String>>>();
	
		if (request.getParameterMap().containsKey("parametri"))
		{
				para = makeFilterSectionParameters(request.getParameter("parametri"));
				filtri.add(para);
		}
		if(request.getParameterMap().containsKey("marche"))
		{
			brands = makeFilterSectionBrands(request.getParameter("marche"));
			filtri.add(brands);
		}
		
		if (request.getParameterMap().containsKey("potenziali"))
		{
			 pot = makeFilterSectionPotentials(request.getParameter("potenziali"));
			 filtri.add(pot);
		}
		
		StringBuilder section= new StringBuilder();
		/*section.append(combinesectionParameters(para));
		
		section.append(" and ");
		section.append(combinesectionParameters(brands));
		section.append(" and ");
		section.append(combinesectionParameters(pot));*/
		Iterator<HashMap<Integer, ArrayList<String>>> itr= filtri.iterator();
		while (itr.hasNext())
		{
			HashMap<Integer, ArrayList<String>> f = itr.next();
			section.append(combinesectionParameters(f));
			if (itr.hasNext())
			{
				section.append(" and ");
			}
		}
		
		return section.toString();
		
	}
	
	public String buildFilterSection(HttpServletRequest request)
	{
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		HashMap<Integer, ArrayList<String>> para = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> brands = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> pot = new HashMap<Integer, ArrayList<String>>();
		ArrayList<HashMap<Integer, ArrayList<String>>> filtri= new ArrayList<HashMap<Integer,ArrayList<String>>>();
	
		if (request.getParameterMap().containsKey("parametri") && !request.getParameter("parametri").equals(""))
		{
				para = makeFilterSectionParameters(request.getParameter("parametri"));
				filtri.add(para);
		}
		if(request.getParameterMap().containsKey("marche") && !request.getParameter("marche").equals(""))
		{
			brands = makeFilterSectionBrands(request.getParameter("marche"));
			filtri.add(brands);
		}
		
		if (request.getParameterMap().containsKey("potenziali") && !request.getParameter("potenziali").equals(""))
		{
			 pot = makeFilterSectionPotentials(request.getParameter("potenziali"));
			 filtri.add(pot);
		}
		
		StringBuilder section= new StringBuilder();
		/*section.append(combinesectionParameters(para));
		
		section.append(" and ");
		section.append(combinesectionParameters(brands));
		section.append(" and ");
		section.append(combinesectionParameters(pot));*/
		Iterator<HashMap<Integer, ArrayList<String>>> itr= filtri.iterator();
		while (itr.hasNext())
		{
			HashMap<Integer, ArrayList<String>> f = itr.next();
			section.append(combinesectionParameters(f));
			if (itr.hasNext())
			{
				section.append(" and ");
			}
		}
		
		return section.toString();
		
	}
	
	public HashMap<Integer, ArrayList<String>> generateParameters(String request,String tab)
	{
		ArrayList<String>  step1=fromString2Array(request);
		//genero i parametri
		ArrayList<Parameter> step2=fromString2ParameterArray(step1, tab);
		HashMap<Integer, ArrayList<Integer>> gl=groupList(step2);
		HashMap<Integer, ArrayList<String>> lp=coupleMaker(gl, tab);
		return lp;
	}
	/**
	 * effettua il primo step del processo di parsing del descrittore del filtro  prodotto dal front-end in javascript
	 * decompone la stringa nelle sottostringhe che descrivono ogni singolo parametro
	 * @param filterParameters stringa nel formato class_id,parameter_id|class_id,parameter_id 
	 * @return lo ArrayList delle sottostringhe
	 */
	public ArrayList<String> fromString2Array(String filterParameters)
	{
		ArrayList<String> parameters= new  ArrayList<String>();
		StringTokenizer stk= new StringTokenizer(filterParameters,"|");
		while (stk.hasMoreTokens())
		{String e= stk.nextToken();
		parameters.add(e);
		}
		return parameters;
	}
	/**
	 * esegue il secondo step del processo di parsing del descrittore del filtro  prodotto dal front-end in javascript
	 * converte la lista di Stringhe  ottenuta al primo step in una lista di Parameter,cioè il parametro di input per buildQuery
	 * @param paramString
	 * @param key categoria del filtro <"parametri","potenziali","marche">
	 * @return lista di Parameter
	 */
	public ArrayList<Parameter> fromString2ParameterArray(ArrayList<String> paramString,String key)
	{
		ArrayList<Parameter> parameters= new ArrayList<Parameter>();
		Iterator<String> it= paramString.iterator();
		while (it.hasNext())
		{
			String p = it.next();
			StringTokenizer tk= new StringTokenizer(p,",");
			String class_id=tk.nextToken();
			String parameter_id= tk.nextToken();
			Parameter par= new Parameter(key, Integer.parseInt(class_id), Integer.parseInt(parameter_id));
			parameters.add(par);
		}
		return parameters;
	}
	/**
	 * genera le clausole select che dovranno essere combinate con lo statement union 
	 * @param coupleBase
	 * @return
	 */
	public String subSelect(String coupleBase,String key)
	{
		HashMap<String, String> keyAdapter= new HashMap<String, String>();
		keyAdapter.put("parametri","par");
		keyAdapter.put("potenziali", "pot");
		keyAdapter.put("marche", "mar");
		String head="SELECT pv_id FROM rel_pv_"+keyAdapter.get(key)+" r where ";
		StringBuilder select=new StringBuilder(head);
		select.append(coupleBase);
		
		return select.toString();
	}
	/**
	 * produce la query del tab unendo le subQueries tramite la clausola union 
	 * @param list
	 * @param n numero di classi diverse presenti nel filtro
	 * @return String
	 */
	public String queryTabFactory(ArrayList<String> list,int n)
	{
		StringBuilder querytab=new StringBuilder("select pv_id from (");
		Iterator<String> it= list.iterator();
		while (it.hasNext())
		{
			String q= it.next();
			querytab.append(q);
			if(it.hasNext())
				querytab.append(" union all ");
		}
		querytab.append(") as o  GROUP BY o.pv_id HAVING COUNT(*) >=");
		querytab.append(Integer.toString(n));
		return querytab.toString();
	}
	
	
	
	public String buildTabQuery(ArrayList<Parameter> parametersList,String key)
	{
		HashMap<Integer, ArrayList<Integer>> gl=groupList(parametersList);
		HashMap<Integer, ArrayList<String>> lp=coupleMaker(gl, key);
		Iterator<Integer> i= lp.keySet().iterator();
		ArrayList<String> subSelect= new ArrayList<String>();
		while (i.hasNext())
		{
			ArrayList<String> c=lp.get(i.next());
			Iterator<String> it=c.iterator();
			while (it.hasNext())
			{
				String item=it.next();
				subSelect.add(subSelect(item,key));
				//System.out.println(subSelect(item,key));
			}
		}
		// ora ho la lista dei subselect da concatenare con union
		String select=queryTabFactory(subSelect, lp.size());
		return select;
				
	}
	public String buildTabQueryOld(ArrayList<Parameter> parametersList,String key)
	{
		HashMap<Integer, ArrayList<Integer>> gl=groupList(parametersList);
		HashMap<Integer, ArrayList<String>> lp=coupleMaker(gl, key);
		HashMap<Integer, String> op=orPackager(lp);
		String andWhere= andPackager(op);
		return selectFactory(key, andWhere);
	}
	/**
	 * combina le query sui singoli tab per i filtri in un unica query tramite union all
	 * @param tabQueries
	 * @return query String
	 */
	public String QueryUnifier(ArrayList<String> tabQueries)
	{
		StringBuilder query=new StringBuilder("select pv_id from (");
		/*queryHead="select * from ("
		queryTail=")as tb1 GROUP BY tb1.pv_id HAVING COUNT(*) = %d"%len(d)
		query=""
		for s in d:
			query=query+s+" union all"
		#rimuovo l'ultimo  union all
		query=query[0:len(query)-9]
		query= queryHead+query+queryTail*/
		String queryTail=String.format(")as tb1 GROUP BY tb1.pv_id HAVING COUNT(*) = %d", tabQueries.size());
		Iterator<String> it= tabQueries.iterator();
		while (it.hasNext())
		{
			String select=it.next();
			query.append(select);
			if (it.hasNext())
				query.append(" union all ");
		}
		query.append(queryTail);
		return query.toString();
	}
	/**
	 * combina le query sui singoli tab per i filtri in un unica query tramite union all
	 * versione modificata per il paging
	 * @param tabQueries
	 * @return query String
	 */
	public String QueryUnifierPaging(ArrayList<String> tabQueries)
	{
		StringBuilder query=new StringBuilder("select pv_id as id from (");
		/*queryHead="select * from ("
		queryTail=")as tb1 GROUP BY tb1.pv_id HAVING COUNT(*) = %d"%len(d)
		query=""
		for s in d:
			query=query+s+" union all"
		#rimuovo l'ultimo  union all
		query=query[0:len(query)-9]
		query= queryHead+query+queryTail*/
		String queryTail=String.format(")as tb1 GROUP BY tb1.pv_id HAVING COUNT(*) = %d", tabQueries.size());
		Iterator<String> it= tabQueries.iterator();
		while (it.hasNext())
		{
			String select=it.next();
			query.append(select);
			if (it.hasNext())
				query.append(" union all ");
		}
		query.append(queryTail);
		return query.toString();
	}
	
	/**
	 * henera le "coppie and"  (tc_clkey_id=% and tc_key_id=%)
	 * @param gl e' un dizionario con chiave class_id e valore la lista di ogni parametro id appartenente a class_id
	 * @param key
	 * @return {class_id=[(tc_clmar_id=10 and tc_mar_id=1), (tc_clmar_id=10 and tc_mar_id=2)]}
	 */
	public HashMap<Integer, ArrayList<String>> coupleMaker(HashMap<Integer, ArrayList<Integer>> gl,String key)
	{
		//definisco i nomi delle colonne usate nelle queries
		HashMap<String, ParametersColumns> col= new HashMap<String, ParametersColumns>();
		ParametersColumns parametri= new ParametersColumns("tc_clpar_id", "tc_par_id");
		col.put("parametri", parametri);
		ParametersColumns marchi= new ParametersColumns("tc_clmar_id", "tc_mar_id");
		col.put("marche", marchi);
		ParametersColumns potenziali= new ParametersColumns("tc_clpot_id", "tc_pot_id");
		col.put("potenziali", potenziali);
		HashMap<Integer, ArrayList<String>> cm=new HashMap<Integer, ArrayList<String>>();
		

		Iterator<Integer> it=gl.keySet().iterator();
		while( it.hasNext())
		{
			Integer k=it.next();
			Iterator<?> itr=gl.get(k).iterator();
			//devo iterare lungo gli elementi dello ArrayList con chiave k
			ArrayList<String> coupleList=new ArrayList<String>();
			while(itr.hasNext())
			{int parameter_id=(Integer) itr.next();
				String couple="(%s=%d and %s=%d)";
				String andCouple=String.format(couple, col.get(key).getClassColumn(),k.intValue(),col.get(key).getParameterColumn(),parameter_id);
				
				coupleList.add(andCouple);
				cm.put(k.intValue(), coupleList);
				
			}
		}
		return cm;
	}
	/**
	 * genera i gruppi di clausole or 
	 * @param dizionario delle classi usate come parametro del filtro il valore dello hashmap e' lo ArrayList delle coppie and class_id e parameter_id
	 * @return Hashmap, ogniclass_id con la stringa composta dalle sue coppie class_id e parameter_id in or
	 */
	public HashMap<Integer, String> orPackager(HashMap<Integer, ArrayList<String>> lp)
	{
		HashMap<Integer, String> dictOut=new HashMap<Integer, String>();
		Iterator<Integer> it=lp.keySet().iterator();
		//itero lungole chiavi di lp che sono le class_id
		while (it.hasNext())
		{
			Integer k= it.next();
			//se la key non e' ancora stata aggiunta a dictOut devo costruire la clausola or
			if (!dictOut.containsKey(k))
				{
					StringBuilder str=new  StringBuilder("( ");
					//itero lungo gli elementi dello array che ha chiave k
					Iterator<?> itr=lp.get(k).iterator();
					while (itr.hasNext())
					{
						String cl=(String) itr.next();
						str.append(cl);
						if (itr.hasNext())
							str.append(" or ");
					}
					str.append(" )");
					dictOut.put(k, str.toString());
					
				}
			
			
		}
		return dictOut;
	}
	public String selectFactory(String key,String where)
	{
		
		HashMap<String, StringBuilder> Queries=new HashMap<String, StringBuilder>();
		Queries.put("parametri",new StringBuilder("SELECT pv_id FROM rel_pv_par r  where "));
		Queries.put("marchi", new StringBuilder("SELECT pv_id FROM rel_pv_mar where "));
		Queries.put("potenziali",new StringBuilder("SELECT pv_id FROM rel_pv_pot r  where "));
		
		StringBuilder select= Queries.get(key);
		select.append(where);
		/*
		 * query= self.Query[key]
				l=[]
				for o in d.itervalues():
					l.append("( %s)"%(query+o))
				return l*/
		return select.toString();
	}
	/**
	 * genera i gruppi di parametri per ogni class_id
	 * @param parametersList
	 * @return HashMap<Integer, ArrayList<Integer>>
	 */
	public HashMap<Integer, ArrayList<Integer>> groupList(ArrayList<Parameter> parametersList)
	{

		 HashMap<Integer, ArrayList<Integer>>groups=new HashMap<Integer, ArrayList<Integer>>();
		 Iterator<Parameter> itr=parametersList.iterator();
		 while (itr.hasNext())
		 {Parameter p=itr.next();
			 if (!groups.containsKey(p.getClass_id()))
				 {
				 	
					 ArrayList<Integer>pList= new ArrayList<Integer>();
					 pList.add(p.getParameter_id());
					 groups.put(p.getClass_id(), pList);
				 }
			 else
			 {
				 ArrayList<Integer>pList=groups.get(p.getClass_id());
				 pList.add(p.getParameter_id());
			 }
			
		 }
		 return groups;
	}

}
