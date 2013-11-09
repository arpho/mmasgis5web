package com.webgis.application.tasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;
import util.IstatClauseBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webgis.application.IstatBase;
import com.webgis.application.IstatRegioni;
import com.webgis.application.Paging;

public class brandsAnalysisTask extends Task {

	@Override
	public void doTask(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("brandsAnalysisTask");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		//ArrayList<HashMap<String,String>> total = new ArrayList<HashMap<String,String>>();
		
		String censimento = request.getParameter("censimento");
		String classeId = request.getParameter("classe");
		String reg = request.getParameter("reg");
		String pro = request.getParameter("pro");
		String com = request.getParameter("com");
		String cap = request.getParameter("cap");
		      
		Paging p = new Paging();
		//creo array contenente codici utb
		ArrayList<String> utbs_reg = p.extractUTB(reg);
		ArrayList<String> utbs_pro = p.extractUTB(pro);
		ArrayList<String> utbs_com = p.extractUTB(com);
		//ottengo il blocco di query con i tc_istat
		IstatClauseBuilder istat = new IstatClauseBuilder(utbs_reg, utbs_pro, utbs_com);
		String query_istat = istat.getIstatClause();
		
		result = this.executeAnaylis(censimento, classeId, query_istat);
			
		jsonEncode(result, out);
			
	}

	
	
	private ArrayList<HashMap<String, String>> executeAnaylis(String censimento, String classeId, String query_istat) {
		// TODO Auto-generated method stub
		//eseguo prima query che conta pv e somma potenziali
		
		ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		
		Connection con = null;
		Connection con2 = null;
		Statement stmt = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		Iterator it = null;
		String base_pot = "", base_num = "";
		
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+censimento,Consts.user,Consts.passwd);
			stmt = con.createStatement();
			System.out.println(Consts.queryBrandsAnalysisView  + query_istat);
			int i = stmt.executeUpdate(Consts.queryBrandsAnalysisView  + query_istat);

			rs = stmt.executeQuery(Consts.queryBrandsAnalysisFirst);
			
			while(rs.next()) {
				base_num = rs.getString("base_num");
				base_pot = rs.getString("base_pot");
			}
			
			rs = stmt.executeQuery(Consts.queryBrandsAnalysisList + classeId);
			con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+censimento,Consts.user,Consts.passwd);
			stmt2 = con2.createStatement();
			while(rs.next()) {
				String query=String.format(Consts.queryBrandsAnalysisSingle, classeId, rs.getString("tc_mar_id"));
				System.out.println(query);
				rs2 = stmt2.executeQuery(query);
				
				while(rs2.next()) {
					
					System.out.println("marca: "+ rs.getString("testo") + " " + rs2.getString("num_pv") + " " + rs2.getString("pot_pv"));
					HashMap<String, String> tmp = new HashMap<String,String>();
					String testo = rs.getString("testo");
					int  num_pv = Integer.parseInt(rs2.getString("num_pv"));
					double numerica = (double)(num_pv)/Double.parseDouble(base_num)*100;
					numerica = (double)((int)(numerica*1000))/1000;
					double pot_pv = Double.parseDouble(rs2.getString("pot_pv"));
					pot_pv = (double)((int)(pot_pv*1000))/1000;
					double ponderata = (pot_pv/Double.parseDouble(base_pot))*100;
					ponderata = (double)((int)(ponderata*1000))/1000;
					
					stmt3 = con2.createStatement();
					System.out.println(String.format(Consts.queryBrandsAnalysisPot, classeId, rs.getString("tc_mar_id")));
					rs3 = stmt3.executeQuery(String.format(Consts.queryBrandsAnalysisPot, classeId, rs.getString("tc_mar_id")));
					rs3.next();
					
					double pot_marca = Double.parseDouble(rs3.getString("pot_marca"));
					double ind_marca = (pot_marca / Double.parseDouble(base_pot))*100;
					ind_marca = (double)((int)(ind_marca*1000))/1000;
					double eff_marca = (pot_marca / pot_pv);
					eff_marca = (double)((int)(eff_marca*1000))/1000;
					double eff_valore = (ponderata / Double.parseDouble(base_num));
					eff_valore = (double)((int)(eff_valore*1000))/1000;
					
					
					tmp.put("marca", testo);
					tmp.put("num_pv", String.valueOf(num_pv));
					tmp.put("numerica", String.valueOf(numerica));
					tmp.put("pot_pv", String.valueOf(pot_pv));
					tmp.put("ponderata", String.valueOf(ponderata));
					tmp.put("pot_marca", String.valueOf(pot_marca));
					tmp.put("ind_marca", String.valueOf(ind_marca));
					tmp.put("eff_marca", String.valueOf(eff_marca));
					tmp.put("eff_valore", String.valueOf(eff_valore));
					tmp.put("base_num", base_num);
					tmp.put("base_pot", base_pot);
					
					result.add(tmp);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
