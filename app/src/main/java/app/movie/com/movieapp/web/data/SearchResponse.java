package app.movie.com.movieapp.web.data;


import com.squareup.moshi.Json;

import java.util.List;

/**
 * Response class for doing a search request to the given API
 */
public class SearchResponse {
    @Json(name = "Search") public List<Movie> movies;
}
