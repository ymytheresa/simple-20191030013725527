package com.boc.cloud.api.oauth;

import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.entity.User;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

/**
 * Authentication URL for IBM API Connect OAuth2 (grant_type = access code)
 *
 * @author cyper
 * @see https://www.ibm.com/support/knowledgecenter/SSFS6T/com.ibm.apic.toolkit.doc/con_auth_url.html
 */
@RestController
public class AuthenticationAPI extends BaseRestApi {

    @ApiOperation(value = "Authenticate user", notes = "Authenticate user", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 401, message = "Unauthorized")})
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<?> authenticateUser(@RequestHeader("authorization") String authString,
                                              HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        System.out.println("authString=" + authString);
        String token = authString.trim().substring("basic ".length());
        String auth = new String(Base64.getDecoder().decode(token));
        String[] parts = auth.split(":");
        String username = parts[0];
        String password = parts[1];

        User query = new User();
        System.out.println("username=" + username);
        System.out.println("password=" + password);
        query.setUsername(username);
        query.setPassword(password);
        List<User> users = db.findByIndex(getSelector(query), User.class);
        System.out.println("users.size()=" + users.size());
        // user does not exist
        if (users.size() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            User user = users.get(0);
            HttpHeaders headers = new HttpHeaders();

            headers.add("API-Authenticated-Credential", user.getUsername());
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }

    }

}
