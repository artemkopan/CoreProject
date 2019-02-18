package io.facebook.stetho.okhttp3;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class StethoInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}