package com.boc.cloud.api.utils;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cloudant db factory to support multiple database for different user groups.
 * 
 * @author cyper
 *
 */
@Component
public class CloudantDbFactoryBean implements FactoryBean<Database> {
    private static final String DEFAULT_DB_KEY = "default";

    final static Logger logger = LoggerFactory.getLogger(CloudantDbFactoryBean.class);

    private Map<String, Database> cache = new ConcurrentHashMap<>();

    private CloudantClient client;

    private String customerName;

    @Value("${cloudant.account}")
    private String account;

    @Value("${cloudant.username}")
    private String username;

    @Value("${cloudant.password}")
    private String password;

    @Value("${cloudant.dbname}")
    private String dbname;

    @PostConstruct
    public void initClient() {
        //this.client = ClientBuilder.account(account).username(username).password(password).build();
    }

    @Override
    public Database getObject() throws Exception {
        logger.info("get db instance for customer {}", customerName);

        if (customerName == null || !customerName.matches("cust\\d{4}")) {
            if (!cache.containsKey(DEFAULT_DB_KEY)) {
                logger.info("create a new db instance for default.");

                //Database db = client.database(this.dbname, true);
                //cache.put(DEFAULT_DB_KEY, db);
            }

            return cache.get(DEFAULT_DB_KEY);

        } else {
            String calculatedDbName = getDbName(customerName);
            if (!cache.containsKey(customerName)) {
                logger.info("create a new db instance for {}", customerName);
                logger.info("db name is {}", calculatedDbName);

                Database db = client.database(calculatedDbName, true);
                cache.put(customerName, db);
            }

            return cache.get(customerName);
        }
    }

    /**
     * 根据cutomerName动态计算出对应的group.
     * 
     * @param customerName
     * @return
     */
    private String getDbName(String customerName) {
        return String.format("bocapis_%s", customerName.substring(4, 6));
    }

    @Override
    public Class<?> getObjectType() {
        return Database.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
