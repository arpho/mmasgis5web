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
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;
import util.PagingData;

import com.google.gson.GsonBuilder;
import com.webgis.application.Parameter;
import com.webgis.application.QueryBuilder;
import com.webgis.application.tasks.paging.PagingQueries;
import com.webgis.application.tasks.paging.PagingQueries.Queries;

public class RefactoredPagingTask extends Task {

	/**
	 * 
	 * @param out
	 *            :text-output stream PrintWriter
	 * @param db
	 *            :string nome censimento
	 * @param query
	 *            :String query da eseguire
	 * @return ArrayList<HashMap<String, String>> : risultato query pronto per
	 *         jsonEncode4PvList
	 * @throws ServletException
	 */
	public PagingData queryDb4Pv(PrintWriter out, String db, String query,
		String queryCount) throws ServletException {
		/////////DEBUG
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(query);
		//////////////
		new GsonBuilder().create();
		ArrayList<HashMap<String, String>> pvList = new ArrayList<HashMap<String, String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		new Consts();
		PagingData r;
		try {
			// SELECT IF( rpp.tc_clpot_id =1, rpp.valore, 'valore non
			// disponibile' ) AS potenziale,cod_cliente, pv.pv_id, cod_mmas,
			// nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1
			// AS telefono, cf_pi AS codice_fiscale,nome2 as
			// titolare,fax,cliente FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id =
			// pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id
			// where ";

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, Consts.user, Consts.passwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			// displaying records
			while (rs.next()) {
				HashMap<String, String> pv = new HashMap<String, String>();
				pv.put("potenziale", rs.getString(1));
				pv.put("cod_cliente", rs.getString("cod_cliente"));
				pv.put("pv_id", Integer.toString(rs.getInt("pv_id")));
				pv.put("cod_mmas", rs.getString(4));
				pv.put("ragione_sociale", rs.getString(5));
				pv.put("indirizzo", rs.getString("indirizzo"));
				pv.put("comune", rs.getString("comune"));
				pv.put("cap", rs.getString("cap"));
				pv.put("provincia", rs.getString("provincia"));
				pv.put("telefono", rs.getString("telefono"));
				pv.put("codice_fiscale", rs.getString("codice_fiscale"));
				pv.put("fax", rs.getString("fax"));
				pv.put("titolare", rs.getString("titolare"));
				pv.put("cliente", Integer.toString(rs.getInt("cliente")));

				pvList.add(pv);
				// out.println("<p>"+rs.getObject(2).toString()+"</p>");

			}
			// calcolo totalProperty
			ResultSet total = stmt.executeQuery(queryCount);
			total.next();// apro il resultset
			int totalCount = total.getInt("total");
			con.close();
			// ArrayList<HashMap<String, Object>> result = new
			// ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> data = new HashMap<String, Object>();
			// data.put("success",true );
			data.put("result", pvList);
			data.put("total", totalCount);
			r = new PagingData(pvList, totalCount);
			// gson.toJson(data, out);

		} catch (SQLException e) {
			throw new ServletException("Servlet Could not display records.", e);
		} catch (ClassNotFoundException e) {
			throw new ServletException("JDBC Driver  not found.", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e) {
			}
		} // eof try

		return r;

	}

	/**
	 * 
	 * @param out
	 *            :text-output stream PrintWriter
	 * @param db
	 *            :string nome censimento
	 * @param query
	 *            :String query da eseguire
	 * @return ArrayList<HashMap<String, String>> : risultato query pronto per
	 *         jsonEncode4PvList
	 * @throws ServletException
	 */
	public PagingData queryDb4Pv(String db, String query, String queryCount) throws ServletException {
		new GsonBuilder().create();
		ArrayList<HashMap<String, String>> pvList = new ArrayList<HashMap<String, String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		new Consts();
		PagingData r;
		try {
		
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ db, Consts.user, Consts.passwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			// displaying records
			while (rs.next()) {
				HashMap<String, String> pv = new HashMap<String, String>();
				pv.put("potenziale", rs.getString(1));
				pv.put("cod_cliente", rs.getString("cod_cliente"));
				pv.put("pv_id", Integer.toString(rs.getInt("pv_id")));
				pv.put("cod_mmas", rs.getString(4));
				pv.put("ragione_sociale", rs.getString(5));
				pv.put("indirizzo", rs.getString("indirizzo"));
				pv.put("comune", rs.getString("comune"));
				pv.put("cap", rs.getString("cap"));
				pv.put("provincia", rs.getString("provincia"));
				pv.put("telefono", rs.getString("telefono"));
				pv.put("codice_fiscale", rs.getString("codice_fiscale"));
				pv.put("fax", rs.getString("fax"));
				pv.put("titolare", rs.getString("titolare"));
				pv.put("cliente", Integer.toString(rs.getInt("cliente")));

				pvList.add(pv);
				// out.println("<p>"+rs.getObject(2).toString()+"</p>");

			}
			// calcolo totalProperty
			ResultSet total = stmt.executeQuery(queryCount);
			total.next();// apro il resultset
			int totalCount = total.getInt("total");
			con.close();
			// ArrayList<HashMap<String, Object>> result = new
			// ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> data = new HashMap<String, Object>();
			// data.put("success",true );
			data.put("result", pvList);
			data.put("total", totalCount);
			r = new PagingData(pvList, totalCount);
			// gson.toJson(data, out);

		} catch (SQLException e) {
			throw new ServletException("Servlet Could not display records.", e);
		} catch (ClassNotFoundException e) {
			throw new ServletException("JDBC Driver  not found.", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException e) {
			}
		} // eof try

		return r;

	}

	public void doTask(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		Boolean debug = new Boolean(false);
		String censimento = request.getParameter("censimento");
		//String reg = request.getParameter("reg");
		if (request.getParameterMap().containsKey("debug")) {
			debug = true;
		}
		paging(response, request, censimento, debug);
	}

	/**
	 * 
	 * @param query
	 *            query per il fetching dei pv
	 * @param censimento
	 *            censimento selezionato
	 * @param out
	 *            outputStream
	 * @param queryCount
	 *            : query per il fetching della totalProperty
	 */
	protected void paging_list(String query, String censimento, PrintWriter out, String queryCount) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		PagingData rs = null;
		try {
			rs = queryDb4Pv(out, censimento, query, queryCount);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		if (rs != null) {
			rs.setQuery(query);
			jsonEncode(rs, out);
			out.close();
		} else {
			out.println("errore in queryDb4Pv: " + query);
		}

	}

	/**
	 * genera lo arrayList delle query per ogni singolo tab che verrà usato da
	 * queryUnifier per generare la query filtro complessiva
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param queries
	 *            ArrayList<String> è lo ArrayList che conterrà
	 * @param q
	 * @return
	 */
	public ArrayList<String> prepareFilter(HttpServletRequest request) {
		QueryBuilder q = new QueryBuilder();
		ArrayList<String> queries = new ArrayList<String>();
		// se e' stato definito un filtro sui parametri genero la query relativa
		if (request.getParameterMap().containsKey("parametri")) {
			String parametri = request.getParameter("parametri");

			ArrayList<String> step1 = q.fromString2Array(parametri);
			ArrayList<Parameter> step2 = q.fromString2ParameterArray(step1,
					"parametri");
			String queryP = q.buildTabQuery(step2, "parametri");
			queries.add(queryP);
		}
		// se e' stato definito un filtro sui potenziali genero la query
		// relativa
		if (request.getParameterMap().containsKey("potenziali")) {
			String potenziali = request.getParameter("potenziali");
			ArrayList<String> step1 = q.fromString2Array(potenziali);
			ArrayList<Parameter> step2 = q.fromString2ParameterArray(step1,
					"potenziali");

			String queryP = q.buildTabQuery(step2, "potenziali");
			queries.add(queryP);
		}
		// se e' stato definito un filtro sulle marche genero la query relativa
		if (request.getParameterMap().containsKey("marche")) {
			String marche = request.getParameter("marche");
			ArrayList<String> step1 = q.fromString2Array(marche);
			ArrayList<Parameter> step2 = q.fromString2ParameterArray(step1,
					"marche");

			String queryP = q.buildTabQuery(step2, "marche");
			queries.add(queryP);
		}
		return queries;
	}

	public <E> void paging(HttpServletResponse response, HttpServletRequest request, String censimento, Boolean debug) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// se è richiesto un filtro di sicuro sara' presente almeno una tra
		// questiparametri(parametri, marche,potenziali)
		PagingQueries query = new PagingQueries(request);
		Queries q = query.getQuery();
		if (debug) {

			if (request.getParameterMap().containsKey("debug")) {
				try {
					out = response.getWriter();
					Iterator<String> t = request.getParameterMap().keySet().iterator();
					while (t.hasNext()) {
						t.next();
						// out.println("<p> chiave:"+key+" parametro: "+request.getParameter(key)+"</p>");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				out.println("<p> queries " + q + "</p>");
			}
		}
		paging_list(q.getQueryList(), censimento, out, q.getQueryCount());

	}

	public Boolean isFiltered(HttpServletRequest request) {
		Boolean b = new Boolean(false);
		if (request.getParameterMap().containsKey("parametri")) {
			b = true;
		}
		if (request.getParameterMap().containsKey("marche")) {
			b = true;
		}
		if (request.getParameterMap().containsKey("potenziali")) {
			b = true;
		}
		return b;
	}

	public String buildFilterSection(String parametri, String potenziali,
			String marche) {
		QueryBuilder q = new QueryBuilder();
		HashMap<Integer, ArrayList<String>> para = q
				.makeFilterSectionParameters(parametri);
		HashMap<Integer, ArrayList<String>> brands = q
				.makeFilterSectionBrands(marche);
		HashMap<Integer, ArrayList<String>> pot = q
				.makeFilterSectionPotentials(potenziali);
		StringBuilder section = new StringBuilder();
		section.append(para);
		section.append(" and ");
		section.append(brands);
		section.append(" and ");
		section.append(pot);
		return section.toString();

	}

}
