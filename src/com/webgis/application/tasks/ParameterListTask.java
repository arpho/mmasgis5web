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
 * implementa il task per il fetching dei parametri di un pv
 * @author giuseppe
 *
 */
public class ParameterListTask extends Task {


	/**
	 *  interroga il db per ottenere la lista di parametri del pv, produce  e invia il json al client
	 * @param pv_id:int pv_id
	 * @param out: PrintWriter output stream
	 */
	public void parameters_list(int pv_id,PrintWriter out,String censimento)
	{
		new Consts();
		String query=Consts.queryParameters;
		String queryClass = Consts.queryClassParameters;
		
		ArrayList<HashMap<String, String>> rs = null;
		try
		{
			rs=queryDb4Parameters(out,censimento,query, pv_id, queryClass);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		jsonEncode(rs,out);
        //out.println("<p> json: "+json+"</p>");
        
        
		
	}
	/**
	 * interroga il censimento per ottenere i parametri del pv e ne ritorna il resultset 
	 * @param out:text-output stream PrintWriter
	 * @param db:string nome censimento
	 * @param query:String query da eseguire
	 * @return ArrayList<HashMap<String, String>> : risultato query pronto per jsonEncode4PvList
	 * @throws ServletException
	 */
	public ArrayList<HashMap<String, String>> queryDb4Parameters(PrintWriter out,String db,String query, int pv_id, String queryClass) throws ServletException
	{
		ArrayList<HashMap<String, String>> parameters=new ArrayList<HashMap<String,String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		new Consts();
		
		Statement stmtA = null;
		ResultSet rsA = null;
		
		Statement stmtB = null;
		ResultSet rsB = null;
		
		
		try
		{
			//SELECT IF( rpp.tc_clpot_id =1, rpp.valore,  'valore non disponibile' ) AS potenziale,cod_cliente, pv.pv_id, cod_mmas, nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1 AS telefono, cf_pi AS codice_fiscale,nome2 as titolare,fax,cliente FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id = pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id where ";

			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,Consts.user,Consts.passwd);
			
			
			stmtA = con.createStatement();
			rsA = stmtA.executeQuery(queryClass);
			
			while(rsA.next()) {
				
				HashMap<String, String> p = new HashMap<String,String>();
				p.put("parametro", rsA.getString("parametro"));
				//System.out.println("***parametro: " + rsA.getString("parametro"));
				//System.out.println("***id_parametro: " + rsA.getString("id_parametro"));
					
				stmtB = con.createStatement();
				rsB = stmtB.executeQuery(String.format(query, rsA.getString("id_parametro"), pv_id));
				
				if (rsB.next()) {
					p.put("valore", rsB.getString("valore"));
					//System.out.println("***valore" + rsB.getString("valore"));
				} else {
					p.put("valore", "-");
				}
				parameters.add(p);
			}
			
			
			/*
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			// displaying records
			while(rs.next())
			{
					HashMap<String, String> p = new HashMap<String, String>();
					//stmt = con.createStatement();
					p.put("parametro", rs.getString("parametro"));
					p.put("valore",rs.getString("valore"));
					parameters.add(p);
					//out.println("<p>"+rs.getObject(2).toString()+"</p>");
		
			}
			*/
			con.close();
			//ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> data=new HashMap<String, Object>();
			//data.put("success",true );
			data.put("result", parameters);
			//gson.toJson(data, out);
			
			  }catch (SQLException e) {
				  throw new ServletException("Servlet Could not display records.", e);
			  } catch (ClassNotFoundException e) {
			  throw new ServletException("JDBC Driver  not found.", e);
			  } finally {
			  try {
			  if(rs != null) {
			  rs.close();
			  rs = null;
			  }
			  if(stmt != null) {
			  stmt.close();
			  stmt = null;
			  }
			  if(con != null)
			  {
				  con.close();
				  con = null;
			  }
			  } catch (SQLException e) {}
		} //eof try
		return parameters;
	}
	
	public void doTask(HttpServletRequest request,HttpServletResponse response)
	{
		response.setContentType("application/json");
		String censimento=request.getParameter("censimento");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pv_id=Integer.parseInt((request.getParameter("id").toString()));
    	parameters_list(pv_id,out,censimento); 
    	out.close();
	}


}
