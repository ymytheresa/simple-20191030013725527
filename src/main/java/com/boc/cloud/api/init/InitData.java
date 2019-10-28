package com.boc.cloud.api.init;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.entity.Atm;
import com.boc.cloud.entity.Branch;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitData {

    private static Logger logger = LoggerFactory.getLogger(InitData.class);

    public static List<String> splitCSV(String txt) {
        String reg = "\\G(?:^|,)(?:\"([^\"]*+(?:\"\"[^\"]*+)*+)\"|([^\",]*+))";
        Matcher matcherMain = Pattern.compile(reg).matcher("");
        Matcher matcherQuoto = Pattern.compile("\"\"").matcher("");
        matcherMain.reset(txt);
        List<String> strList = new ArrayList<>();
        while (matcherMain.find()) {
            String field;
            if (matcherMain.start(2) >= 0) {
                field = matcherMain.group(2);
            } else {
                field = matcherQuoto.reset(matcherMain.group(1)).replaceAll("\"");
            }
            strList.add(field);
        }
        return strList;
    }

    public static List<Object> readData(File file, Class clazz) throws Exception {
        List<String> readLines = FileUtils.readLines(file, "UTF-8");
        String fileName = file.getName();
        String entityName = fileName.substring(0, fileName.indexOf("."));
        int i = 0;
        List<Object> objects = new ArrayList<>();
        String[] names = null;
        for (String lineTxt : readLines) {
            List<String> strList = splitCSV(lineTxt);
            i++;// line number
            if (i == 1) {// column names
                names = new String[strList.size()];
                strList.toArray(names);
                for (int j = 0; j < names.length; j++) {
                    names[j] = GenerateEntities.formatField(names[j]);
                }
                continue;
            }
            String[] values = new String[strList.size()];
            strList.toArray(values);

            Object obj;
            obj = clazz.newInstance();
            PropertyUtils.setProperty(obj, "type", entityName);
            for (int k = 0; k < values.length; k++) {
                if (k < names.length) {
                    String name = names[k], value = values[k] == null ? null : values[k].trim();
                    formateValue(entityName, obj, name, value);
                }
            }
            objects.add(obj);
        }
        return objects;
    }

    private static void formateValue(String entityName, Object obj, String name, String value)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        switch (entityName) {
            case "Atm":
                if (name.equals("address_coordinates")) {
                    if (StringUtils.isNotBlank(value)) {
                        Atm.AddressCoordinates address_coordinate = new Atm.AddressCoordinates();
                        address_coordinate.setLatitude(Double.parseDouble(value.split(",")[0]));
                        address_coordinate.setLongitude(Double.parseDouble(value.split(",")[1]));
                        PropertyUtils.setProperty(obj, name, address_coordinate);
                    }
                } else if (name.equals("services")) {
                    if (StringUtils.isNotBlank(value)) {
                        String[] temp = value.split(",");
                        PropertyUtils.setProperty(obj, name, temp);
                    }
                } else {
                    PropertyUtils.setProperty(obj, name, value);
                }
                break;
            case "Branch":
                if (name.equals("address_coordinates")) {
                    if (StringUtils.isNotBlank(value)) {
                        Branch.AddressCoordinates address_coordinate = new Branch.AddressCoordinates();
                        address_coordinate.setLatitude(Double.parseDouble(value.split(",")[0]));
                        address_coordinate.setLongitude(Double.parseDouble(value.split(",")[1]));
                        PropertyUtils.setProperty(obj, name, address_coordinate);
                    }
                } else if (name.equals("services")) {
                    if (StringUtils.isNotBlank(value)) {
                        String[] temp = value.split(",");
                        PropertyUtils.setProperty(obj, name, temp);
                    }
                } else if (name.equals("opening_hours")) {
                    if (StringUtils.isNotBlank(value)) {
                        Branch.OpeningHours hour = new Branch.OpeningHours();
                        hour.setMon2fri(value.split(",")[0]);
                        hour.setSat(value.split(",")[1]);
                        hour.setSunholiday(value.split(",")[2]);
                        PropertyUtils.setProperty(obj, name, hour);
                    }
                } else {
                    PropertyUtils.setProperty(obj, name, value);
                }
                break;
            case "Invest_Txn":
            case "Appointment":
                if (name.equals("date_time") || name.equals("datetime")) {
                    if (StringUtils.isNotBlank(value)) {
                        PropertyUtils.setProperty(obj, name, formatDate(value, "yyyyMMdd_HHmm"));
                    }
                } else {
                    PropertyUtils.setProperty(obj, name, value);
                }
                break;
            case "Account_Txn":
            case "Credit_Card_Txn":
                if (name.equals("datetime") || name.equals("open_date")) {
                    if (StringUtils.isNotBlank(value)) {
                        PropertyUtils.setProperty(obj, name, formatDate(value, "dd/MM/yyyy HH:mm"));
                    }
                } else {
                    PropertyUtils.setProperty(obj, name, value);
                }
                break;
            case "Credit_Card":
            case "Personal_Loan":
                if (name.equals("open_date")) {
                    if (StringUtils.isNotBlank(value)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(value);
                        } catch (ParseException e) {
                            throw new Http400Exception(MsgKeys.ERROR_INVALID_DATE, MsgKeys.IS_INVALID, "日期");
                        }
                        PropertyUtils.setProperty(obj, name, new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                } else {
                    PropertyUtils.setProperty(obj, name, value);
                }
                break;
            default:
                PropertyUtils.setProperty(obj, name, value);
                break;
        }
    }

    private static String formatDate(String strDate, String pattern) {
        Date date;
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_DATE, MsgKeys.IS_INVALID, "日期");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return sdf.format(date);
    }

    public static void main(String[] args) {
        String str = "code,districtCode,address,gps,opening,status,services";
        System.out.println("program ---------------------- start");
        List<String> strList = splitCSV(str);
        System.out.println("program ---------------------- start strList");
        for (String str1 : strList) {
            System.out.println(str1);
        }
        System.out.println("----");
    }
}
