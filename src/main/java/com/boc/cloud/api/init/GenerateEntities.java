package com.boc.cloud.api.init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

public class GenerateEntities {
	public static String srcFolder;
	static{
		URL url=GenerateEntities.class.getResource("/newdata");
		srcFolder=url.getPath();
	}
	public static String desFolder="/Users/sword/git/BoCAPIs/src/main/java/com/boc/cloud/entity/";
	public static String packageName="com.boc.cloud.entity";
	
	public static void generateEntity(File file) throws Exception{
		String fileName=file.getName().substring(0,file.getName().indexOf("."));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String lineTxt;
        if ((lineTxt = br.readLine()) != null) {
            String[] names = lineTxt.split(",");
            generateEntity(fileName,names);
        }
        br.close();
	}
	
	public static void generateEntity(String entityName, String[] fields) throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("package "+packageName+";\n");
		sb.append("public class "+entityName+" extends BaseEntity{\n");
		//generateField("type",sb);
		for (String name : fields) {
        	generateField(formatField(name),sb);
        }
		sb.append("}\n");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(desFolder+entityName+".java")),"UTF-8"));
        bw.write(sb.toString());
        bw.close();
	}
	
	public static void generateField(String name,StringBuffer sb) throws Exception{
		String upperName=name.substring(0,1).toUpperCase()+name.substring(1);
	    if("key".equals(name)) {
	        sb.append("\t@JsonIgnore\n");
	    }
		sb.append("	private String "+name+";\n");
		sb.append("	public String get"+upperName+"(){\n");
		sb.append("		return "+name+";\n");
		sb.append("	}\n");
		sb.append("	public void set"+upperName+"(String "+name+"){\n");
		sb.append("		this."+name+"="+name+";\n");
		sb.append("	}\n");
	}
	
	public static String formatField(String name){
		/*if(name.indexOf("code")>=0 && name.length()==5) name="code";//fix invalid char for code
		if(name!=null && (name.matches("^[A-Z\\d-]+$") || name.toLowerCase().indexOf("key")>=0)){
			name=name.toLowerCase();
			String[] strs=name.split("-");
			name="";
			for(int i=0;i<strs.length;i++){
				if(i>0){
					name+=strs[i].substring(0, 1).toUpperCase()+strs[i].substring(1);
				}else{
					name+=strs[i];
				}
			}
		}*/
		return name;
	}
	
	public static void main(String[] args) throws Exception{
		File dir=new File(srcFolder);
		File[] files=dir.listFiles();
		for(File file:files){
			//if(file.getName().indexOf("Loan")<0) continue;
			generateEntity(file);
			System.out.println("Generated file named "+file.getName());
		}
		//System.out.println(formatField("TX-CODE"));
	}
}
