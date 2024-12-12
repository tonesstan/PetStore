package PetStoreTests;

import PetStoreTests.models.*;
import io.restassured.path.json.JsonPath;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.ThreadLocalRandom.current;

public class Objects {

    public static User newUser() {
        JsonPath response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when().get("https://randomuser.me/api")
                .then().statusCode(200).log().all()
                .extract().jsonPath();

        User user = new User();
        user.setId(current().nextLong(1001, 2147483647));
        user.setUsername(response.getString("results[0].login.username"));
        user.setFirstName(response.getString("results[0].name.first"));
        user.setLastName(response.getString("results[0].name.last"));
        user.setEmail(response.getString("results[0].email"));
        user.setPassword(response.getString("results[0].login.password"));
        user.setPhone(response.getString("results[0].cell"));
        user.setUserStatus(1);
        return user;
    }

    public static Pet newPet() {
        Pet pet = new Pet();
        pet.setId(current().nextLong(1001, 2147483647));
        Category category = new Category();
        category.setId(current().nextLong(1001, 2147483647));
        category.setName("Test Category");
        pet.setCategory(category);
        pet.setPhotoUrls(null);
        Tag tag = new Tag();
        tag.setId(current().nextLong(1001, 2147483647));
        tag.setName("Test Tag");
        pet.setTags(List.of(tag));
        pet.setName("Test Pet");
        pet.setStatus("available");
        return pet;
    }

    public static Order newOrder() {
        Order order = new Order();
        order.setId(current().nextLong(1001, 2147483647));
        order.setPetId(current().nextLong(1001, 2147483647));
        order.setQuantity(current().nextInt(1, 10));
        order.setShipDate();
        order.setStatus("placed");
        order.setComplete(true);
        return order;
    }

}
