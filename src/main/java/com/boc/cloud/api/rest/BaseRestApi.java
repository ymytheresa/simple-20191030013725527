package com.boc.cloud.api.rest;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.oauth.AuthenticationAPI;
import com.boc.cloud.api.utils.CloudantDbFactoryBean;
import com.boc.cloud.api.utils.FilterHelper;
import com.boc.cloud.api.utils.Seq;
import com.boc.cloud.entity.User;
import com.cloudant.client.api.Database;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public abstract class BaseRestApi {
    private final static Logger logger = LoggerFactory.getLogger(BaseRestApi.class);

    @Autowired
    private CloudantDbFactoryBean dbFactory;

    /**
     * 是否爲開發模式, 開發模式方便在本机测试, 不走OAuth流程, customer的信息直接配置在application*.properties裏.
     */
    @Value("${dev}")
    private boolean isDev;

    /**
     * customer格式: cust0102
     *
     * @see User
     * @see AuthenticationAPI
     */
    @Value("${customer}")
    private String customer;

    /**
     * 根據請求動態分配database, 如果請求中不含customer信息, 則使用application.properties中配置的dbname.
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected Database getDb(HttpServletRequest request) throws Exception {
        String customer = getCustomer(request);
        System.out.println("customer=" + customer);
        dbFactory.setCustomerName(customer);
        return dbFactory.getObject();
    }

    protected String getQuery(String key, String value, String comparator) {
        if (comparator == null) {
            comparator = "$eq";
        }
        if (key != null && value != null) {
            return "\"" + key + "\": {\"" + comparator + "\": \"" + value + "\"}";
        }
        return "";
    }

    protected String getQuery(String key, String value) {
        return getQuery(key, value, null);
    }

    public String getSelector(Object obj) {
        String selector = "{\"selector\": {";
        selector += getQuery("type", obj.getClass().getSimpleName());
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(obj);
        for (PropertyDescriptor pd : pds) {
            Object value = FilterHelper.getValue(obj, pd.getName());
            if (!"class".equals(pd.getName()) && value != null) {
                selector += "," + getQuery(pd.getName(), String.valueOf(value));
            }
        }
        selector += "}}";
        return selector;
    }

    /**
     * Get next JNLNO.
     *
     * @return
     * @throws Exception
     */
    protected synchronized long getSeqNo(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Seq query = new Seq();
        List<Seq> seqs = db.findByIndex(getSelector(query), Seq.class);

        // if no records in SEQ table.
        if (seqs.size() == 0) {
            Seq seq = new Seq();
            seq.setNext_val("2");
            db.save(seq);
            return 1;

        } else {
            // if exists, return next seq and increment its value by 1.
            Seq seq = seqs.get(0);
            String next_val = seq.getNext_val();
            seq.setNext_val(String.valueOf(Long.valueOf(next_val) + 1));
            db.update(seq);
            return Long.valueOf(next_val);
        }

    }

    /**
     * 以標準格式返回當前日期時間.
     *
     * @return
     */
    protected String now() {
        return formatDate(new Date());
    }

    /**
     * 以標準格式返回當前日期.
     *
     * @return
     */
    protected String nowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static void checkInputDate(String input) {
        try {
            OffsetDateTime.parse(input);
        } catch (Exception e) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_DATETIME, MsgKeys.IS_INVALID, "datetime");
        }
    }

    /**
     * 将date转成標準格式字符串.
     *
     * @param date 日期
     * @return
     */
    protected static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 从ibm api connect gateway给出的请求頭中得到customer.
     *
     * @param request
     * @return
     */
    public String getCustomer(HttpServletRequest request) {
        if (isDev) {
            logger.info("in Dev mode.");
            return customer;
        }

        logger.debug("\n========= http headers from gateway ========= ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            System.out.println(key + "=" + value);
            logger.debug(key + ": " + value);
        }
        String ibmAppUser = request.getHeader("ibm-app-user");
        System.out.println("ibmAppUser=" + ibmAppUser);
        return ibmAppUser;
    }

    protected String getCustomerId(HttpServletRequest request) {
        String customer = getCustomer(request);
        if (customer == null || !customer.matches("cust\\d{4}")) {
            // 不需要customer的場合, 比如Bank, Market, Appointment
            return null;
        } else {
            // 需要customer的場合. cust0101
            return customer.substring(0, 4) + customer.substring(6);
        }
    }

}
