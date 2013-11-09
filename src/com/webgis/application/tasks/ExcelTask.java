package com.webgis.application.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.excelExporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * implementa l'esportazione in excel dei soli record selezionati dal client
 * @author giuseppe
 *
 */
public class ExcelTask extends Task {
	
	public void doTask(HttpServletRequest request,HttpServletResponse response)
	{
		Gson gson = new GsonBuilder().create();
		String jsonRecord=request.getParameter("selections");
		String jsonHeader=request.getParameter("header");
		String censimento=request.getParameter("censimento");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=estrazione_"+ censimento +".xls");
		Type type=new  TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();
		ArrayList<HashMap<String,String>> records=gson.fromJson(jsonRecord,type);
		Type typeHeader=new  TypeToken<ArrayList<String>>(){}.getType();
		ArrayList<String> header=gson.fromJson(jsonHeader,typeHeader);
		//System.out.println("***header"+ header);
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		excelExporter e= new excelExporter(outputStream, header);
		e.exportList(records, header);
		try {
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
