package com.webgis.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;

//import com.mycompany.application.tasks.PagingTask;
//import com.mycompany.application.tasks.Pv_listTask;
import com.webgis.application.tasks.BrandsListTask;
import com.webgis.application.tasks.BrandsValueListTask;
import com.webgis.application.tasks.ExcelTask;
import com.webgis.application.tasks.FullExcelTask;
import com.webgis.application.tasks.GetClassTask;
import com.webgis.application.tasks.GetValueTask;
import com.webgis.application.tasks.ParameterListTask;
import com.webgis.application.tasks.PotentialsListTask;
import com.webgis.application.tasks.RefactoredPagingTask;
import com.webgis.application.tasks.Task;
import com.webgis.application.tasks.brandsAnalysisTask;


/**
 * Servlet implementation class GreetingServlet
 */
@WebServlet(description = " servlet MMASGIS", urlPatterns = { "/GreetingServlet" })
public class GreetingServlet extends HttpServlet implements Servlet {

	HashMap<String, Task> taskActions;


	public class PagingData
	{
		public PagingData(ArrayList<HashMap<String, String>> pvList,
				int totalCount) {
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

	
	
	



	/**
	 * elenca i tasks della servlet
	 * @author giuseppe
	 */
	enum tasks
	{
	Pv_list,
	Parameters_list,
	Potentials_list,
	Brands_list,
	Brands_value_list,
	GetClass,
	GetValue,
	Filter,
	Paging,
	excel,
	}
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GreetingServlet() {
    	
        super();
        
   
        new Consts();
        taskActions= new HashMap<String, Task>();
        //taskActions.put("pv_list", new Pv_listTask());
        taskActions.put("brands_list",new BrandsListTask());
        taskActions.put("brands_values_list", new BrandsValueListTask());
        taskActions.put("parameters_list", new ParameterListTask());
        taskActions.put("potentials_list", new PotentialsListTask());
        taskActions.put("getClass",new GetClassTask());
        taskActions.put("getValue", new GetValueTask());
        taskActions.put("paging", new RefactoredPagingTask());
        taskActions.put("excel",new ExcelTask());
        taskActions.put("fullExcel", new FullExcelTask());
        taskActions.put("brands_analysis", new brandsAnalysisTask());
        
        //tasks_actions.put(key, value)
    }



    


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String task=request.getParameter("task");
		response.setContentType("application/json");
		response.setContentType("text/html;charset=UTF-8");
		taskActions.get(task).doTask(request, response);
		
        }
	

	


	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ })
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException
		{
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			
			String task=request.getParameter("task").toString();
			taskActions.get(task).doTask(request, response);
		}
}