package PetStoreTests;

import PetStoreTests.models.*;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static PetStoreTests.Objects.*;
import static io.restassured.RestAssured.baseURI;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.*;

@Feature("Тестирование API PetStore")
public class PetStoreTests {

    private static final User user = newUser();
    private static final Pet pet = newPet();
    private static final Order order = newOrder();
    private static final String token = "special-key";
    private static String toDelete = "";

    ApiController controller = new ApiController();

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(user, user.getUsername(), "/user", "пользователя"),
                Arguments.of(pet, String.valueOf(pet.getId()), "/pet", "питомца"),
                Arguments.of(order, String.valueOf(order.getId()), "/store/order", "заказа")
        );
    }

    @BeforeAll
    public static void setUp() {baseURI = "https://petstore.swagger.io/v2";}

    @AfterEach
    public void cleanUp() {
        if (!toDelete.isEmpty()) controller.delete(toDelete, token);
        toDelete = "";
    }

    @ParameterizedTest(name = "Создание {3}")
    @org.junit.jupiter.api.Tag("post")
    @MethodSource("data")
    public void post(Object body, String param, String endpoint, String subject) {
        Response response = controller.post(endpoint, token, body);
        assertEquals(200, response.getStatusCode(), "\nОшибка: создание нового " + subject + " провалено!\n");
        System.out.println("Создание нового " + subject + " прошло успешно!");
        response = controller.get(endpoint + "/" + param, token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено созданного " + subject + "!\n");
        System.out.println("Данные созданного " + subject + " получены с сервера!");
        toDelete = endpoint + "/" + param;
        String result = (endpoint.equals("/user")) ? response.jsonPath().getString("username") : response.jsonPath().getString("id");
        assertEquals(param, result, "\nОшибка: полученные данные не соответствуют ожидаемым!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        System.out.println("Тест пройден!");
    }

    @ParameterizedTest(name = "Удаление {3}")
    @org.junit.jupiter.api.Tag("delete")
    @MethodSource("data")
    public void delete(Object body, String param, String endpoint, String subject) {
        Response response = controller.post(endpoint, token, body);
        assertEquals(200, response.getStatusCode(), "\nОшибка: создание нового " + subject + " провалено!\n");
        System.out.println("Создание нового " + subject + " прошло успешно!");
        response = controller.get(endpoint + "/" + param, token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено созданного " + subject + "!\n");
        System.out.println("Данные созданного " + subject + " получены с сервера!");
        toDelete = endpoint + "/" + param;
        String result = (endpoint.equals("/user")) ? response.jsonPath().getString("username") : response.jsonPath().getString("id");
        assertEquals(param, result, "\nОшибка: полученные данные не соответствуют ожидаемым!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        response = controller.delete(endpoint + "/" + param, token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: удаление " + subject + " провалено!\n");
        System.out.println("Удаление " + subject + " прошло успешно!");
        response = controller.get(endpoint + "/" + param, token);
        assertEquals(404, response.getStatusCode(), "\nОшибка: данные " + subject + " остались на сервере!\n");
        System.out.println("Данные " + subject + " отсутствуют на сервере!");
        toDelete = "";
        System.out.println("Тест пройден!");
    }

    @Test
    @org.junit.jupiter.api.Tag("put")
    @DisplayName("Обновление пользователя")
    public void putUser() {
        Response response = controller.post("/user", token, user);
        assertEquals(200, response.getStatusCode(), "\nОшибка: создание нового пользователя провалено!\n");
        System.out.println("Создание нового пользователя прошло успешно!");
        response = controller.get("/user/" + user.getUsername(), token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено созданного пользователя!\n");
        System.out.println("Данные созданного пользователя получены с сервера!");
        toDelete = "/user/" + user.getUsername();
        assertEquals(String.valueOf(user.getId()), response.jsonPath().getString("id"), "\nОшибка: полученные данные не соответствуют ожидаемым!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        String userId = String.valueOf(user.getId());
        User updatedUser = user;
        long newId;
        do {newId = current().nextLong(1001, 2147483647);} while (newId == user.getId());
        updatedUser.setId(newId);
        response = controller.put("/user/" + user.getUsername(), token, updatedUser);
        assertEquals(200, response.getStatusCode(), "\nОшибка: обновление пользователя провалено!\n");
        System.out.println("Обновление пользователя прошло успешно!");
        response = controller.get("/user/" + user.getUsername(), token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено обновлённого пользователя!\n");
        System.out.println("Данные обновлённого пользователя получены с сервера!");
        assertNotEquals(userId, response.jsonPath().getString("id"), "\nОшибка: данные не были обновлены на сервере!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        System.out.println("Тест пройден!");
    }

    @Test
    @org.junit.jupiter.api.Tag("put")
    @DisplayName("Обновление питомца")
    public void putPet() {
        Response response = controller.post("/pet", token, pet);
        assertEquals(200, response.getStatusCode(), "\nОшибка: создание нового питомца провалено!\n");
        System.out.println("Создание нового питомца прошло успешно!");
        String param = String.valueOf(pet.getId());
        response = controller.get("/pet/" + param, token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено созданного питомца!\n");
        System.out.println("Данные созданного питомца получены с сервера!");
        toDelete = "/pet/" + param;
        assertEquals(String.valueOf(pet.getId()), response.jsonPath().getString("id"), "\nОшибка: полученные данные не соответствуют ожидаемым!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        String petName = pet.getName();
        Pet updatedPet = pet;
        updatedPet.setName("updatedName");
        response = controller.put("/pet", token, updatedPet);
        assertEquals(200, response.getStatusCode(), "\nОшибка: обновление питомца провалено!\n");
        System.out.println("Обновление питомца прошло успешно!");
        response = controller.get("/pet/" + param, token);
        assertEquals(200, response.getStatusCode(), "\nОшибка: не найдено обновлённого питомца!\n");
        System.out.println("Данные обновлённого питомца получены с сервера!");
        assertNotEquals(petName, response.jsonPath().getString("name"), "\nОшибка: данные не были обновлены на сервере!\n");
        System.out.println("Полученные данные соответствуют отправленным!");
        System.out.println("Тест пройден!");
    }

}