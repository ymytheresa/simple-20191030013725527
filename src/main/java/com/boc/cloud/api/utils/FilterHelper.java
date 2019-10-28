package com.boc.cloud.api.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {
    private static final Logger logger = LoggerFactory.getLogger(FilterHelper.class);

    private FilterHelper() {
        // do nothing
    }

    public static String upperCase(String str, int firstCount) {
        if (str != null && firstCount <= str.length()) {
            return str.substring(0, firstCount).toUpperCase() + str.substring(firstCount);
        }
        return str;
    }

    public static String upperCase(String str) {
        return upperCase(str, 1);
    }

    /**
     * get object value for a bean with a field/property name, for example:b.getValue()
     *
     * @param bean bean object
     * @param field field/property name
     * @return
     */
    public static Object getValue(Object bean, String field) {
        try {
            int idx = field.indexOf('.');
            String prop = field;
            if (idx > 0) {
                prop = field.substring(0, idx);
                if (!prop.contains("()")) {// use get property function to get object
                    prop = "get" + upperCase(prop) + "()";
                }
            }
            int methodIdx = prop.indexOf("()");
            Object object=null;
            if (methodIdx > 0) {
                object = MethodUtils.invokeMethod(bean, prop.substring(0, methodIdx));
            } else if(bean!=null){
                object = PropertyUtils.getProperty(bean, prop);
            }
            if (idx > 0) {
                return getValue(object, field.substring(idx + 1));
            } else {
                return object;
            }
        } catch (Exception e) {
        	System.out.println("Did not find the property named "+field);
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Check if the field values matched with the given values
     *
     * @param bean any Object
     * @param fields specified fields for filter
     * @param values given values for filter
     * @return
     */
    public static boolean isMatched(Object bean, String[] fields, String[] values) {
        boolean isMatched = true;
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i] != null && values[i] != null) {
                    String value = String.valueOf(getValue(bean, fields[i]));
                    if (!values[i].equals(value)) {
                        isMatched = false;
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return isMatched;
    }

    /**
     * get the new list by filter if the field values matched with the given values
     *
     * @param list any Object list
     * @param fields specified fields for filter
     * @param values given values for filter
     * @return
     */
    public static<T> List<T> filterList(List<T> list, String[] fields, String[] values) {
        if (list == null || fields == null || values == null || fields.length != values.length) {
            return list;
        }
        List<T> newList = new ArrayList<>();
        for (T bean : list) {
            if (isMatched(bean, fields, values)) {
                newList.add(bean);
            }
        }
        return newList;
    }

    /**
     * get the new list by filter if the field value matched with the given value
     *
     * @param list any Object list
     * @param field specified field for filter
     * @param value given value for filter
     * @return
     */
    public static<T> List<T> filterList(List<T> list, String field, String value) {
        return filterList(list, new String[] {field}, new String[] {value});
    }

    /**
     * get first index if the field values matched with the given values
     *
     * @param list any Object list
     * @param fields specified fields for filter
     * @param values given values for filter
     * @return
     */
    public static<T> int getFirstIndex(List<T> list, String[] fields, String[] values) {
        if (list == null || fields == null || values == null || fields.length != values.length) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (isMatched(list.get(i), fields, values)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get first index if the field value matched with the given value
     *
     * @param list any Object list
     * @param field specified field for filter
     * @param value given value for filter
     * @return
     */
    public static<T> int getFirstIndex(List<T> list, String field, String value) {
        return getFirstIndex(list, new String[] {field}, new String[] {value});
    }

    public static<T> T getFirstObject(List<T> list, String[] fields, String[] values) {
        int idx = getFirstIndex(list, fields, values);
        if (list != null && idx >= 0) {
            return list.get(idx);
        }
        return null;
    }

    public static<T> T getFirstObject(List<T> list, String field, String value) {
        return getFirstObject(list, new String[] {field}, new String[] {value});
    }

    /**
     * get index for a String list by filter the value
     *
     * @param list any String list
     * @param value given value for filter
     * @return
     */
    public static int getIndex(List<String> list, String value) {
        if (list == null || value == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (value.equals(list.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
