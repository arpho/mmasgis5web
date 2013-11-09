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
 * esegue il task brandList
 * @author giuseppe
 *
 */
public class BrandsListTask extends Task {
	
	/**
	 * interroga il db per costruire l'oggetto da cui verrà prodotto il json da inviare al client per popolare lalistadei marchi per i filtri
	 * @param db: String: censimento selezionato 
	 * @param query: String
	 * @return
	 * @throws ServletException
	 */
	public ArrayList<HashMap<String, String>> queryDb4Brands(String db,String query) throws ServletException
	{
		ArrayList<HashMap<String, String>> brands=new ArrayList<HashMap<String,String>>();
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
					p.put("classificazione", rs.getString("classificazione"));
					p.put("brand_id", rs.getString("id"));
					brands.add(p);
					//out.println("<p>"+rs.getObject(2).toString()+"</p>");
		
			}
			con.close();
			//HashMap<String, Object> data=new HashMap<String, Object>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return brands;
	}
	
	public void brands_list(int pv_id,PrintWriter out, String censimento)
	{
		new Consts();
		String query=String.format(Consts.queryBrands, pv_id);
		ArrayList<HashMap<String, String>> rs = null;
		try
		{
			rs=queryDb4Brands(censimento,query);
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
		PrintWriter out=null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
		response.setContentType("text/html;charset=UTF-8");
    	int pv_id=Integer.parseInt((request.getParameter("id").toString()));
    	brands_list(pv_id,out,censimento);
    	out.close();
	}

}
