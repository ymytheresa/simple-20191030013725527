package com.boc.cloud.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class Swagger2 {
    private Set<String> typeSet = new HashSet<>();

    public Swagger2() {
        typeSet.add("application/json");
    }

    @Bean
    public Docket createBankInfoRestApi() {
        String group = "Bank Information";
        String desc = "Give BOCHK customers and potential customers a quick summary of bank branches and ATMs.";

        return buildDocket(group, desc, "/api/bank-info/.*");
    }

    @Bean
    public Docket createMarketInfoRestApi() {
        String group = "Market Information";
        String desc = "Obtain common market data easily.";

        return buildDocket(group, desc, "/api/market-info/.*");
    }

    @Bean
    public Docket createCustomerRestApi() {
        String group = "Customer Operations";
        String desc = "Gain access to shared BOCHK customer profile data.";

        return buildDocket(group, desc, "/api/customer/.*");
    }

    @Bean
    public Docket createAccountsRestApi() {
        String group = "Account Operations";
        String desc = "Allow BOCHK customers to manipulate their accounts and perform financial transactions in an innovative way.";

        return buildDocket(group, desc, "/api/accounts.*");
    }

    @Bean
    public Docket createCreditCardRestApi() {
        String group = "Card Operations";
        String desc = "Enable BOCHK customers to pay the merchants by credit cards.";

        return buildDocket(group, desc, "/api/credit-cards.*");
    }

    @Bean
    public Docket createPersonalLoansRestApi() {
        String group = "Personal Loans";
        String desc = "Expose our quick personal loan services via APIs for even more convenience.";

        return buildDocket(group, desc, "/api/personal-loans.*");
    }

    @Bean
    public Docket createInvestmentsRestApi() {
        String group = "Investments";
        String desc = "Open up a whole new world of opportunities for automated stock trading.";

        return buildDocket(group, desc, "/api/investments.*");
    }

    @Bean
    public Docket createAppointmentsRestApi() {
        String group = "Appointments";
        String desc = "Enable BOCHK customers to reach our team for professional services.";

        return buildDocket(group, desc, "/api/services/appointments.*");
    }

    @Bean
    public Docket createInternalRestApi() {
        // Hackathon Self-Service

        String group = "Internal";
        String desc = "Reset the API backend database to the factory default version.";

        return buildDocket(group, desc, "com.boc.cloud.api.init", "/internal/.*");
    }

    private Docket buildDocket(String title, String desc, String url) {
        return buildDocket(title, desc, "com.boc.cloud.api.rest", url);
    }

    private Docket buildDocket(String title, String desc, String basePackage, String url) {
        return new Docket(DocumentationType.SWAGGER_2).consumes(typeSet).produces(typeSet).groupName(title)
            .apiInfo(buildApiInfo(title, desc)).select().apis(RequestHandlerSelectors.basePackage(basePackage))
            .paths(PathSelectors.regex(url)).build();
    }

    private ApiInfo buildApiInfo(String title, String desc) {
        return new ApiInfoBuilder().title(title).description(desc).termsOfServiceUrl("")
            .contact(new Contact("FinTech Hackathon", "https://www.bochkhackathon.com", "it_innovation_lab@bochk.com"))/*.version("1.0.0")*/.build();
    }
}
