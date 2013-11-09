/**
 * contiene le classi che che eseguono i tasks della servlet
 */
package com.webgis.application.tasks;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.PagingData;
/**
 * classe parent dei task, contiene alcuni metodi usati da più tasks
 * @author giuseppe
 * 
 */
public abstract class Task {
	

	/**
	 * esegue la codifica in json del resultset per il paging e lo invia sullo output stream
	 * @param data: PagingData
	 * @param out:text-output stream PrintWriter
	 */
	public void jsonEncode(PagingData data,PrintWriter out)
	{
		/////////DEBUG
		//System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		//////////
		Gson gson = new GsonBuilder().create();
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("results", data.getResults());
		result.put("total", data.getTotal());
		result.put("success", true);
		gson.toJson(result, out);
	}
	/**
	 * esegue la codifica in json del resultset e lo invia sullo output stream
	 * @param data: ArrayList<HashMap<String, String>>
	 * @param out:text-output stream PrintWriter
	 */	
	public void jsonEncode(ArrayList<HashMap<String, String>> data,PrintWriter out)
	{
		Gson gson = new GsonBuilder().create();
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("results", data);
		result.put("success", true);
		gson.toJson(result, out);
	}
	
	/**
	 * esegue il parsing delle selezioni geografiche fatte dal client, generando la lista delle utb
	 * @param selections
	 * @note il parametro selections ricevuto dal client è nel formato utb1|utb2|..
	 * @return ArrayList<String>
	 */
	public ArrayList<String> extractUTB(String selections)
	{ 
		ArrayList<String> utbList= new ArrayList<String>();
		StringTokenizer st2 = new StringTokenizer(selections, "|");
		while (st2.hasMoreElements()) 
		{
			utbList.add(st2.nextElement().toString());
		}
		return utbList;
			
		}
	
	public abstract void doTask(HttpServletRequest request,HttpServletResponse response);

}
