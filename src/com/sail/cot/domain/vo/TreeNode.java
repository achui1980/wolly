/**
 * 
 */
package com.sail.cot.domain.vo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.sail.cot.domain.CotModule;

/**
 * <p>Title: Ext+DWR+Spring</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 17, 2008 5:51:07 PM </p>
 * <p>Class Name: TreeNode.java </p>
 * @author achui
 *
 */
public  class TreeNode {   
	private String id;  
	private String url;
	private String Text;   
	private boolean leaf;   
	private String cls="";  
	private String href = "";
	private boolean expandable; //是否展开 
	private String description; //描述信息 

	//iframe的名称
	private String hrefTarget="f2";
	private Map<String,String> map = new HashMap<String,String>();
	private Map<String,String> parentMap = new HashMap<String,String>();
	//private String hrefTarget = "";   
	//private OaModule oaModule;
	public TreeNode(){
		
	}
	public TreeNode(List<CotModule> list)   
	{   
		Iterator itreator = list.iterator();
		while(itreator.hasNext())
		{
			CotModule oaModule = (CotModule)itreator.next();
			this.map.put(oaModule.getId().toString(), oaModule.getId().toString());
			if(oaModule.getMoudleParent() != null)
				this.parentMap.put(oaModule.getMoudleParent().toString(),oaModule.getId().toString());
		}   
	}      
	  
	public String getId() {        
	        return this.id;   
	}   
	    
	public boolean getLeaf() {   
		return this.parentMap.get(this.id) == null?true:false; 
	}          
	  
	public String getText() {  
		//System.out.println("name:"+oaModule.getModuleName());
	        return this.Text;
	}   
	  
	public String getCls() {   
	    return this.parentMap.get(this.id) == null?"file":"folder";   
	}

	public String getHref() {
		return this.href;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setText(String text) {
		Text = text;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHrefTarget() {
		return hrefTarget;
	}

	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	public boolean isExpandable() {
		return expandable;
	}
	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}