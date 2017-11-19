package app.movie.com.movieapp.injection.modules;

import android.support.annotation.NonNull;

import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import app.movie.com.movieapp.BuildConfig;
import app.movie.com.movieapp.injection.scopes.PerApp;
import app.movie.com.movieapp.web.clients.MovieAppClient;
import app.movie.com.movieapp.web.data.Movie;
import app.movie.com.movieapp.web.interceptors.ApiKeyInterceptor;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class WebServiceModule {

    /** In seconds. Timeout of connection operation. */
    private static final int TIMEOUT_CONNECT = 15;
    /** In seconds. Timeout of read operation. */
    private static final int TIMEOUT_READ = 15;
    /** In seconds. Timeout of write operation. */
    private static final int TIMEOUT_WRITE = 15;

    @PerApp
    @Provides
    @NonNull
    OkHttpClient providesOkHttp(@NonNull final ApiKeyInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @PerApp
    @Provides
    @NonNull
    ApiKeyInterceptor providesApiKeyInterceptor() {
        return new ApiKeyInterceptor();
    }

    @PerApp
    @Provides
    @NonNull
    Moshi providesMoshi() {
        return new Moshi.Builder()
//                .add(new Movie())
                .build();
    }

    @PerApp
    @Provides
    @NonNull
    Retrofit providesAuthRetrofit(@NonNull final OkHttpClient client, @NonNull final Moshi moshi) {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .build();
    }

    @PerApp
    @Provides
    @NonNull
    MovieAppClient providesWebService(@NonNull final Retrofit retrofit) {
        return retrofit.create(MovieAppClient.class);
    }
}
