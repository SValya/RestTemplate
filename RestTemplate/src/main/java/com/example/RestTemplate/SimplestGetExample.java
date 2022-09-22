package com.example.RestTemplate;

import com.example.RestTemplate.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SimplestGetExample {
    static final String URL_USERS = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        String resultCode;
        String cookie = getUsers();
        // Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор. В случае успеха вы получите первую часть кода.
        Long id = 3L;
        Byte age = 9;
        User user = new User(id,"James", "Brown", age);
        resultCode = postUser(user, cookie);
        System.out.println("*=*=*=*=*=*  RESULT CODE  *=*=*=*=*=*");
        System.out.println(resultCode);
    }

    public static String postUser(User user, String cookies) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookies);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        System.out.println("Cookie:  " + entity.getHeaders());
        System.out.println(entity);
        System.out.println("==========  Method POST  ===========");
        String result1 = restTemplate.exchange(URL_USERS, HttpMethod.POST, entity, String.class).getBody();
        System.out.println(result1);
        System.out.println("==========  Method GET  ===========");
        System.out.println(restTemplate.exchange(URL_USERS, HttpMethod.GET, entity, String.class).getBody());
        System.out.println("==========  Method PUT  ===========");
        user.setName("Thomas");
        user.setLastName("Shelby");
        String result2 = restTemplate.exchange(URL_USERS, HttpMethod.PUT, entity, String.class).getBody();
        System.out.println(result2);
        System.out.println("==========  Method DELETE  ===========");
        String result3 = restTemplate.exchange("http://94.198.50.185:7081/api/users/3", HttpMethod.DELETE, entity, String.class).getBody();
        System.out.println(result3);
        assert result1 != null;
        assert result2 != null;
        assert result3 != null;
        return result1.concat(result2).concat(result3);

    }


    public static String getUsers() {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity<String>: To get result as String.
        HttpEntity<User[]> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<User[]> response = restTemplate.exchange(URL_USERS, HttpMethod.GET, entity, User[].class);

        HttpStatus statusCode = response.getStatusCode();

        HttpHeaders h = response.getHeaders();
        String set_cookie = h.getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("header: " + h );
        System.out.println("cookie: " + set_cookie);
        System.out.println("get(\"Set-Cookie\"): " + h.get("Set-Cookie"));

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            User[] list = response.getBody();

            if (list != null) {
                for (User e : list) {
                    System.out.println("User: " + e);
                }
            }
        }

        return set_cookie;
    }
}
