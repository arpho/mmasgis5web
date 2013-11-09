package util;

import java.util.HashMap;

public class FindAttribute
{
	private String field,value;
	private Boolean option;
	
	public FindAttribute(HashMap<String,Object> field)
	{
		this.field=(String) field.get("field");
		this.value= (String) field.get("value");
		this.option=(Boolean) field.get("option");
	}

	public String getClause()
	{
		if (! this.option)
		{
			StringBuilder clause= new StringBuilder();
			clause.append(this.field);
			clause.append(" ='");
			
			clause.append(this.value);
			clause.append('\'');
			return clause.toString();
		}
		StringBuilder clause= new StringBuilder();
		clause.append(this.field);
		clause.append(" like '%");
		
		clause.append(this.value);
		clause.append("%\'");
		return clause.toString();
		
	}
}
