package com.webgis.application.tasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;
/**
 * implementa il task brandsValueList, ritornando la lista dei marchi trattati da un pv
 * @author giuseppe
 *
 */
public class BrandsValueListTask  extends Task{
	
	/**
	 * prepara l'oggetto da cui generare il json per popolare la lista dei marchi nella tabella anagrafica
	 * @param db: String: censimento selezionato
	 * @param query:String: query concuiinterrogare il db
	 * @return
	 * @throws ServletException
	 */
	public ArrayList<HashMap<String, String>> queryDb4BrandsValues(String db,String query) throws ServletException
	{
		ArrayList<HashMap<String, String>> brandsValues=new ArrayList<HashMap<String,String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		new Consts();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,Consts.user,Consts.passwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("valore", rs.getString("valore"));
				//System.out.println("***valore: "+rs.getString("valore"));
				brandsValues.add(p);
				//out.println("<p>"+rs.getObject(2).toString()+"</p>");
			}
			if (!rs.first()) {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("valore", "-");
				brandsValues.add(p);
			}
			
			con.close();
			//HashMap<String, Object> data=new HashMap<String, Object>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return brandsValues;
	}
	
	/**
	 *  produce  e invia il json al client, per popolare la lista dei brands trattati, per popolare la tabella marchi della scheda anagrafica 
	 * @param pv_id
	 * @param brand_id
	 * @param out
	 * @param censimento
	 */
	public void brands_values_list(int pv_id,int brand_id,PrintWriter out,String censimento)
	{
		new Consts();
		String query=String.format(Consts.queryBrandsValues, pv_id,brand_id);
		ArrayList<HashMap<String, String>> rs = null;
		try
		{
			rs=queryDb4BrandsValues(censimento,query);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		jsonEncode(rs,out);
	}
	public void doTask(HttpServletRequest request,HttpServletResponse response)
	{
		String censimento=request.getParameter("censimento");
		response.setContentType("application/json");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	int pv_id=Integer.parseInt((request.getParameter("id").toString()));
     	int brand_id=Integer.parseInt((request.getParameter("brand_id").toString()));
     	brands_values_list(pv_id,brand_id,out,censimento);
     	out.close();
	}

}
