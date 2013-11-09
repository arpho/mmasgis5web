package util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class excelExporter {

	ServletOutputStream file;
	ArrayList<String> header;
	Boolean loadedHead;
	Integer row;
	WritableSheet sheet;
	WritableWorkbook workbook;


	public excelExporter(ServletOutputStream file) {
		this.loadedHead = new Boolean(false);
		this.file = file;
		this.row = new Integer(1);
		this.header = new ArrayList<String>();
		try {
			this.workbook = Workbook.createWorkbook(file);//new File(file));
			this.sheet = this.workbook.createSheet("First Sheet", 0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public excelExporter(ServletOutputStream file, ArrayList<String> header) {
		this.header = header;
		this.loadedHead = new Boolean(true);
		this.file = file;
		this.row = new Integer(1);
		this.header = new ArrayList<String>();
		try {
			this.workbook = Workbook.createWorkbook(file);
			this.sheet = this.workbook.createSheet("First Sheet", 0);
			writeHeader(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 * 
	 * @param file String nome del file 
	 */
	public excelExporter(String file) {
		this.loadedHead = new Boolean(false);
		this.row = new Integer(1);
		this.header = new ArrayList<String>();
		try {
			this.workbook = Workbook.createWorkbook(new File(file));
			this.sheet = this.workbook.createSheet("First Sheet", 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 * esegue la scrittura e la chiusura del foglio excel
	 */
	private void close() {
		try {
			this.workbook.write();
			this.workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	
	public void exportdb(PagingData l) {
		exportList(l.getResults());
	}
	
	
	/**
	 * esporta una lista di record tramite chiamate successive a exportRecord
	 * @param fromJson ArrayList<LinkedHashMap<String, String>> lista di record da esportare, ricevuta dal client
	 * 
	 */
	public void  exportList(ArrayList<HashMap<String, String>> fromJson) {

		int row = 1;
		Iterator<HashMap<String, String>> i = fromJson.iterator();
		while (i.hasNext())
		{
			Map<String, String>r = i.next();
			exportRecord(r,row);
			row = row+1;
		}
		close();
	}

	
	/**
	 * esporta una lista di record tramite chiamate successive a exportRecord
	 * @param fromJson ArrayList<LinkedHashMap<String, String>> lista di record da esportare, ricevuta dal client
	 * 
	 */
	public void  exportList(ArrayList<HashMap<String, String>> fromJson, ArrayList<String> header) {
		//System.out.println("***lista record da esportare"+fromJson);

		int row = 1;
		Iterator<HashMap<String, String>> i = fromJson.iterator();
		while (i.hasNext())
		{
			Map<String, String>r = i.next();
			//System.out.println("***item:"+r);
			exportRecord(r,row,header);
			row = row+1;

		}
		close();
	}


	/**
	 * esporta un record sul foglio excel aggiungendovi una riga
	 * inoltre popola la prima riga, cioe' gli header se questi non sono ancora stati caricati
	 * @param r
	 * @param row: int riga su cui scrivere
	 */
	public void exportRecord(Map<String, String> r,int row) {
		r.keySet().iterator();
		if (!isHeaderLoaded())
		{// se lo header non è stato ancora 
			populateHeader(r);
			writeHeader(this.header);
		}
		writeRow(r, row,this.header);
		//close();
	}

	
	/**
	 * esporta un record sul foglio excel aggiungendovi una riga
	 * inoltre popola la prima riga, cioe' gli header se questi non sono ancora stati caricati
	 * @param r
	 * @param row: int riga su cui scrivere
	 */
	public void exportRecord(Map<String, String> r,int row,ArrayList<String> header) {
		//r.keySet().iterator();
		//System.out.println("***exportRecord: " + row);
		writeRow(r, row,header);
		//close();



	}
	
	
	public ArrayList<String> getHeader() {
		return header;
	}

	
	public WritableWorkbook getWorkbook() {
		return this.workbook;
	}

	
	/**
	 * controlla che la lista degli header sia popolata
	 * @return 
	 */
	private Boolean isHeaderLoaded() {

		return this.loadedHead;
	}
	
	
	/**
	 * popola la lista degli header
	 * @param r HashMap>String,String>
	 * @note viene invocata solo una volta prima che venga esportato il primo record di una lista di record
	 */
	/*
	private void populateHeader(ArrayList<String> r) {
		this.header=r;
		this.loadedHead=true;
	}
	 */

	/**
	 * popola la lista degli header
	 * @param r HashMap>String,String>
	 * @note viene invocata solo una volta prima che venga esportato il primo record diunalista di record
	 */
	private void populateHeader(Map<String, String> r) {
		Iterator<String> i=r.keySet().iterator();
		while (i.hasNext())
		{
			String h=i.next();
			this.header.add(h);

		}
		this.loadedHead=true;
	}
	
	
	private void writeHeader(ArrayList<String> header) {
		Iterator<String> h=header.iterator();
		Integer c=new Integer(0);
		while ( h.hasNext()) {
			String s=h.next();
			Label label = new Label(c,0, s); 
			c+=1;
			try {
				this.sheet.addCell(label);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * scrive una riga sul foglio excel
	 * @param r record che verra' scritto sul foglio excel Map<String, String> 
	 * @param row
	 * @param header
	 */
	public void writeRow(Map<String, String> r,Integer row,ArrayList<String> header) {
		//System.out.println("***writeRow: header: "+header);
		//System.out.println("***row: "+row);
		Iterator<String> h=header.iterator();
		Integer c=new Integer(0);
		//setto la larghezza della prima colonna a 30 caratteri
		//this.sheet.setColumnView(0, 30);
		//ciclo il record e riempio le celle corrispondenti
		while ( h.hasNext())
		{
			String s=h.next();
			//System.out.println("***aggiungo colonna: "+s);
			Label label = new Label(c,row, r.get(s)); 
			//System.out.println("***valore colonna: "+r.get(s));
			//System.out.println("***lunghezza stringa: "+r.get(s).length());
			try {
				this.sheet.addCell(label);
				//this.sheet.setColumnView(row, r.get(s).length());
			} catch (RowsExceededException e) {
				//System.out.println("");
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c+=1;
		}
	}

}
