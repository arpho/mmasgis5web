package util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * e' la struttura datiusata da jsonEncode per generare il pacchetto json
 * @author giuseppe
 *
 */
public class PagingData
{
	String query;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.query = query;
	}
	public PagingData(ArrayList<HashMap<String, String>> pvList,
			int totalCount) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.total=totalCount;
		this.results=pvList;
	}
	public ArrayList<HashMap<String, String>> getResults() {
		return results;
	}
	public void setResults(ArrayList<HashMap<String, String>> results) {
		this.results = results;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	ArrayList<HashMap<String, String>> results;
	int total;
	public void pagingData(ArrayList<HashMap<String, String>> results,int total)
	{
		this.results=results;
		this.total=total;
	}
}