package com.sail.cot.util;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanUtils;

public class ListSort implements Comparator<Object>{
	
	private boolean type;//true升序,false降序
	private String field;//属性名
	private String tbName;//对象名
	private String fieldType;//属性类型
	
	//null值为最小
	//返回负数表示o1 小于o2，返回0 表示o1和o2相等，返回正数表示o1大于o2。---升序
	//返回负数表示o1 大于o2，返回0 表示o1和o2相等，返回正数表示o1小于o2。---降序
	public int compare(Object o1, Object o2) {
		try {
			Class clzz = Class.forName("com.sail.cot.domain."+this.getTbName());
			Object obj1 = clzz.cast(o1);
			Object obj2 = clzz.cast(o2);
			String value1 = BeanUtils.getProperty(obj1, this.getField());
			String value2 = BeanUtils.getProperty(obj2, this.getField());
			
			if(this.getFieldType().equals("int")){
				if(this.isType()==false){
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return 1;
					}
					if(value1!=null && value2==null){
						return -1;
					}
					int a = Integer.parseInt(value1);
					int b = Integer.parseInt(value2);
					return (a < b ? 1 : (a == b ? 0 : -1));
				}else{
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return -1;
					}
					if(value1!=null && value2==null){
						return 1;
					}
					int a = Integer.parseInt(value1);
					int b = Integer.parseInt(value2);
					return (a < b ? -1 : (a == b ? 0 : 1));
				}
			}
			if(this.getFieldType().equals("float")){
				if(this.isType()==false){
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return 1;
					}
					if(value1!=null && value2==null){
						return -1;
					}
					float a = Float.parseFloat(value1);
					float b = Float.parseFloat(value2);
					return (a < b ? 1 : (a == b ? 0 : -1));
				}else{
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return -1;
					}
					if(value1!=null && value2==null){
						return 1;
					}
					float a = Float.parseFloat(value1);
					float b = Float.parseFloat(value2);
					return (a < b ? -1 : (a == b ? 0 : 1));
				}
			}
			if(this.getFieldType().equals("string")){
				if(this.isType()==false){
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return 1;
					}
					if(value1!=null && value2==null){
						return -1;
					}
					return (value1.compareTo(value2)<0 ? 1 : (value1.compareTo(value2)==0 ? 0 : -1));
				}else{
					if(value1==null && value2==null){
						return 0;
					}
					if(value1==null && value2!=null){
						return -1;
					}
					if(value1!=null && value2==null){
						return 1;
					}
					return (value1.compareTo(value2)<0 ? -1 : (value1.compareTo(value2)==0 ? 0 : 1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	public boolean isType() {
		return type;
	}


	public void setType(boolean type) {
		this.type = type;
	}


	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}


}
