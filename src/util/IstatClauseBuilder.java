package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class IstatClauseBuilder {
	
	ArrayList<String> utbs_reg;
	ArrayList<String> utbs_pro;
	ArrayList<String> utbs_com;
	
	public IstatClauseBuilder(ArrayList<String> ur, ArrayList<String> up, ArrayList<String> uc) {
		utbs_reg = ur;
		utbs_pro = up;
		utbs_com = uc;
	}
	
	protected ArrayList<String> getTcIstat(String layer) {
		
		ArrayList<String> result = new ArrayList<String>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Iterator it = null;
		String query = "";
		if(layer.equals("reg")) {
			it = utbs_reg.iterator();
			query = Consts.queryIstatRegioni;
		}
		if(layer.equals("pro")) {
			it = utbs_pro.iterator();
			query = Consts.queryIstatProvince;
		}
		if(layer.equals("com")) {
			it = utbs_com.iterator();
			query = Consts.queryIstatComuni;
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mmasgisDB",Consts.user,Consts.passwd);
			while(it.hasNext()) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query+it.next().toString());
				
				while(rs.next()) {
					result.add(rs.getString("tc_istat_id"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getIstatClause() {
		
		ArrayList<String> tc_istat = new ArrayList<String>();
		
		if(!this.utbs_reg.isEmpty()) {
			System.out.println("regioni");
			ArrayList<String> tmp = this.getTcIstat("reg");
			Iterator it = tmp.iterator();
			while(it.hasNext()) {
				tc_istat.add(it.next().toString());
			}
		}
		if(!this.utbs_pro.isEmpty()) {
			System.out.println("province");
			ArrayList<String> tmp = this.getTcIstat("pro");
			Iterator it = tmp.iterator();
			while(it.hasNext()) {
				tc_istat.add(it.next().toString());
			}
		}
		if(!this.utbs_com.isEmpty()) {
			System.out.println("comuni");
			ArrayList<String> tmp = this.getTcIstat("com");
			Iterator it = tmp.iterator();
			while(it.hasNext()) {
				tc_istat.add(it.next().toString());
			}
		}
		
		Iterator itr = tc_istat.iterator();
		String s = "(";
		while(itr.hasNext()) {
			s = s + "tc_istat_id= " + itr.next().toString();
			if(itr.hasNext()) {
				s = s + " OR ";
			}
		}
		s = s + ")";
		
		return s;
	}

}
