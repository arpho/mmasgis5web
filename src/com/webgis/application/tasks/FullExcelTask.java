package com.webgis.application.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.PagingData;
import util.excelExporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webgis.application.tasks.paging.PagingQueries;
import com.webgis.application.tasks.paging.PagingQueries.Queries;

public class FullExcelTask extends Task{

	@Override
	public void doTask(HttpServletRequest request, HttpServletResponse response) {
		
		Gson gson = new GsonBuilder().create();
		//Boolean debug;
		String censimento=request.getParameter("censimento");
		String jsonHeader=request.getParameter("header");
		System.out.println(jsonHeader);
		Type typeHeader=new  TypeToken<ArrayList<String>>(){}.getType();
		ArrayList<String> header=gson.fromJson(jsonHeader,typeHeader);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=estrazione_"+ censimento +".xls");
		//if (request.getParameterMap().containsKey("debug")) {
		//	debug = true;
		//}
		PagingQueries query= new PagingQueries(request);
		Queries q = query.getQuery();// genero le queries che soddisfano la richiesta
		RefactoredPagingTask ref = new RefactoredPagingTask();// riutilizzo il codice che genera la lista di pv dal resultset
		PagingData pd=null;
		try {
			pd = ref.queryDb4Pv(censimento, q.getFullQuery(), q.getQueryCount());// ottengo  la lista di pv dal resultset completo ovvero senza la clausola limit
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		export(pd,response, header);//esporto in excel
	}
	
	public void export(PagingData data,HttpServletResponse response)
	{
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//se è richiesto un filtro di sicuro sara' presente almeno una tra questiparametri(parametri, marche,potenziali)
		excelExporter excel= new excelExporter(outputStream);
		excel.exportList(data.getResults());
		//TODO devi usare exportList(data.getResults(),header)

	}

	public void export(PagingData data,HttpServletResponse response, ArrayList<String> header)
	{
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//se è richiesto un filtro di sicuro sara' presente almeno una tra questiparametri(parametri, marche,potenziali)
		excelExporter excel= new excelExporter(outputStream, header);
		excel.exportList(data.getResults(), header);
		//TODO devi usare exportList(data.getResults(),header)
	}
	
}
