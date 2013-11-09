package com.webgis.application.tasks.paging;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webgis.application.Paging;
import com.webgis.application.QueryBuilder;

import util.Consts;
import util.FindFunction;

public class PagingQueries {
	Boolean filtered;
	Boolean searched;
	HttpServletRequest request;
	Boolean sorted;
	StringBuilder queryBase;
	String queryCount;
	
	
	public void setFiltered(Boolean filtered) {
		this.filtered = filtered;
	}
	public void setSorted(Boolean sorted) {
		this.sorted = sorted;
	}
	public StringBuilder putOrder(StringBuilder query,HttpServletRequest request)
	{
		
		Gson gson = new GsonBuilder().create();
		String sort= request.getParameter("sort");
		Type type=new  TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();
		ArrayList<HashMap<String,String>> sorter=gson.fromJson(sort,type);
		query.append(" order by ");
		query.append(sorter.get(0).get("property"));
		query.append(" ");
		query.append(sorter.get(0).get("direction"));
		return query;
	}
	public String getOrderClause(HttpServletRequest request)
	{
		
		Gson gson = new GsonBuilder().create();
		StringBuilder query= new StringBuilder();
		String clause= getOrderClause(request, true);
		if (request.getParameterMap().containsKey("sort"))
				{
					String sort= request.getParameter("sort");
					Type type=new  TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();
					ArrayList<HashMap<String,String>> sorter=gson.fromJson(sort,type);
					
					query.append(" order by ");
					query.append(sorter.get(0).get("property"));
					query.append(" ");
					query.append(sorter.get(0).get("direction"));
					query.append(' ');
					clause=query.toString();// ritorno la clausola order di default
				}
		return clause;
	}
	
	public String getOrderClause(HttpServletRequest request, Boolean defaultSort)
	{
		
		//Gson gson = new GsonBuilder().create();
		StringBuilder query= new StringBuilder();
		query.append(" order by ");
		query.append("ragione_sociale");
		query.append(" ");
		query.append("ASC");
		query.append(' ');
		return query.toString();
	}
	
	public String getOrderClause(String sorting)
	{
		
		Gson gson = new GsonBuilder().create();
		StringBuilder query= new StringBuilder();
		Type type=new  TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();
		ArrayList<HashMap<String,String>> sorter=gson.fromJson(sorting,type);
		query.append(" order by ");
		query.append(sorter.get(0).get("property"));
		query.append(" ");
		query.append(sorter.get(0).get("direction"));
		query.append(' ');
		return query.toString();
	}
	
	public String makeCountQuery(String q)
	{
		StringBuilder query= new StringBuilder("select count(pv_id)as total  from (");
		query.append(q);
		query.append(") as a");
		return query.toString();
		
	}
	
	public Boolean isFiltered(HttpServletRequest request)
	{
		Boolean b= new Boolean(false);
		if(request.getParameterMap().containsKey("parametri")) {
			if(request.getParameter("parametri").equals("")) {
				System.out.println("***parametri vuoti");
			} else {
				b=true;
			}
		}
		
		if( request.getParameterMap().containsKey("marche")) {
			if(request.getParameter("marche").equals("")) {
				System.out.println("***marche vuote");
			} else {
				b=true;
			}
		}
		if( request.getParameterMap().containsKey("potenziali")) {
			if(request.getParameter("potenziali").equals("")) {
				System.out.println("***potenziali vuoti");
			} else {				
				b=true;
			}
		}
		System.out.println("***filtered? "+b);
		return b;
	}
	

	public Boolean isSorted(HttpServletRequest request)
	{
		Boolean b= new Boolean(false);
		if( request.getParameterMap().containsKey("sort"))
		{
			b=true;
		}
		return b;
	}
	
	
	public PagingQueries(HttpServletRequest request)
	{
		this.request=request;
		this.filtered=new Boolean(isFiltered(request));
		this.queryBase=new StringBuilder(Consts.RefactoredQueryBasePv);
		this.searched= new Boolean(request.getParameterMap().containsKey("search"));
		if ( this.filtered)
		{
			this.queryBase=new StringBuilder(Consts.QueryFilteredPv);
		}
		this.sorted= new Boolean(isSorted(request));
		
	}
	public PagingQueries(String selections,Boolean filtered)
	{
		this.filtered=filtered;
		this.queryBase=new StringBuilder(Consts.RefactoredQueryBasePv);
		if ( this.filtered)
		{
			this.queryBase=new StringBuilder(Consts.QueryFilteredPv);
		}
	}
	
	public class Queries
	{
		String queryList,queryCount,fullQuery;
		public String getFullQuery() {
			return fullQuery;
		}
		public void setFullQuery(String fullQuery) {
			this.fullQuery = fullQuery;
		}
		public String getQueryList() {
			return queryList;
		}
		public void setQueryList(String queryList) {
			this.queryList = queryList;
		}
		public String getQueryCount() {
			return queryCount;
		}
		public void setQueryCount(String queryCount) {
			this.queryCount = queryCount;
		}
		public  Queries(String qList,String qCount)
		{
		this.queryCount=qCount;
		this.queryList=qList;
		}
		public String toString()
		{
			StringBuilder str= new StringBuilder("pqging queries: ");
			str.append("queryList: ");
			str.append(this.queryList);
			str.append("\n");
			str.append("querycount: ");
			str.append(this.queryCount);
			return str.toString();
		}
	}
	public  Queries getQuery()
	{
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(this.queryBase);
		this.queryBase.append(getGeographicClause(this.request));
		if ( this.filtered)
		{
			this.queryBase.append(" and ");
			this.queryBase.append(getFilterClause(this.request));
		}
		if (this.searched)
		{
			FindFunction ff = new FindFunction();
			this.queryBase.append(ff.getClause(this.request));
		}
		this.queryBase.append(" group by pv.pv_id ");
		this.queryBase.append(getOrderClause(this.request));

		//rimuovo i doppioni dovuti al join con rel_pv_pot
		
		Queries q= new Queries(this.queryBase.toString(),makeCountQuery(this.queryBase.toString()));// ancora manca la clausola limit in querybase, posso generare queryCount
		q.setFullQuery(this.queryBase.toString());//conservo la query che ritorna tutto il resultset con la selezione geografica e i filtri
		//aggiungo la clausola limit
		this.queryBase.append(getLimitClause(this.request));
		q.setQueryList(this.queryBase.toString());
		return  q;
	}
	
	public  Queries getQuery(String selections,String parametri,String marche,String potenziali,String order,String start,String limit)
	{
		this.queryBase.append(getGeographicClause(selections));
		if ( this.filtered)
		{
			this.queryBase.append(" and ");
			this.queryBase.append(getFilterClause(parametri,marche,potenziali));
		}
		if (this.sorted)
		{
			this.queryBase.append(getOrderClause(order));
		}
		Queries q= new Queries(this.queryBase.toString(),makeCountQuery(this.queryBase.toString()));// ancora manca la clausola limit in querybase, posso generare queryCount
		//aggiungo la clausola limit
		this.queryBase.append(getLimitClause(start,limit));
		q.setQueryList(this.queryBase.toString());
		return  q;
	}
	
 private Object getGeographicClause(String selections) {
		// TODO Auto-generated method stub
		return null;
	}
public String getLimitClause(HttpServletRequest request) {
		StringBuilder query= new StringBuilder(" limit ");
		query.append(request.getParameter("start"));
		query.append(',');
		query.append(request.getParameter("limit"));
		return query.toString();
		
	}
 
 public String getLimitClause(String limit,String start) {
		StringBuilder query= new StringBuilder(" limit ");
		query.append(start);
		query.append(',');
		query.append(limit);
		return query.toString();
		
	}
	public String getFilterClause(HttpServletRequest request)
	{
		//System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		String clause="";
		QueryBuilder q= new QueryBuilder();
		clause=q.buildFilterSection(request);
		return clause;
	}
	
	public String getFilterClause(String parametri,String marche,String potenziali)
	{
		String clause="";
		QueryBuilder q= new QueryBuilder();
		clause=q.buildFilterSection(parametri,marche,potenziali);
		return clause;
	}
	
	public String getGeographicClause(HttpServletRequest request)
	{
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//String selections= request.getParameter("selections");
		String reg = request.getParameter("reg");
		String pro = request.getParameter("pro");
		String com = request.getParameter("com");
		String cap = request.getParameter("cap");
		System.out.println(reg);
		//estraggo le utb
		Paging p= new Paging();
		ArrayList<String> utb_reg = p.extractUTB(reg);
		ArrayList<String> utb_pro = p.extractUTB(pro);
		ArrayList<String> utb_com = p.extractUTB(com);
		ArrayList<String> utb_cap = p.extractUTB(cap);
		//System.out.println("utb tostring:");
		//System.out.println(utb.toString());
		
		String c= p.getGeographicSection(utb_reg, utb_pro, utb_com, utb_cap);
		return c;
	}
	
	/**
	 * aggiunge le clausole finali(limit e sort) alla query
	 * @param query0: query da completare
	 * @param request
	 * @return String
	 */
	public String completeQuery(String query0,HttpServletRequest request)
	{
		StringBuilder query=new StringBuilder(query0);
			if(request.getParameterMap().containsKey("limit"))
					{
				String limit=request.getParameter("limit");
				String start=request.getParameter("start");
				query.append(" limit ");
				query.append(start);
				query.append(",");
				query.append(limit);
					}
			return query.toString();
		}
	}

