package app.movie.com.movieapp.web.interceptors;


import android.support.annotation.NonNull;

import java.io.IOException;

import app.movie.com.movieapp.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    /** Query param name */
    private static final String QUERY_NAME = "apikey";

    /** Major constructor */
    public ApiKeyInterceptor() {

    }

    /** Add api key as a parameter on every call to the API */
    public static Request addParams(@NonNull final Request original) throws IOException {
        final HttpUrl originalHttpUrl = original.url();
        final HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter(QUERY_NAME, BuildConfig.API_KEY)
                .build();

        return original.newBuilder().url(url).build();
    }

    @Override
    public Response intercept(@NonNull final Interceptor.Chain chain) throws IOException {
        return chain.proceed(addParams(chain.request()));
    }
}

