package spotify.example;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import spotify.example.domain.Artista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by andvicente on 11/10/16.
 */
public class SpotifyTest {

    @Before
    public void antesTestes(){
        RestAssured.baseURI = "https://api.spotify.com";
        RestAssured.basePath = "/v1/";
    }

    /**
     * Procurar por um artista no Spotify
     * @throws Exception
     */
    @Test
    public void shouldReturnColdPlay() throws Exception {

        Response response =
        given().
                auth().oauth2("").
                accept(ContentType.JSON).
                queryParam("q", "Coldplay").
                queryParam("type", "artist").
        when().
                get("search").
        then().
                //log().
                //body().
                statusCode(200).
                body("artists.items.name[0]",equalTo("Coldplay")).
        extract().
                response();

        List<HashMap> respArtistas = response.jsonPath().getList("artists.items");
        procurarArtista(respArtistas,"Coldplay");
    }

    /**
     * Listar as top tracks de um artista e verificar se uma determinada música está na lista
     * Top Tracks: https://api.spotify.com/v1/artists/{id}/top-tracks
     */
    @Test
    public void shouldReturn10topTracksOfPinkFloyd(){
        Response response =
                given().
                        auth().oauth2("").
                        accept(ContentType.JSON).
                        queryParam("q", "Pink Floyd").
                        queryParam("type", "artist").
                        when().
                        get("search").
                then().
                        statusCode(200).
                        body("artists.items.name[0]",equalTo("Pink Floyd")).
                extract().
                        response();

        List<HashMap> respArtistas = response.jsonPath().getList("artists.items");
        List<Artista> artistasEncontrados = procurarArtista(respArtistas,"Pink Floyd");
        String idArtista = artistasEncontrados.get(0).getId();

        Response responseTopTracks =
                given().
                        accept(ContentType.JSON).
                        pathParameter("id",idArtista).
                        queryParam("country","BR").
                when().
                        get("artists/{id}/top-tracks").
                then().
                        //log().
                        //body().
                        statusCode(200).
                        body("tracks",hasSize(10)).
                        body("tracks.name",hasItem("Money")).
                 extract().
                        response();

        List<String> musicas = responseTopTracks.jsonPath().getList("tracks.name");
        musicas.forEach(System.out::println);

    }

    /**
     * Retorna Playlists de um Usuario do Spotify
     * URL API: https://api.spotify.com/v1/users/{user_id}/playlists
     */
    @Test
    public void shouldReturnPlaylistsOfUser(){

        Response responsePlaylistsOfUser =
                given().
                        auth().oauth2("").
                        accept(ContentType.JSON).
                        pathParameter("user_id","andvicente").
                when().
                        get("users/{user_id}/playlists").
                then().
                        //log().
                        //body().
                        statusCode(200).
                        body("items.name",hasItem("Wedding Rock and Classics")).
                extract().
                        response();

                List <HashMap> playlists = responsePlaylistsOfUser.jsonPath().getList("items");

        printPlaylistsCreatedByUser(playlists,"andvicente");
    }

    /**
     * Verificar se uma Playlist tem determinadas musicas
     * https://developer.spotify.com/web-api/console/get-playlist-tracks/
     */
    @Test
    public void playlistHasTracks(){

    }

    private void printPlaylistsCreatedByUser(List<HashMap> playlists, String user) {
        List <String> playlistsOfUser = new ArrayList<>();
        for(HashMap playlist : playlists){
            String playlistName = playlist.get("name").toString();
            HashMap playlistOwner = (HashMap) playlist.get("owner");
            String idOwner = (String) playlistOwner.get("id");
            if (idOwner.equals(user))
                playlistsOfUser.add(playlistName);
        }
        playlistsOfUser.forEach(System.out::println);
    }


    public List<Artista> procurarArtista(List<HashMap> respArtistas, String artistaProcurado){
        List<Artista> artistas = new ArrayList<>();

        for (HashMap respArtista : respArtistas) {
            Artista artista = new Artista();
            if(artistaProcurado.equals(respArtista.get("name").toString())) {
                artista.setName(respArtista.get("name").toString());
                artista.setId(String.valueOf(respArtista.get("id")));
                artista.setPopularity((Integer) respArtista.get("popularity"));
                artistas.add(artista);
            }
        }

        for (Artista artista : artistas){
            System.out.println("ID: " + artista.getId());
            System.out.println("Artista: " + artista.getName());
            System.out.println("Popularidade: " + artista.getPopularity());
        }

        return artistas;

    }


}
