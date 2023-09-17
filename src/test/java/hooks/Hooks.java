package hooks;

import io.cucumber.java.Before;

import static base_urls.ManagementOnSchoolsBaseUrl.setSpec;

public class Hooks {
    //This class must be declared in Runner class

    @Before("@Api_Test")//import io.cucumber.java.Before;
    //This will run before each @Api_Test
    public void beforeApi(){

        setSpec();

    }

}
