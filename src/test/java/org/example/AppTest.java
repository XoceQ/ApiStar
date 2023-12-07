package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AppTest {
    private String api;

    @BeforeClass
    public void setUp(){
        api = "https://swapi.dev/api/";
    }

    @Test
    public void getMethod() throws JsonProcessingException {
        Response response;
        response = given().get(api+"people/2/");

        ObjectMapper objectMapper = new ObjectMapper();
        PeoplePojo persona = objectMapper.readValue(response.asString(), PeoplePojo.class);


        String skinColor = persona.getSkin_color();
        int filmsCount = persona.getFilms().length;


        System.out.println(skinColor + " - " +  filmsCount + " - " + persona.getEye_color());

        org.testng.Assert.assertEquals("gold",skinColor);
        org.testng.Assert.assertEquals(6, filmsCount);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    /*The second test is testing the functionality related to obtaining information about a specific movie in which the character obtained in the first part of the test appears. Here is the breakdown of the test:

    Performs a GET request to the API to get information about a specific character, in this case, the character with index 2 (person/2).
    It uses the ObjectMapper object to map the obtained JSON response to an object of the PeoplePojo class representing the character.
            It then fetches the second element of the array films from the PeoplePojo object, which contains the URL of the second movie in which the character appears.
    It makes a new GET request to the API using the URL of the film obtained in the previous step.
    Uses another ObjectMapper object to map the obtained JSON response to an object of the FilmPojo class representing the movie.
    Performs several checks using the FilmPojo object to make sure the movie meets certain conditions, such as having a list of characters, planets, vehicles and species with more than one element and verifying that the release date is in the correct format.
*/

    @Test
    public void test2() throws JsonProcessingException {
        Response response;
        Response responseFilm;
        response = given().get(api+"people/2/");

        ObjectMapper objectMapper = new ObjectMapper();
        PeoplePojo person = objectMapper.readValue(response.asString(), PeoplePojo.class);

        String movie2 = person.getFilms()[1];

        responseFilm = given().get(movie2);
        ObjectMapper objectMapperFilm = new ObjectMapper();
        FilmPojo film = objectMapperFilm.readValue(responseFilm.asString(), FilmPojo.class);



        Assert.assertTrue(film.getCharacters().length >= 1);
        Assert.assertTrue(film.getPlanets().length >= 1);
        Assert.assertTrue(film.getVehicles().length >= 1);
        Assert.assertTrue(film.getSpecies().length >= 1);

        Assert.assertTrue(film.getRelease_date().charAt(4) == '-' &&
                film.getRelease_date().charAt(7) == '-' &&
                Integer.parseInt(film.getRelease_date().substring(5,7))<=12 &&
                Integer.parseInt(film.getRelease_date().substring(8,9))<=31);

    }


}