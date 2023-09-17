package stepdefinitions;

import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pages.HomePage;
import pages.RegisterPage;
import utilities.Driver;
import utilities.JDBCUtils;
import utilities.ReusableMethods;

import java.sql.*;
import java.util.List;

import static base_urls.ManagementOnSchoolsBaseUrl.setSpec;
import static base_urls.ManagementOnSchoolsBaseUrl.spec;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
import static utilities.JSUtils.clickElementByJS;

public class US01_RegisterStepDefs {
    RegisterPage registerPage = new RegisterPage();
    HomePage homePage = new HomePage();
    private Connection connection;
    private ResultSet resultSet;
    private static String fakePhoneNumber;
    private static String fakeSsn;
    private static String fakeUsername;
    Response response;


    @Given("go to the {string}")
    public void goToThe(String url) {

        Driver.getDriver().get(url);

    }


    @Given("click on register")
    public void click_on_register() {

        homePage.registerLink.click();

    }


    @When("Enter name {string}, surname {string},birth_place {string}, phone_number {string},gender {string}, birth_day {string},ssn {string},username {string},password{string}")
    public void enterNameSurnameBirth_placePhone_numberGenderBirth_daySsnUsernamePassword(String name, String surname, String birth_place, String phone_number, String gender, String birth_day, String ssn, String username, String password) {

        Faker faker = new Faker();

        registerPage.nameInput.sendKeys(name);
        registerPage.surNameInput.sendKeys(surname);
        registerPage.birthPlaceInput.sendKeys(birth_place);

        fakePhoneNumber = faker.number().numberBetween(100, 999) + " " + faker.number().numberBetween(100, 999) + " " + faker.number().numberBetween(1000, 9999);
        //System.out.println("fakePhoneNumber = " + fakePhoneNumber);
        registerPage.phoneNumberInput.sendKeys(fakePhoneNumber);

        if (gender.equalsIgnoreCase("male")) {
            clickElementByJS(registerPage.maleRadioButton);
        } else {
            clickElementByJS(registerPage.femaleRadioButton);
        }

        registerPage.birthDay.sendKeys(birth_day);

        fakeSsn = faker.idNumber().ssnValid();
        //System.out.println("fakeSsn = " + fakeSsn);
        registerPage.ssnInput.sendKeys(fakeSsn);

        fakeUsername = faker.name().username();
        //System.out.println("fakeUsername = " + fakeUsername);
        registerPage.userNameInput.sendKeys(fakeUsername);

        registerPage.passwordInput.sendKeys(password);
    }

    @Then("click on register button")
    public void click_on_register_button() throws InterruptedException {

        Thread.sleep(2000);
        RegisterPage registerPage = new RegisterPage();
        clickElementByJS(registerPage.registerButton);
        ReusableMethods.waitForVisibility(registerPage.alertMessage, 5);
        assertTrue(registerPage.alertMessage.getText().contains("Guest User registered"));

    }

    @Then("close browser")
    public void closeBrowser() {

        Driver.getDriver().quit();

    }

    @Given("connect to database")
    public void connect_to_database() throws SQLException {

        //connection = DriverManager.getConnection("jdbc:postgresql://managementonschools.com:5432/school_management", "select_user", "43w5ijfso");
        //The connectio will be created when we call executeQuery() method from JDBCUtils class.

    }

    @When("get guest user via username {string}")
    public void get_guest_user_via_username(String username) throws SQLException {

        //Statement statement = connection.createStatement();

        String query = "select * from guest_user where username = '" + fakeUsername + "'";

        resultSet = JDBCUtils.executeQuery(query);
        resultSet.next();//To move the pointer to the records, we need to call next()

    }

    @Then("validate  username {string} birth_day {string}   birth_place {string}  gender {string}  name {string} phone_number {string}  ssn {string} surname {string}")
    public void validate_username_birth_day_birth_place_gender_name_phone_number_ssn_surname(String username, String birth_day, String birth_place, String gender, String name, String phone_number, String ssn, String surname) throws SQLException {

        String actualUsername = resultSet.getString("username");
        String actualBirth_day = resultSet.getString("birth_day");
        String actualBirth_place = resultSet.getString("birth_place");
        String actualGender = resultSet.getString("gender");
        String actualName = resultSet.getString("name");
        String actualPhone_number = resultSet.getString("phone_number");
        String actualSsn = resultSet.getString("ssn");
        String actualSurname = resultSet.getString("surname");

        assertEquals(fakeUsername, actualUsername);//fakeUsername will be generated on UI part and will be validated here
        assertEquals(birth_day, actualBirth_day);
        assertEquals(birth_place, actualBirth_place);
        assertEquals(gender, actualGender);
        assertEquals(name, actualName);
        assertEquals(fakePhoneNumber, actualPhone_number);
        assertEquals(fakeSsn, actualSsn);
        assertEquals(surname, actualSurname);


    }

    @Then("close the connection")
    public void close_the_connection() throws SQLException {

//        resultSet.close();
//        connection.close();
        JDBCUtils.closeConnection();

    }


    @When("get guest user via non-existing username {string}")
    public void getGuestUserViaNonExistingUsername(String username) {

        String query = "select * from guest_user where username = '" + username + "'";
        resultSet = JDBCUtils.executeQuery(query);


    }

    @Then("validate  if the user is deleted")
    public void validateIfTheUserIsDeleted() throws SQLException {

        assertFalse(resultSet.next());//Since the resultSet is empty, next() method must return false

    }

    @Given("send get request to get all guest users")
    public void send_get_request_to_get_all_guest_users() {
        //String url = "https://managementonschools.com/app/guestUser/getAll?size=10000";
        //setSpec(); //--> This method must be called before api tests. We will call this before api test on hooks class
        spec.pathParams("first", "guestUser", "second", "getAll").queryParams("size", "10000");

        response = given(spec).get("{first}/{second}");

        response.prettyPrint();


    }

    @Then("validate  username {string} birth_day {string}   birth_place {string}  gender {string}  name {string} phone_number {string}  ssn {string} surname {string} by API")
    public void validate_username_birth_day_birth_place_gender_name_phone_number_ssn_surname_by_api(String username, String birthDay, String birthPlace, String gender, String name, String phoneNumber, String ssn, String surname) {


        JsonPath jsonPath = response.jsonPath();
        String actUsername = jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.username").get(0).toString();
        String actSsn = jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.ssn").get(0).toString();
        String actName = jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.name").get(0).toString();
        String actSurname = jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.surname").get(0).toString();
        String actBirthDay= jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.birthDay").get(0).toString();
        String actBirthPlace= jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.birthPlace").get(0).toString();
        String actPhoneNumber= jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.phoneNumber").get(0).toString();
        String actGender= jsonPath.getList("content.findAll{it.username=='"+fakeUsername+"'}.gender").get(0).toString();

        assertEquals(200, response.statusCode());
        assertEquals(fakeUsername, actUsername);
        assertEquals(fakeSsn, actSsn);
        assertEquals(name, actName);
        assertEquals(surname, actSurname);
        assertEquals(birthDay, actBirthDay);
        assertEquals(birthPlace, actBirthPlace);
        assertEquals(fakePhoneNumber, actPhoneNumber);
        assertEquals(gender, actGender);


    }

    @Then("validate user {string} is deleted")
    public void validateUserIsDeleted(String username) {

     List<Object> objectList = response.jsonPath().getList("content.findAll{it.username=='"+username+"'}");
     assertTrue(objectList.isEmpty());

    }
}
