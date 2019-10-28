/**
 * Copyright © 2011 IBM Corporation. All Rights Reserved.
 *
 * Change Log:
 *
 * Date 			Change Flag 	Author 	Descriptions of Changes
 * Aug 22, 2011 		CREATE 		zymao 	created.
 */
package com.boc.cloud.api.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author zymao
 * base utilization functions
 */
public class Common {
	/**
	 * check object whether blank or not 
	 * @param obj Object
	 * @return is null or not
	 */
	public static boolean isNull(Object obj) {
		return (obj == null) || (obj.toString().trim().equals(""))
			|| (obj.toString().toLowerCase().trim().equals("null"));
	}
	
	/**
	 * check object whether blank or not 
	 * @param obj Object
	 * @param obj_null object which is returned  if obj is null
	 * @return if obj is null then return obj_null
	 */
	public static Object isNull(Object obj,Object obj_null) {
		if(isNull(obj))return obj_null;
		else return obj;
	}
	
	/**
	 * check object whether blank or not 
	 * @param obj Object
	 * @param null_value value which is returned if obj is null
	 * @return if obj is null then return null_value
	 */
	public static int isNull(Object obj,int null_value) {
		if(isNull(obj))return null_value;
		else return Integer.parseInt(obj.toString());
	}
	
	/**
	 * check string whether blank or not 
	 * @param strText string
	 * @return is null or not
	 */
	public static boolean isNull(String str) {
		return (str == null) || (str.trim().equals(""));
	}
	
	/**
	 * check param_value is or not integer
	 * @param value string
	 * @return
	 */
	public static boolean isInteger(String value){
		try{
			Integer.parseInt(value);
			return true;
		}catch(Throwable t) {
			return false;
		}
	}
	
	/**
	 * Check if the value is Array object
	 * @param value object value 
	 * @return
	 */
	public static boolean isArray(Object value){
		if(value!=null){
			return value.toString().matches("^\\[[[\\w]+\\.]*[\\w]+;@[\\w]+$");
		}else{
			return false;
		}
	}
	/**
	 * Check if the value is List object
	 * @param value object value 
	 * @return
	 */
	public static boolean isList(Object value){
		if(value!=null){
			return value.toString().matches("^\\[[[,\\s]*[[\\w]+\\.]*[\\w]+@[\\w]+]+\\]$");
		}else{
			return false;
		}
	}
	/**
	 * Check if the value is embedded object
	 * @param value object value
	 * @return
	 */
	public static boolean isObject(Object value){
		if(value!=null){
			return value.toString().matches("^[[\\w]+\\.]*[\\w]+@[\\w]+$");
		}else{
			return false;
		}
	}
	
	/**
     * check whether or not equal between array a1 and array a2
     * @param a1 1 dimension array
     * @param a2 1 dimension array
     * @return
     */
    public static boolean equal(String[] a1,String[] a2){
    	if(a1.length==a2.length){
	    	for(int i=0;i<a1.length;i++){
	    		if(!a1[i].equals(a2[i])){
	    			return false;
	    		}
	    	}
	    	return true;
    	}
    	return false;
    }
    
    /**
     * Check if 2 objects are equal
     * @param obj1 Object
     * @param obj2 Object
     * @return if 2 objects are equal
     */
    public static boolean equal(Object obj1,Object obj2){
    	return (obj1==null && obj2==null) || 
    	       (obj1!=null && obj2!=null && obj1.equals(obj2));
    }
    
    /**
     * Check if 2 strings are equal
     * @param str1 Object
     * @param str2 Object
     * @return if 2 strings are equal
     */
    public static boolean equal(String str1,String str2){
    	return (str1==null && str2==null) || 
    	       (str1!=null && str2!=null && str1.trim().equals(str2.trim()));
    }
    
    /**
	 * Check str is or not equal with strIn
	 * Or strIn(such as 2) in the range str(such as 1-6).
	 * @param str   string to match
	 * @param strIn string to find
	 * @param ignoreCase ignore case
	 * @param checkRange check range for integer
	 * @return
	 */
	public static boolean equal(String str,String strIn,boolean ignoreCase,boolean checkRange){
		if(checkRange){
			int index=str.indexOf("-");
			if(index>0){
				String begin=str.substring(0,index);
				String end=str.substring(index+1);
				if(isInteger(begin) && isInteger(end) && isInteger(strIn)){
					int bi=Integer.parseInt(begin);
					int ei=Integer.parseInt(end);
					int value=Integer.parseInt(strIn);
					return (value>=bi && value<=ei);
				}
			}
		}
		return equal(strIn,str,ignoreCase);
	}
	/**
	 * Check if 2 String2 are equal 
	 * @param str1 String 1
	 * @param str2 String 2
	 * @param ignoreCase if ignore case
	 * @return
	 */
	public static boolean equal(String str1,String str2,boolean ignoreCase){
		if(str1==null && str2==null){
			return true;
		}else if(str1!=null && str2!=null){
			if(ignoreCase){
				return str1.equalsIgnoreCase(str2);
			}else{
				return str1.equals(str2);
			}
		}else{
			return false;
		}
	}
	
	/**
	 * check num in a_int array
	 * @param a_int int array
	 * @param num number to find
	 * @return if num in a_int array
	 */
	public static boolean in(int[] a_int, int num){
		if(a_int!=null){
            for (int anA_int : a_int) {
                if (anA_int == num) {
                    return true;
                }
            }
		}
		return false;
	}
	
	/**
	 * check strIn in strs array
	 * @param strs string array
	 * @param strIn string to find
	 * @param ignoreCase whether or not ignore case
	 * @param checkRange check range for integer
	 * @return
	 */
	public static boolean in(String[] strs,String strIn,boolean ignoreCase,boolean checkRange){
		if(strIn!=null && strs!=null && strs.length>0){
            for (String str : strs) {
                if (equal(str, strIn, ignoreCase, checkRange)) {
                    return true;
                }
            }
		}
		return false;
	}
	
	/**
	 * check strIn in strs array
	 * @param strs string array
	 * @param strIn string to find
	 * @param ignoreCase whether or not ignore case
	 * @return
	 */
	public static boolean in(String[] strs,String strIn,boolean ignoreCase){
		return in(strs,strIn,ignoreCase,false);
	}
	
	/**
	 * check strIn in strs array
	 * @param strs string array
	 * @param strIn string to find
	 * @return
	 */
	public static boolean in(String[] strs,String strIn){
		return in(strs,strIn,false);
	}
	
	/**
	 * check a array whether or not in a_list array
	 * @param a_list 2 dimensions array
	 * @param a 1 dimension array
	 * @return
	 */
	public static boolean in(String[][] a_list,String[] a){
		if(a_list!=null && a!=null){
            for (String[] anA_list : a_list) {
                if (equal(anA_list, a)) {
                    return true;
                }
            }
		}
    	return false;
    }
	
	/**
	 * check if obj in array list
	 * @param list Object list
	 * @param obj the object to be compared
	 * @param compareField the field to be compared, if null then compare the whole object
	 * @return
	 */
	public static boolean in(List list, Object obj, String compareField){
		if(list!=null && list.size()>0 && obj!=null){
            for (Object aList : list) {
                if (compareField == null) {
                    if (obj.equals(aList)) return true;
                } else if (aList != null) {
                    try {
                        Object fieldValue = PropertyUtils.getProperty(aList, compareField);
                        if (obj.equals(fieldValue)) return true;
                    } catch (Exception e) {
                        break;
                    }
                }
            }
		}
		return false;
	}
    
    /**
     * check str whether or not include any item in a_include
     * @param str string
     * @param a_include string array
     * @return
     */
    public static boolean include(String str,String[] a_include){
    	return getIncludeIndex(str,a_include,true)>=0;
    }
    
    /**
     * check if the string is included any item in a_include,if exist then return the index
     * @param str string object
     * @param a_include string array
     * @param insenstive: case insensitive?
     * @return
     */
    public static int getIncludeIndex(String str,String[] a_include,boolean insensitive){
    	int index;
    	if(a_include!=null){
	    	if(insensitive){
	    		str=str.toLowerCase();
	    	}
	    	String item;
            for (String anA_include : a_include) {
                item = anA_include;
                if (insensitive) {
                    item = item.toLowerCase();
                }
                index = str.indexOf(item);
                if (index >= 0) {
                    return index;
                }
            }
    	}
        return -1;
    }
    
    /**
     * Check if the any item in a_inlcude array are included in a_target array like intersection, for example:
     * there is a number named "2" in {"A","2","B"} are included in {"1","2","3","4","5"}
     * @param a_include array to be checked for inclusion
     * @param a_target target array
     * @return
     */
    public static boolean include(String[] a_include, String[] a_target){
    	if(a_include!=null){
            for (String anA_include : a_include) {
                if (in(a_target, anA_include)) {
                    return true;
                }
            }
    	}
		return false;
    }
    
    /**
     * Check if the all items in a_inlcude array are included in a_target array, for example:
     * the all numbers in {"1","2","3"} are included in {"1","2","3","4","5"}
     * @param a_include array to be checked for inclusion
     * @param a_target target array
     * @return
     */
    public static boolean includeAll(String[] a_include, String[] a_target){
    	if(a_include==null)return false;
    	boolean isIncludeAll=true;
        for (String anA_include : a_include) {
            if (!in(a_target, anA_include)) {
                isIncludeAll = false;
                break;
            }
        }
		return isIncludeAll;
    }
    
    /**
     * check str parameter whether or not include some items in a_include,if exist then return the min index
     * @param str string
     * @param a_include string array
     * @param insenstive: case insensitive?
     * @return
     */
    public static int getMinIndex(String str,String[] a_include,boolean insensitive){
    	int index=-1,item_index;
    	if(insensitive){
    		str=str.toLowerCase();
    	}
    	String item;
        for (String anA_include : a_include) {
            item = anA_include;
            if (insensitive) {
                item = item.toLowerCase();
            }
            item_index = str.indexOf(item);
            if (item_index >= 0) {
                if (index == -1) {
                    index = item_index;
                } else if (item_index < index) {
                    index = item_index;
                }
            }
        }
        return index;
    }
    
    /**
	 * get index from a 1 dimensions array
	 * @param a  1 dimensions array
	 * @param strFind  string to be found
	 * @return index in the array
	 */
	public static int getIndex(String[] a,String strFind){
		for(int i=0;i<a.length;i++){
			if(a[i]!=null && a[i].equals(strFind)){
				return i;
			}
		}
		return -1;
	}
    
    /**
	 * replace [expr] with [expr]+[substitute];
	 * @param str String
	 * @param expr expression to be compiled 
	 * @param substitute substitute string
	 * @param ignoreCase if ignore case
	 * @param isLastOrFirst null=>All, true=>last one, false=>first one
	 * @return
	 */
	public static String replace(String str,String expr,String substitute,boolean ignoreCase,Boolean isLastOrFirst){
		Pattern p;
		if(ignoreCase){
			p=Pattern.compile(expr,Pattern.CASE_INSENSITIVE);
		}else{
			p=Pattern.compile(expr);
		}
		StringBuffer sb = new StringBuffer();
        Matcher m = p.matcher(str);
        int matchCount=0;
        if(isLastOrFirst!=null && isLastOrFirst){
        	while (m.find()){
        		matchCount++;
        	}
        	m = p.matcher(str);//get it again
        }
        int cnt=0;//matcher counter
        while (m.find()){
        	if(isLastOrFirst!=null){
	        	if(isLastOrFirst){//replace last one
	        		if(cnt==matchCount-1){
	        			m.appendReplacement(sb, substitute);
	        		}else{
	        			m.appendReplacement(sb, m.group());
	        		}
	        		cnt++;
	        	}else{//replace first one
	        		if(cnt==0){
	        			m.appendReplacement(sb, substitute);
	        			cnt++;
	        		}else{
	        			m.appendReplacement(sb, m.group());
	        		}
	        	}
        	}else{
        		m.appendReplacement(sb, substitute);
        	}
        }
        m.appendTail(sb);
        return sb.toString();
	}
	
	/**
	 * replace [expr] with [expr]+[substitute];
	 * @param str String
	 * @param expr expression to be compiled 
	 * @param substitute substitute string
	 * @param ignoreCase if ignore case
	 * @return
	 */
	public static String replace(String str,String expr,String substitute,boolean ignoreCase){
		return replace(str,expr,substitute,ignoreCase,null);
	}
	
	/**
	 * replace [expr] with [expr]+[substitute];
	 * @param str String
	 * @param expr expression to be compiled 
	 * @param substitute substitute string
	 * @return
	 */
	public static String replace(String str,String expr,String substitute){
		return replace(str,expr,substitute,false);
	}
	
	/**
	 * format blank or NULL string
	 * @param str string
	 * @param defaultValue default value for blank or NULL string 
	 * @return
	 */
	public static String formatStr(Object obj,String defaultValue){
		if(isNull(obj)){
			return defaultValue;
		}else{
			return obj.toString();
		}
	}
	/**
	 * format blank or NULL string
	 * @param str string
	 * @param defaultValue default value for blank or NULL string 
	 * @return
	 */
	public static <T> T formatObject(T obj, T defaultValue){
		if(isNull(obj)){
			return defaultValue;
		}else{
			return obj;
		}
	}
	/**
     * format NULL string
     * @param str String
     * @return
     */
    public static String formatNullStr(Object obj){
    	return formatStr(obj,"");
    }
    
    /**
     * format Boolean object
     * @param obj String object as default
     * @return
     */
    public static Boolean formatBoolean(Object obj){
    	if(obj==null){
    		return false;
    	}else{
    		return "true".equals(obj.toString().toLowerCase());
    	}
    }
    
    /**
	 * format float number
	 * @param strFloat float number
	 * @return
	 */
	public static String formatFloat(String strFloat){
		String num=strFloat;
		int index=num.indexOf(".");
		while(num.charAt(num.length()-1)=='0'){
			if(num.length()>(index+1)){
				num=num.substring(0,num.length()-1);
			}else{break;}
		}
		if(num.length()==(index+1)){
			num=num.substring(0,num.length()-1);
		}
		return num;
	}
	/**
	 * format date string.for example:Tue, 07 Nov 2006 10:32:53 -0500
	 * @param strDate date string
	 * @return
	 */
	public static String formatDate(String strDate){
		int index=strDate.indexOf("-");
		if(index>=0){
			return strDate.substring(0,index).trim();
		}else{
			return strDate;
		}
	}
	/**
	 * format date to string like this:12/30/2008
	 * @param date java.util.Date
	 * @return date string like this:12/30/2008
	 */
	public static String formatDate(Date date){		
		if(!isNull(date)){
			return new java.sql.Date(date.getTime()).toString();
		}else{
			return "";
		}	
	}
	
	/**
	 * get a item from a 2 dimensions array
	 * @param a   2 dimensions array
	 * @param strFind  2 dimensions array string
	 * @param cmpIndex compared column index
	 * @return a 1 dimension array from the 2 dimensions array
	 */
	public static String[] getItemOfArray(String[][] a,String strFind,int cmpIndex){
		if(strFind!=null){
            for (String[] anA : a) {
                if (anA.length > cmpIndex && anA[cmpIndex].equals(strFind)) {
                    return anA;
                }
            }
		}
		return null;
	}
	
	/**
	 * get a item from a 2 dimensions array
	 * @param a   2 dimensions array
	 * @param strFind  2 dimensions array string
	 * @return a 1 dimension array from the 2 dimensions array
	 */
	public static String[] getItemOfArray(String[][] a,String strFind){
		return getItemOfArray(a,strFind,0);
	}
	
	/**
	 * get date suffix
	 * @param day date number string
	 * @return date suffix
	 */
	public static String getDateSuffix(String day){
		if(day!=null && day.length()>0){
			String[][] DAYFIX={{"1","st"},{"2","nd"}, {"3","rd"},{"21","st"},
					           {"22","nd"}, {"23","rd"},{"31","st"}};
			String[] item=Common.getItemOfArray(DAYFIX, day);
			String suffix="th";
			if(item!=null){
				suffix=item[1];
			}
			return suffix;
		}
		return null;
	}
	
	/**
	 * format decimal
	 * @param number numeric value
	 * @param format numeric format
	 * @param defaultValue default value
	 * @return
	 */
    public static String formatDecimal(Object number,String format,String defaultValue){
    	String str=null;
		if(!isNull(number)){
			DecimalFormat df = new DecimalFormat(format); 
			str=df.format(number);
		}
		return formatStr(str,defaultValue);
    }
    /**
	 * format decimal
	 * @param number numeric value
	 * @param format numeric format
	 * @return
	 */
    public static String formatDecimal(Object number,String format){
    	return formatDecimal(number,format,"0");
    }
	/**
	 * format money string
	 * @param money money value
	 * @param defaultValue for NULL object
	 * @param isFormat if format like 11,222.00
	 * @return
	 */
	public static String formatMoney(Double money,String defaultValue,boolean isFormat){
		String format="0.00";
		if(isFormat){
			format=",##0.00";
		}
		return formatDecimal(money,format,defaultValue);
	}
	/**
	 * format integer money string
	 * @param money money value
	 * @param defaultValue for NULL object
	 * @param isFormat if format like 11,222
	 * @return
	 */
	public static String formatIntMoney(Double money,String defaultValue,boolean isFormat){
		String format="0";
		if(isFormat){
			format=",##0";
		}
		return formatDecimal(money,format,defaultValue);
	}
	
	/**
	 * format money string
	 * @param money money value
	 * @param defaultValue for NULL object
	 * @return
	 */
	public static String formatMoney(Double money,String defaultValue){
		return formatMoney(money,defaultValue,true);
	}
	
	/**
	 * format integer money string
	 * @param money money value
	 * @param defaultValue for NULL object
	 * @return
	 */
	public static String formatIntMoney(Double money,String defaultValue){
		return formatIntMoney(money,defaultValue,true);
	}
	
	/**
	 * format money string
	 * @param money money value
	 * @return
	 */
	public static String formatMoney(Double money){
		return formatMoney(money,"0.00",true);
	}

	/**
	 * format integer money string
	 * @param money money value
	 * @return
	 */
	public static String formatIntMoney(Double money){
		return formatIntMoney(money,"0",true);
	}
	
	/**
	 * format money string
	 * @param money money value
	 * @return
	 */
    public static String formatMoney(String money,String defaultValue){
        Double m = null;
        try{
            m = Double.valueOf(money);
        }catch(Exception e){}
		return formatMoney(m,defaultValue,true);
	}
    
	/**
	 * format integer money string
	 * @param money money value
	 * @return
	 */
    public static String formatIntMoney(String money,String defaultValue){
        Double m = null;
        try{
            m = Double.valueOf(money);
        }catch(Exception e){}
		return formatIntMoney(m,defaultValue,true);
	}
    
    /**
	 * format money string
	 * @param money money value
	 * @return
	 */
    public static String formatMoney(String money){
    	return formatMoney(money,"0.00");
	}
    
    /**
	 * format money string
	 * @param money money value
	 * @return
	 */
    public static String formatIntMoney(String money){
    	return formatIntMoney(money,"0");
	}
    
    /**
     * format a decimal to percentage
     * @param num  decimal
     * @param format 
     * @return 
     */
    public static String formatPercentage(Double num, String format){
        DecimalFormat df = new DecimalFormat(format);
        return df.format(num);
    }
    
    /**
     * format a decimal to percentage with 0.00% format
     * @param num  decimal
     * @return 
     */
    public static String formatPercentage(Double num){
        return formatPercentage(num,"0.00%");
    }
    
    /**
     * big number(double) to string, it will not return the number like this:8.0E+8
     * @param num double number
     * @return
     */
    public static String bigNumberToString(double num){
    	return BigDecimal.valueOf(num).toPlainString();
    }
    
    /**
     * big number(long) to string, it will not return the number like this:8.0E+8
     * @param num long number
     * @return
     */
    public static String bigNumberToString(long num){
    	return BigDecimal.valueOf(num).toPlainString();
    }
	
	/**
	 * format long string
	 * @param str string 
	 * @param maxLen max length for every word
	 * @return
	 */
	public static String formatLongString(String str,int maxLen){
		if(str!=null){
			String[] words=str.split(" ");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<words.length;i++){
		        if(i>0)sb.append(" ");
		        sb.append(formatLongWord(words[i],maxLen));
			}
			return sb.toString();
		}else{
			return null;
		}
	}
	/**
	 * format long string
	 * @param str string 
	 * @return
	 */
	public static String formatLongString(String str){
		return formatLongString(str,0);
	}
    
    /**
	 * format long word 
	 * @param word word string 
	 * @param maxLen max length for one word
	 * @return
	 */
	public static String formatLongWord(String word,int maxLen){
		if(word!=null){
			if(maxLen>0){
				StringBuffer sb=new StringBuffer();
				while(word.length()>maxLen){
					String str=word.substring(0,maxLen);
					str=replace(str,"[,|;|/]+"," ",false,true);
					if(str.length()>maxLen){
						int index=str.indexOf(" ");
						sb.append(str.subSequence(0, index+1));
						word=word.substring(index+1);
					}else{
						sb.append(str+" ");
						word=word.substring(maxLen);
					}
				}
				sb.append(word);
				return sb.toString();
			}else{
				return replace(word,"[,|;]+"," ");
			}
		}else{
			return null;
		}
	}
	
	/**
	 * format long word 
	 * @param word word string with 45 characters
	 * @return
	 */
	public static String formatLongWord(String word){
		return formatLongWord(word,45);
	}
	
	/**
	 * escape HTML code
	 * @param code HTML code
	 * @return
	 */
	public static String escapeHTML(String code){
		if(code != null){	
			code = code.replaceAll("<", "&lt;");
			code = code.replaceAll(">", "&gt;");
		}
		return code;
	}
	
	/**
	 * return HTML code
	 * @param code escaped HTML code
	 * @return
	 */
	public static String returnHTML(String code){
		if(code != null){
			code = code.replaceAll("&lt;", "<");
			code = code.replaceAll("&gt;", ">");
		}
		return code;
	}
    
    /**
     * format HTML tag
     * @param htmlTag tag string 
     * @param removeOrAddTarget
     * @return whether remove <,>,/ or add target for link
     */
    public static String formatHTMLTag(String htmlTag, boolean removeOrAddTarget){
    	String strTag=htmlTag;
    	int index=strTag.indexOf("<");
		if(index>=0){
			strTag=strTag.substring(index+1);
		}
		index=strTag.lastIndexOf(">");
		if(index>0){
			strTag=strTag.substring(0,index);
		}
		index=strTag.lastIndexOf("/");//</div> or <input name=''/>
		boolean isLink=false, isBeginTag=true;
		if(index==0){
			strTag=strTag.substring(1);
			isBeginTag=false;
	    }else if(index==strTag.length()-1){
			strTag=strTag.substring(0,index);
		}
		if(!removeOrAddTarget && isBeginTag){
			int i_space=strTag.indexOf(" ");
			String tag_name=strTag;
			if(i_space>0){
				tag_name=strTag.substring(0,i_space);
			}
			isLink=tag_name.toLowerCase().equals("a");
		}
		if(removeOrAddTarget){
			return strTag;
		}else{
			if(isLink && !checkAttr(strTag,"target")){
				return "<"+strTag+" target='_blank'>";
			}else{
				return htmlTag;
			}
		}
    }
    
    /**
	 * check HTML tag string has attribute(attr) or not
	 * @param strTag tag string 
	 * @param attr tag attribute
	 * @return
	 */
    public static boolean checkAttr(String strTag,String attr){
    	return checkAttr(strTag,attr,false);
    }
    /**
	 * check HTML tag string has attribute(attr) or not
	 * @param strTag tag string 
	 * @param attr tag attribute
	 * @param isCheck whether or not check <,>,/
	 * @return
	 */
	public static boolean checkAttr(String strTag,String attr,boolean isCheck){
		int index;
		if(isCheck){
			strTag=formatHTMLTag(strTag,true);
		}
		String[] attrs=strTag.split("\\s+");
        for (String attr1 : attrs) {
            index = attr1.indexOf("=");
            if (index > 0) {
                if (attr1.substring(0, index).toLowerCase().equals(attr)) {
                    return true;
                }
            }
        }
		return false;
	}
    
    /**
	 * format HTML string 
	 * @param html HTML string
	 * @param addTargetForLink whether or not add target='_blank' for <a>
	 * @param isParamCheck parameter checking(remove script tag)
	 * @return
	 */
    public static String formatHTML(String html,boolean addTargetForLink,boolean isParamCheck){
    	String str=html;
    	StringBuffer sb=new StringBuffer();
    	String[] escapes={"script","body","html","link","style"};
    	int bi=html.indexOf("<");
    	int ei,ei_l,si;
    	String tag,all_tag, old_tag;
    	try{
	    	do{
	    		if(bi>0){
	    			sb.append(html.substring(0,bi));
	    			html=html.substring(bi);
	    			bi=0;
	    		}else if(bi<0){
	    			sb.append(html);
	    			break;
	    		}
	    		
	    		ei=html.indexOf(">");
	    		if(ei==-1){
	    			sb.append("&lt;"+html.substring(bi+1));
	    			break;
	    		}
	    		
	    		ei_l=html.substring(1).indexOf("<");
	    		if(ei_l>bi && ei_l<ei){
	    			ei=ei_l+1;
	    			old_tag=html.substring(bi,ei);
	    		}else{
	    			old_tag=html.substring(bi,ei+1);
	    		}
	    		all_tag=html.substring(bi+1,ei);
	    		si=all_tag.indexOf(" ");
	    		//get tag name
	    		if(si>0){
	    			tag=all_tag.substring(0,si);
	    		}else{
	    			tag=all_tag;
	    		}
	    		if(tag.length()>0 && tag.charAt(tag.length()-1)=='/'){
	    			tag=tag.substring(0,tag.length()-1);
	    		}
	    		boolean isLink=tag.toLowerCase().equals("a");
	    		//handle with end tag with '/'
	    		if(tag.length()>0 && tag.charAt(0)=='/'){
	    			tag=tag.substring(1);
	    		}
	    		
	    		if(in(escapes,tag,true)){//escape tag
	    			if(isParamCheck){//for parameter checking to filter <script>
	    				if(!tag.toLowerCase().equals(escapes[0])){
	    					sb.append(old_tag);
	    				}
	    			}else{//for simple XML content
	    				sb.append("&lt;"+all_tag+"&gt;");
	    			}
	    		}else if(addTargetForLink && isLink && !checkAttr(all_tag,"target")){//add link target
	    			sb.append("<"+all_tag+" target='_blank'>");
	    		}else{
	    			sb.append(old_tag);
	    		}
	    		
	    		if(ei_l>bi && ei_l<ei){
	    			ei=ei_l+1;
	    			html=html.substring(ei);
	    		}else{
	    			html=html.substring(ei+1);
	    		}
	    		bi=html.indexOf("<");
	    	}while(true);
    	}catch(RuntimeException e){
    		return str;
    	}
    	return sb.toString();
    }
    
    /**
	 * format HTML string 
	 * @param html HTML string
	 * @param addTargetForLink whether or not add target='_blank' for <a>
	 * @return
	 */
	public static String formatHTML(String html,boolean addTargetForLink){
		return formatHTML(html,addTargetForLink,false);
	}
    
    /**
	 * format HTML string 
	 * @param html HTML string
	 * @return
	 */
    public static String formatHTML(String html){
    	return formatHTML(html,true);
    }
    
    /**
	 * format HTML to get rid of the HTML tags except a_tags
	 * @param html  HTML string 
	 * @param a_tags tags excluded
	 * @param removed if true then removed tags else escape tags
	 * @param addTargetForLink whether or not add target='_blank' for <a>
	 * @return
	 */
	public static String formatHTML(String html,String[] a_tags, boolean removed, boolean addTargetForLink){
		if(html==null){
		    return "";
		}
    	StringBuffer sb=new StringBuffer();
    	int left_i=html.indexOf("<");
    	int right_i;
    	String str;
    	while(left_i>=0){
    		if(left_i>0){
    			String tmp=sb.toString();
				if(tmp.lastIndexOf(" ")!=tmp.length()-1){
					sb.append(" ");
				}
    			sb.append(html.substring(0, left_i));
    		}
    		right_i=html.indexOf(">");
    		if(right_i>=left_i){
    			str=html.substring(left_i,right_i+1);
    			if(include(str,a_tags)){
    				if(addTargetForLink){
    					sb.append(formatHTMLTag(str,false));
    				}else{
    					sb.append(str);
    				}
    			}else if(!removed){
    				sb.append(escapeHTML(str));
    			}
    			if(html.length()>right_i+1){
    				html=html.substring(right_i+1);
    			}else{
    				html="";
    			}
    		}else{
    			if(right_i==-1){
    				sb.append(html.substring(left_i));
    				html="";
    			}else{
    				html=html.substring(left_i);
    			}
    		}
    		left_i=html.indexOf("<");
    	}
    	sb.append(html);
    	return sb.toString();
    }
	
	/**
	 * format HTML to get rid of the HTML tags except a_tags
	 * @param html  HTML string 
	 * @param a_tags tags excluded
	 * @param removed if true then removed tags else escape tags
	 * @return
	 */
	public static String formatHTML(String html,String[] a_tags, boolean removed){
		return formatHTML(html,a_tags,removed,false);
	}
	
	/**
	 * format HTML to get rid of the HTML tags except a_tags
	 * @param html  HTML string 
	 * @param a_tags tags excluded
	 * @return
	 */
	public static String formatHTML(String html,String[] a_tags){
		return formatHTML(html,a_tags,true);
	}
	
	/**
	 * Arithmetic 1:format HTML to get rid of all the HTML tags except "<a></a>"
	 * @param html HTML string 
	 * @param len get fixed length
	 * @param showLinks if not exist "<a></a>" then whether or not show http(s) links
	 * @param removed if true then removed tags else escape tags
	 * @param addTargetForLink whether or not add target='_blank' for <a>
	 * @return
	 */
    public static String formatHTML(String html,int len,boolean showLinks, 
    		boolean removed, boolean addTargetForLink){
    	return formatHTML(html,len,showLinks,removed,addTargetForLink,0);
    }
	
	/**
	 * Arithmetic 1:format HTML to get rid of all the HTML tags except "<a></a>"
	 * @param html HTML string 
	 * @param len get fixed length
	 * @param showLinks if not exist "<a></a>" then whether or not show http(s) links
	 * @param removed if true then removed tags else escape tags
	 * @param addTargetForLink whether or not add target='_blank' for <a>
	 * @param linkMaxLen max length for link string, if it is 0 then don't format link
	 * @return
	 */
    public static String formatHTML(String html,int len,boolean showLinks, boolean removed, 
    		boolean addTargetForLink, int linkMaxLen){
    	String[] a_link={"<a ","</a>"};
    	html=formatHTML(html,a_link,removed,addTargetForLink);
    	if(showLinks && !html.toLowerCase().contains(a_link[1])){//make sure the link is right
			html=showAllLinks(html,linkMaxLen);
		}
    	if(len==0){
    		return html;
    	}else{
    		StringBuffer sb=new StringBuffer();
    		int c_len=0,str_len;
    		int left_i,right_i,end_i;
    		String str;
    		do{
    			left_i=html.indexOf(a_link[0]);
    			if(left_i==0){
        			right_i=html.indexOf(">");
        			end_i=html.indexOf(a_link[1]);
        			if(right_i>0 && end_i>right_i){
        				str=html.substring(right_i+1,end_i);
        				sb.append(html.substring(left_i,right_i+1));
        				sb.append(str);
        				sb.append(a_link[1]);
        				html=html.substring(end_i+a_link[1].length());
        				c_len+=str.length();
        				if(c_len>=len){sb.append("...");}
        			}else{
        				left_i=-1;
        			}
    			}
    			if(left_i>0 || left_i==-1){
    				if(left_i>0){
	    				str=html.substring(0, left_i);
	    				html=html.substring(left_i);
	    				str_len=left_i;
                    }else{
    					str=html;
    					html="";
    					str_len=str.length();
    				}
    				if((len-c_len)>=str_len){
    					sb.append(str);
    					c_len+=str_len;
    					if(html.length()>0 && c_len>=len){
    						sb.append("...");
    					}
    				}else{
    					sb.append(str.substring(0, len-c_len)+"...");
                        break;
    				}
        		}
    		}while(c_len<len && html.length()>0);
    		return sb.toString();
    	}
    }
    
    /**
	 * Arithmetic 1:format HTML to get rid of all the HTML tags except "<a></a>"
	 * @param html HTML string 
	 * @param len get fixed length
	 * @param showLinks if not exist "<a></a>" then whether or not show http(s) links
	 * @param removed if true then removed tags else escape tags
	 * @return
	 */
    public static String formatHTML(String html,int len,boolean showLinks,boolean removed){
    	return formatHTML(html,len,showLinks,removed,false);
    }
    
    /**
	 * Arithmetic 1:format HTML to get rid of all the HTML tags except "<a></a>"
	 * @param html HTML string 
	 * @param len get fixed length
	 * @param showLinks if not exist "<a></a>" then whether or not show http(s) links
	 * @return
	 */
    public static String formatHTML(String html,int len,boolean showLinks){
    	return formatHTML(html,len,showLinks,true);
    }
    
    /**
	 * in the sb String, there is the url which could be clickable, 
	 * such as "<a href='http://w3.tap.ibm.com' target='_blank'>http://w3.tap.ibm.com</a>"
	 * @param str string to check
	 * @return string
	 */
	public static String showAllLinks(String str){
		return showAllLinks(str,0);
	}
	
	/**
	 * Make the URL sub string be clickable in the string.
	 * such as "<a href='http://w3.tap.ibm.com' target='_blank'>http://w3.tap.ibm.com</a>"
	 * @param str string to check
	 * @param linkMaxLen max length for link string, if it is 0 then don't format link
	 * @return string
	 */
	public static String showAllLinks(String str,int linkMaxLen){
		StringBuffer sb = new StringBuffer();
		String http[]={"http://","https://"};
		String symbol[]={" ",",","<","\n","\\","\t"};
		int http_i=getIncludeIndex(str,http,true);
		int index;
		String link;
		while(http_i>=0){
			sb.append(str.substring(0,http_i));
			str=str.substring(http_i).trim();
			index=getMinIndex(str,symbol,true);
			if(index==-1)index=str.length();
			if(index>http_i){
				link=str.substring(0,index);
				String linkShow=link;
				if(linkMaxLen>0){
					linkShow=formatLongWord(link,linkMaxLen);
				}else if(linkMaxLen==0){//default 45
					linkShow=formatLongWord(link);
				}//else -1 then don't format it 
				sb.append("<a href='"+link+"' target='_blank'>"+linkShow+"</a>");
				str=str.substring(index);
			}
			http_i=getIncludeIndex(str,http,true);
		}
        sb.append(str);
        return sb.toString();
	}
	
	/**
	 * format string(format @@name etc)
	 * @param str old string
	 * @param params string parameter names array
	 * @param obj string parameter values array
	 * @return string replaced after
	 */
	public static String format(String strText, String[] params,String[] obj) {
		String str = strText;
		int i = 0;
		while (i < obj.length && obj[i]!=null && i<params.length) {
			str = str.replaceFirst("@@" + params[i], obj[i]);
			i++;
		}
		return str;
	}
	
	/**
	 * get sub string with begin index
	 * @param str string trimmed
	 * @param begin begin index
	 * @return
	 */
	public static String substring(String str,int begin){
		if(str.length()>=begin){
			return str.substring(begin);
		}else{
			return str;
		}
	}
	
	/**
	 * get sub string with begin index and end index
	 * @param str string trimmed
	 * @param begin begin index
	 * @param end  end index
	 * @return
	 */
	public static String substring(String str,int begin,int end){
		return substring(str,begin,end,false);
	}
	
	/**
	 * get sub string with begin index and end index
	 * @param str string to be trimmed
	 * @param begin begin index
	 * @param end  end index
	 * @param add_p3 add ...?
	 * @return
	 */
	public static String substring(String str,int begin,int end,boolean add_p3){
		if(str.length()>=begin){
			if(str.length()>end && end>=begin){
				if(add_p3){
					return str.substring(begin,end)+"...";
				}else{
					return str.substring(begin,end);
				}
			}else{
				return str.substring(begin);
			}
		}else{
			return str;
		}
	}
	
	/**
	 * get sub string with begin index and count trimmed
	 * @param str string trimmed
	 * @param begin begin index
	 * @param count count trimmed
	 * @return
	 */
	public static String substr(String str,int begin,int count){
		if(count>=0){
			return substring(str,begin,begin+count);
		}else{
			return str;
		}
	}
	
	/**
	 * get sub string with begin flag and end flag, for example:
	 * ("maozy@cn.ibm.com","@",".")=>cn;
	 * @param str String 
	 * @param flagBegin begin flag
	 * @param flagEnd end flag
	 * @return
	 */
	public static String substr(String str,String flagBegin, String flagEnd){
		if(str==null)return null;
		int index=str.indexOf(flagBegin);
		if(index!=-1){
			str=str.substring(index+flagBegin.length());
			index=str.indexOf(flagEnd);
			if(index!=-1){
				return str.substring(0,index);
			}
		}
		return null;
	}
	
	/**
	 * trim the simple object by parse it as String
	 * @param obj simple object such as String
	 * @return
	 */
	public static String trim(Object obj) {
		return obj==null ? null : obj.toString().trim();
	}
	
	/**
     * concat 2 string arrays
     * @param strs1 string array 1
     * @param strs2 string array 2
     * @return
     */
    public static String[] concat(String[] strs1,String[] strs2){
    	if(strs1==null){
    		if(strs2==null){
    			return null;
    		}else{
    			return strs2;
    		}
    	}else{
    		if(strs2==null){
    			return strs1;
    		}else{
    			int index=0;
    			String[] strs=new String[strs1.length+strs2.length];
    			for(int i=0;i<strs1.length;i++){
    				index=i;
    				strs[index]=strs1[i];
    			}
    			index++;
    			for(int i=0;i<strs2.length;i++){
    				index+=i;
    				strs[index]=strs2[i];
    			}
    			return strs;
    		}
    	}
    }
    
    /**
	 * shorten string to specified length
	 * @param str string to be shortened
	 * @param len the last length
	 * @return
	 */
	public static String shortStr(String str,int len){
		return shortStr(str,len,true);
    }
	
	/**
	 * shorten string to specified length
	 * @param str string to be shortened
	 * @param len the last length
	 * @param isMakeFullWord if make the word full
	 * @return
	 */
	public static String shortStr(String str,int len, boolean isMakeFullWord){
		//first,format the HTML string to a plain string
		int start=str.indexOf("<");
		int end;
		while(start>0){
			end=str.indexOf(">");
			if(end>start){
				str=str.substring(0,start)+str.substring(end+1);
				start=str.indexOf("<");
			}else{
				break;
			}
		}
		if(len>str.length()){//avoiding out of index
			len=str.length();
		}
		if (str.length()<=len) return str;
		int index;
		if(isMakeFullWord){
			index= str.indexOf(" ",len-3);
			if (index<0) return str;
		}else{
			index=len-3;
		}
		//if trimmed,then add ...
		if(str.substring(index).trim().length()>0){
			return str.substring(0, index)+"...";
		}else{
			return str;
		}
	}
	
	/**
	 * shorten file name string to specified length
	 * @param str ile name string to be shortened
	 * @param len the last length
	 * @return
	 */
	public static String shortFileName(String fileName,int len){
		int index=fileName.lastIndexOf(".");
		if(index>0){
			String ext=fileName.substring(index);
			return shortStr(fileName.substring(0,index),len-ext.length(),false)+ext;
		}else{
			return shortStr(fileName,len,false);
		}
	}
	
	/**
	 * get extension name
	 * @param name file name or path
	 * @return
	 */
	public static String getExtension(String name){
		if(name==null){
			return null;
		}else{
			int index=name.lastIndexOf(".");
			if(index>0){
				return name.substring(index+1);
			}else{
				return null;
			}
		}
	}
	
	/**
	 * Replace "\n" with "<br>"
	 * @param str free string
	 * @return HTML String
	 */
	public static String formatNewLine(String str){
		if (str!=null){
			str = str.replaceAll("\n","<br>");
		}
		return str;
	}
	
	/**
	 * replicate string 
	 * @param str   source string 
	 * @param count replicating count
	 * @return
	 */
	public static String replicate(String str,int count){
		String text="";
		for(int i=0;i<count;i++)
			text+=str;
		return text;
	}
	
	/**
	 * check the last string of strText is strLast,
	 * if it is true then return strText else return strText+strLast 
	 * @param strText original string 
	 * @param strLast last string
	 * @return
	 */
	public static String checkLastStr(String strText,String strLast){
		if(strText.indexOf(strLast)==(strText.length()-strLast.length())){
			return strText;
		}else{
			return strText+strLast;
		}
	}
	
	/**
	 * combine string like this 1,2
	 * @param str the original String
	 * @param strToAdd string to be added
	 * @param separator string separator
	 * @return
	 */
	public static String combineString(String str,String strToAdd,String separator){
		if(str==null || "".equals(str.trim())){
			return strToAdd;
		}else if(isNull(strToAdd)){
			return str;
		}else{
			return str+separator+strToAdd;
		}
	}
	
	// 精确+
	public static double add(double value1,double value2){
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.add(b2).doubleValue();
	}
	// 精确-   
	public static double sub(double value1,double value2){
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.subtract(b2).doubleValue();
	}
	// 精确*
	public static double mul(double value1,double value2){
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.multiply(b2).doubleValue();
	}
	// 精确除
	public static double div(double value1,double value2,int scale) throws IllegalAccessException{
		//如果精确范围小于0，抛出异常信息
		if(scale<0){         
		    throw new IllegalAccessException("精确度不能小于0");
		}
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.divide(b2, scale).doubleValue();    
	}

	public static void sleep() {
        /*try {
            Thread.sleep(300);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/
    }
}
