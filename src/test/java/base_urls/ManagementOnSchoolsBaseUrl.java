package base_urls;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static utilities.ManagementOnSchoolsAuthentication.generateToken;

public class ManagementOnSchoolsBaseUrl {

    public static RequestSpecification spec;//null

    public static void setSpec(){
        spec = new RequestSpecBuilder()
                .setBaseUri("https://managementonschools.com/app")
                .addHeader("Authorization", generateToken())
                .build();
    }


}
