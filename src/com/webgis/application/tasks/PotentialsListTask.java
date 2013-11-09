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
 * implementa il task per il fetching dei potenziali di un  pv
 * @author giuseppe
 *
 */
public class PotentialsListTask extends Task {
	
	/**
	 * interroga il censimento per ottenere i parametri del pv e ne ritorna il resultset 
	 * @param out:text-output stream PrintWriter
	 * @param db:string nome censimento
	 * @param query:String query da eseguire
	 * @return ArrayList<HashMap<String, String>> : risultato query pronto per jsonEncode4PvList
	 * @throws ServletException
	 */
	public ArrayList<HashMap<String, String>> queryDb4Potentials(PrintWriter out,String db,String query) throws ServletException
	{
		ArrayList<HashMap<String, String>> potentials=new ArrayList<HashMap<String,String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			//SELECT IF( rpp.tc_clpot_id =1, rpp.valore,  'valore non disponibile' ) AS potenziale,cod_cliente, pv.pv_id, cod_mmas, nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1 AS telefono, cf_pi AS codice_fiscale,nome2 as titolare,fax,cliente FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id = pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id where ";

			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,Consts.user,Consts.passwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			//out.println("<p> len rs "+rs.getFetchSize()+" </p>");
			
			// displaying records
			while(rs.next())
			{
					HashMap<String, String> p = new HashMap<String, String>();
					p.put("potenziale", rs.getString("potenziale"));
					p.put("fascia",rs.getString("fascia"));
					p.put("valore",rs.getString("valore"));
					potentials.add(p);
				//  out.println("<p>"+rs.getObject(2).toString()+"</p>");
		
			}
			con.close();
			//ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> data=new HashMap<String, Object>();
			//data.put("success",true );
			data.put("result", potentials);
			//gson.toJson(data, out);
			
			  }catch (SQLException e) {
				  out.println("<p> 4potentialsServlet Could not display records </p>");
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
		return potentials;
	}
	
	/**
	 *  interroga il db per ottenere la lista di potenziali del pv
	 * @param pv_id:int pv_id
	 * @param out: PrintWriter output stream
	 */
	public void potentials_list(int pv_id,PrintWriter out,String censimento)
	{
		new Consts();
		//inserisco lo id nella clausola where della query
		String query=String.format(Consts.queryPotentials, pv_id);
		ArrayList<HashMap<String, String>> rs = null;
		try
		{
			rs=queryDb4Potentials(out,censimento,query);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		jsonEncode(rs,out);
        //out.println("<p> json: "+json+"</p>");
        
//        out.close();
		
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
    	potentials_list(pv_id,out,censimento);
    	out.close();
	}

}
