package util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FindFunction {
	public ArrayList<FindAttribute> fetchAttributes(HttpServletRequest request)
	{
		Gson gson = new GsonBuilder().create();
		String search = request.getParameter("search");
		Type type=new  TypeToken<ArrayList<HashMap<String,Object>>>(){}.getType();
		ArrayList<HashMap<String,Object>> searchAttributes=gson.fromJson(search,type);
		ArrayList<FindAttribute> attributes= new ArrayList<FindAttribute>();
		Iterator<HashMap<String, Object>> itr = searchAttributes.iterator();
		while(itr.hasNext())
		{
			HashMap<String, Object> attribute = itr.next();
			FindAttribute fa= new FindAttribute(attribute);
			attributes.add(fa);
			
		}
		return attributes;
	}
	public String buildClause(ArrayList<FindAttribute> attributes)
	{
		StringBuilder clause= new StringBuilder();
		Iterator<FindAttribute> itr = attributes.iterator();
		while (itr.hasNext())
		{
			clause.append(" and ");
			FindAttribute f = itr.next();
			clause.append(f.getClause());
		}
		return clause.toString();
	}
	public String getClause(HttpServletRequest request)
	{
		System.out.println("getClause");
		ArrayList<FindAttribute> attributes= fetchAttributes(request);
		return buildClause(attributes);
		
		
		
	}
	
	
	public String getClause(ArrayList<FindAttribute> attributes)
	{
		return buildClause(attributes);
		
		
		
	}

}
