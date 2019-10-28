package com.boc.cloud.api.init;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.exception.Http401Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.utils.Common;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;

@RestController
public class UtilityApi extends BaseRestApi {
    @Value("${cloudant.account}")
    private String account;

    @Value("${cloudant.username}")
    private String username;

    @Value("${cloudant.password}")
    private String password;

    @ApiOperation(value = "Reset database", notes = "Reset database", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "confirmationRequired: You must provide confirmation")})
    @RequestMapping(value = "/internal/reset-db", method = RequestMethod.POST)
    public String initdb(@RequestBody @ApiParam(value = "Input (JSON)") ResetDBInput input,
                         HttpServletRequest request) throws Exception {
        String customer = this.getCustomer(request);
        if (customer == null || !customer.matches("cust\\d{4}")) {
            throw new Http401Exception(MsgKeys.ERROR_AUTH_CODE_GRANT_REQUIRED, MsgKeys.GRANT_TYPE_AUTH_CODE_REQUIRED);
        }

        String del_db_name = "bocapis_" + customer.substring(4, 6);

        if (input.getAre_you_sure() == null) {
            throw new Http400Exception(MsgKeys.ERROR_CONFIRMATION_REQUIRED, MsgKeys.NOT_EXIST, "are_you_sure");
        }
        if (input.getAre_you_sure().equals("YES")) {
            CloudantClient client = ClientBuilder.account(account).username(username).password(password).build();
            System.out.println("All my databases : ");
            List<String> databases = client.getAllDbs();
            if (databases.contains(del_db_name)) {
                Common.sleep();
                client.deleteDB(del_db_name);
                Common.sleep();
                client.createDB(del_db_name);
            } else {
                Common.sleep();
                client.createDB(del_db_name);
            }
            Database db = client.database(del_db_name, true);
            StringBuffer sb = new StringBuffer();
            URL url = UtilityApi.class.getResource("/newdata");
            if (url == null) {
                System.out.println("******url is null");
            }
            String csvFilePath = url.getPath();
            Collection<File> listFiles = FileUtils.listFiles(new File(csvFilePath), FileFilterUtils.suffixFileFilter("csv"), null);
            for (File file : listFiles) {
                String fileName = file.getName();
                System.out.println("Importing: " + fileName);
                String entityName = file.getName().substring(0, file.getName().indexOf("."));
                try {
                    sb.append("Bulking data for " + fileName + "...");
                    Class clazz = Class.forName("com.boc.cloud.entity." + entityName);
                    // clear table
//					List oldDataList = db.findByIndex(getSelector4All(clazz), clazz);
//					for (Object object : oldDataList) {
//						db.remove(object);
//					}
                    //insert
                    List<Object> readList = InitData.readData(file, clazz);
                    Common.sleep();
                    db.bulk(readList);
                    sb.append("Bulk data for " + fileName + "--done").append("</br>");
                } catch (Exception e) {
                    sb.append("Bulk data for " + fileName + "--error,message:" + e.getMessage()).append("</br>");
                    e.printStackTrace();
                }
            }
            return new Gson().toJson("Database reset successfully");
        } else {
            return new Gson().toJson("Confirmation of \"YES\" is not specified in the request to reset database");
        }
    }


    @ApiModel
    public static class ResetDBInput {

        @ApiModelProperty(value = "Are you sure to reset the database to factory default?", required = true, example = "NO")
        private String are_you_sure;

        public String getAre_you_sure() {
            return are_you_sure;
        }

        public void setAre_you_sure(String are_you_sure) {
            this.are_you_sure = are_you_sure;
        }
    }

}
