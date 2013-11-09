/**
 * 
 */
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
 * implementa il task getClass, ritorna le classi di parametri disponibili per i filtri
 * @author giuseppe
 *
 */
public class GetClassTask extends Task {
	
	/**
	 * genera l'oggettoche sara' usato per produrre il json per popolare la lista delle classi di parametri disponibili per i filtri
	 * @param query: String query con cui interrogare il db
	 * @param db: String censimento selezionato
	 * @param out:PrintWriter stream di output, 
	 * @return
	 * @throws ServletException
	 */
	public  static ArrayList<HashMap<String, String>> queryDb4Class(String query,String db) throws ServletException
	{

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		new Consts();
		ArrayList<HashMap<String, String>> classes=new ArrayList<HashMap<String,String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,Consts.user,Consts.passwd);
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
					
					HashMap<String, String> p = new HashMap<String, String>();
					p.put("id",rs.getString("id"));
					p.put("text",rs.getString("text"));
					classes.add(p);
			}
			//System.out.println(classes);
		} catch (ClassNotFoundException e) {
		//	out.println("<p> ClassNotfoundException"+e.getMessage()+" </p>");
//			e.printStackTrace();
		} catch (SQLException e) {
		//	out.println("<p> SQLException"+e.getMessage()+" </p>");
			//System.out.println("eccezione sql "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		// displaying records
		
		
		
		  
		return classes;
	
	}
	
	public ArrayList<HashMap<String, String>> getClass(String categoria,String censimento,PrintWriter out)
	{
		new Consts();
		String query=String.format(Consts.queryClass,categoria,categoria, out);
		//query="select * from pv";
		//System.out.println(" query getclass "+query);
		ArrayList<HashMap<String, String>> classes=new ArrayList<HashMap<String,String>>();
		try {
			classes=queryDb4Class(query,censimento);
			
		}/* catch (ServletException e) {
			e.printStackTrace();
		}*/ catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	public void doTask(HttpServletRequest request,HttpServletResponse response)
	{
		String censimento=request.getParameter("censimento");
		String category=request.getParameter("category");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<HashMap<String, String>> r=getClass(category,censimento,out);
		//out.println("<p>rs "+r+"</p>");
		jsonEncode(r,out);
	}

}
