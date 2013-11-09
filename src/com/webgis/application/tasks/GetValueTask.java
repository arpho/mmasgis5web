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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;

import com.webgis.application.query.Query;
import com.webgis.application.query.QueryMar;
import com.webgis.application.query.QueryPar;
import com.webgis.application.query.QueryPot;
/**
 * implementa il task getValue, per il fetching dei valori disponibili per i filtri
 * @author giuseppe
 *
 */
public class GetValueTask extends Task {


	/**
	 * genera l'oggetto da cui verra' prodotto il json per popolare la lista dei valori disponibili per i filtri
	 * @param query: String query con cui interrogare il db 
	 * @param db: censimento selezionato
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> queryDb4Values(String query,String db) 
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList<HashMap<String, String>> values=new ArrayList<HashMap<String,String>>();



		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,Consts.user,Consts.passwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{

				HashMap<String, String> p = new HashMap<String, String>();
				p.put("id", rs.getString("id"));
				p.put("text", rs.getString("text"));
				p.put("class_text", rs.getString("class_text"));
				p.put("class_id", rs.getString("class_id"));
				values.add(p);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return values;
	}

	public ArrayList<HashMap<String, String>> getValues(String categoria,String censimento,PrintWriter out,String id)
	{
		new Consts(); 
		HashMap<String, Query> queries= new HashMap<String, Query>();
		queries.put("par", new QueryPar());
		queries.put("pot", new QueryPot());
		queries.put("mar", new QueryMar());
		/*queries.put("par", c.queryValuesClassPar);
		queries.put("pot", c.queryValuesClassPar);
		queries.put("mar", c.queryValueClassMar);*/
		String query=queries.get(categoria).getQuery(id, categoria);
		//String.format(queries.get(categoria),categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,categoria,id,categoria, out);
		//query="select * from pv";
		//System.out.println(" query getclass "+query);
		ArrayList<HashMap<String, String>> values=new ArrayList<HashMap<String,String>>();

		values=queryDb4Values(query,censimento);

		return values;
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
		String classe_id=request.getParameter("id");
		//out.println("<p>task= "+task+"</p>");
		ArrayList<HashMap<String, String>> r=getValues(category,censimento,out,classe_id);
		jsonEncode(r,out);
	}

}
