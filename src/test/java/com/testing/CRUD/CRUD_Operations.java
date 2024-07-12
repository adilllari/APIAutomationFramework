package com.testing.CRUD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class CRUD_Operations {
    /*========================================================================================================================*/
    //Here we'll declare all Global Variables and Instantiations if required below
    static RequestSpecification rs = RestAssured.given();
    static Response response;
    static ValidatableResponse validatableResponse;

    /*========================================================================================================================*/

    @Test
    public static void crudOperations() {

        String base_Url = "https://rahulshettyacademy.com";
        String add_resourse = "/maps/api/place/add/json";
        String get_resourse = "/maps/api/place/get/json";
        String update_resourse = "/maps/api/place/update/json";
        String delete_resourse = "/maps/api/place/delete/json";


        rs.baseUri(base_Url).basePath(add_resourse).queryParam("key", "qaclick123").contentType(ContentType.JSON).body(Payloads.addPlace());

        //Here we are adding the Place using POST method..
        response = rs.when().log().all().post();

        String responseString = response.asString();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);
        validatableResponse.header("Server", "Apache/2.4.52 (Ubuntu)");

        JsonPath jsonPath = new JsonPath(responseString);
        String placeId = jsonPath.getString("place_id");
        System.out.println(placeId);// to Print the PlaceId

        /*_______________________________________________________________________________________________________________________________________________________________*/

        //Update Place
        //Now we'll update some fields in above added Place...here we'll use PUT method to update this.
        String updateThisAddress = "Galaxy Apartments, Pune";
        rs.baseUri(base_Url).basePath(update_resourse).log().all().queryParam("key", "qaclick123").contentType(ContentType.JSON);
        rs.body("{\n" +
                "\"place_id\":\"" + placeId + "\",\n" +
                "\"address\":\"" + updateThisAddress + "\",\n" +
                "\"key\":\"qaclick123\"\n" +
                "}");

        //Here we are Updating the Place using PUT method..
        response = rs.when().log().all().put();


        validatableResponse = response.then().log().all().statusCode(200);

        validatableResponse.assertThat().body("msg", equalTo("Address successfully updated"));

        /*_______________________________________________________________________________________________________________________________________________________________*/

        //GET PLACE
        rs.baseUri(base_Url).basePath(get_resourse).log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId);
        //using GET method to retrieve the existing place
        response = rs.when().log().all().get();
        String responseString2 = response.asString();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        System.out.println("This is String response: " + responseString2);

        JsonPath js = new JsonPath(responseString2);
        String actualAddress = js.getString("address");
        System.out.println(actualAddress);
        Assert.assertEquals(updateThisAddress, actualAddress, "Validating Address..");


        /*_______________________________________________________________________________________________________________________________________________________________*/


    }
}
