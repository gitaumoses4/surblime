package com.mg.surblime.api;

import android.content.Context;
import android.media.Image;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mg.surblime.forms.FormContext;
import com.mg.surblime.forms.fields.FormField;
import com.mg.surblime.forms.fields.ImageField;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Moses Gitau on 3/25/18 at 4:13 PM.
 */

public abstract class SurblimeAPI<Q extends SurblimeAPI<?>> {

    private final String[] DATE_FORMATS;
    private final Context context;
    private GsonBuilder gsonBuilder;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder okHttpClient;
    private boolean useCache = true;
    private Q self;
    private HashMap<String, String> includeQueryParameters = new HashMap<>();

    public SurblimeAPI(Context context, Class<Q> selfClass) {
        this.context = context;
        gsonBuilder = new GsonBuilder();
        self = selfClass.cast(this);
        DATE_FORMATS = this.getDateFormats();
        gsonBuilder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Interceptor interceptor = getRetrofitInterceptor();
        createGsonBuilder(gsonBuilder);
        if (DATE_FORMATS.length > 0) {
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        }

        gsonBuilder.registerTypeHierarchyAdapter(FormContext.class, new FormContextSerializer());
        okHttpClient = new OkHttpClient.Builder();
        if (interceptor != null) {
            okHttpClient.addInterceptor(interceptor);
        }
        okHttpClient.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            for (Map.Entry<String, String> entry : includeQueryParameters.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            request = request.newBuilder().url(urlBuilder.build()).build();
            return chain.proceed(request);
        });

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(getBaseURL())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
        this.retrofitBuilder = retrofitBuilder;
    }

    public SurblimeAPI(Context context, Class<Q> selfClass, boolean useCache) {
        this(context, selfClass);
        setUseCache(useCache);
    }

    public void addQueryParameter(String key, String value) {
        includeQueryParameters.put(key, value);
    }

    public Context getContext() {
        return context;
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public OkHttpClient.Builder getOkHttpClient() {
        return okHttpClient;
    }

    public abstract Interceptor getRetrofitInterceptor();

    public abstract void createGsonBuilder(GsonBuilder gsonBuilder);

    public abstract String getBaseURL();

    public abstract Class<?> getRetrofitInterface();

    public Q setUseCache(boolean useCache) {
        this.useCache = useCache;
        return self;
    }

    public String[] getDateFormats() {
        return new String[]{};
    }

    private class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }

    private class FormContextSerializer implements JsonSerializer<FormContext<?, ?>> {

        @Override
        public JsonElement serialize(FormContext<?, ?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            for (String key : src.keys()) {
                FormField field = src.fields.get(key);
                if (field instanceof ImageField) {
                    ImageField imageField = (ImageField) field;
                    if (imageField.isSingle()) {
                        ArrayList<String> images = imageField.get();
                        jsonObject.add(key, context.serialize(images == null || images.isEmpty() ? "" : images.get(0)));
                        continue;
                    }
                }
                jsonObject.add(key, context.serialize(src.fields.get(key).get()));
            }
            return jsonObject;
        }
    }

    public boolean isUseCache() {
        return useCache;
    }
}
