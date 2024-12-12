package PetStoreTests;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiController {

    RequestSpecification requestSpecification = given();

    public ApiController() {
        defaultParser = Parser.JSON;
        this.requestSpecification.contentType(ContentType.JSON);
        this.requestSpecification.accept(ContentType.JSON);
        this.requestSpecification.filter(new AllureRestAssured());
    }

    public Response get(String endpoint, String token) {
        return step("Get " + baseURI + endpoint, () -> given(this.requestSpecification)
                .header("api_key", token)
                .get(endpoint)
                .then().log().body()
                .extract().response());
    }

    public Response post(String endpoint, String token, Object body) {
        return step("Post " + baseURI + endpoint, () -> given(this.requestSpecification)
                .header("api_key", token)
                .body(body)
                .post(endpoint)
                .then().log().body()
                .extract().response());
    }

    public Response put(String endpoint, String token, Object body) {
        return step("Put " + baseURI + endpoint, () -> given(this.requestSpecification)
                .header("api_key", token)
                .body(body)
                .put(endpoint)
                .then().log().body()
                .extract().response());
    }

    public Response delete(String endpoint, String token) {
        return step("Delete " + baseURI + endpoint, () -> given(this.requestSpecification)
                .header("api_key", token)
                .delete(endpoint)
                .then().log().body()
                .extract().response());
    }

}