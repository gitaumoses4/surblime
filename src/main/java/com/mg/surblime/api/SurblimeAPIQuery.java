package com.mg.surblime.api;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.gson.GsonBuilder;
import com.mg.surblime.BaseModel;
import com.mg.surblime.util.Tools;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moses on 5/3/18.
 */

public abstract class SurblimeAPIQuery<T> implements Callback<T>, Observer<T> {

    private static final String CACHE_DIRECTORY = "surblime_api_cache";
    protected final Class<T> errorClass;
    private SurblimeAPI surblimeAPI;

    public enum CacheMode {
        MODE_CACHE_ON, MODE_CACHE_OFF
    }

    static final String TAG = SimpleQuery.class.getSimpleName();

    private Context context;
    private Class retrofitInterface;
    private Class parameterTypes[];
    private Object args[];
    private String methodName;
    private Retrofit.Builder retrofit;
    private Retrofit retrofitInstance;
    private OkHttpClient.Builder okHttpClient;
    private ArrayList<String> methodNames = new ArrayList<>();
    private HashMap<String, Pair<Class<?>[], Object[]>> methods = new HashMap<>();
    private CacheMode cacheMode = CacheMode.MODE_CACHE_ON;

    public SurblimeAPIQuery(Class<T> errorClass) {
        this.errorClass = errorClass;
    }

    public SurblimeAPIQuery<T> with(SurblimeAPI surblimeAPI) {
        this.surblimeAPI = surblimeAPI;
        this.context = surblimeAPI.getContext();
        this.retrofit = surblimeAPI.getRetrofitBuilder();
        this.retrofitInterface = surblimeAPI.getRetrofitInterface();
        this.okHttpClient = surblimeAPI.getOkHttpClient();
        this.cacheMode = surblimeAPI.isUseCache() ? CacheMode.MODE_CACHE_ON : CacheMode.MODE_CACHE_OFF;
        return this;
    }

    public SurblimeAPIQuery<T> addQueryParameter(String key, String value) {
        surblimeAPI.addQueryParameter(key, value);
        return this;
    }

    public SurblimeAPIQuery<T> invoke(String apiMethodName, Class... parameterTypes) {
        this.methodName = apiMethodName;
        this.parameterTypes = parameterTypes;
        return this;
    }

    public SurblimeAPIQuery<T> args(Object... args) {
        this.args = args;
        return this;
    }

    public SurblimeAPIQuery<T> subscribe(String apiMethodName, Class<?>[] parameterTypes, Object... args) {
        methods.put(apiMethodName, Pair.create(parameterTypes, args));
        methodNames.add(apiMethodName);
        return this;
    }

    public SurblimeAPIQuery<T> cacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }

    public Retrofit getRetrofit() {
        return retrofitInstance;
    }

    @SuppressWarnings("unchecked")
    public void execute() {
        checkNonNull(retrofitInterface, "Api Class cannot be null");
        checkNonNull(methodName, "Method name cannot be null");
        if (parameterTypes != null && args != null && parameterTypes.length != args.length) {
            throw new IllegalArgumentException("Number of parameter types not equal to number of argument types!");
        }
        try {
            Method method = retrofitInterface.getMethod(methodName, parameterTypes);

            okHttpClient.addInterceptor(getCacheInterceptor());

            retrofit.client(okHttpClient.build());
            this.retrofitInstance = retrofit.build();
            Object s = retrofitInstance.create(retrofitInterface);
            Call<T> call = (Call<T>) method.invoke(s, args);
            call.enqueue(this);
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), "execute", ex);
        }
    }

    private String checkCache(String url) {
        File file = new File(context.getCacheDir() + File.separator + CACHE_DIRECTORY + File.separator + Tools.md5(url));
        if (file.exists()) {
            try {
                return Tools.readFromFile(file);
            } catch (IOException ignore) {
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void zip(final Zipper<T> zipper) {
        ArrayList<Observable<?>> observables = new ArrayList<>();
        checkNonNull(retrofitInterface, "Api Class cannot be null");
        for (String methodName : methodNames) {
            Class<?> parameterTypes[] = methods.get(methodName).first;
            Object[] args = methods.get(methodName).second;

            checkNonNull(methodName, "Method name cannot be null");
            if (parameterTypes != null && args != null && parameterTypes.length != args.length) {
                throw new IllegalArgumentException("Number of parameter types not equal to number of argument types!");
            }
            try {
                Method method = retrofitInterface.getMethod(methodName, parameterTypes);

                okHttpClient.addInterceptor(getCacheInterceptor());
                retrofit.client(okHttpClient.build());
                this.retrofitInstance = retrofit.build();
                Object s = this.retrofitInstance.create(retrofitInterface);
                Observable observable = (Observable) method.invoke(s, args);
                observables.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()));
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), "execute", ex);
            }
        }
        Observable<T> combined = Observable.zip(observables, objects -> zipper.combine(objects));
        combined.subscribe(this);
    }

    protected void checkNonNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    protected T getError(Response<T> response) {
        Converter<ResponseBody, T> converter =
            getRetrofit()
                .responseBodyConverter(
                    errorClass, new Annotation[0]
                );
        try {
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            Log.e("SurblimeAPI", "Error Builder", e);
        }
        return null;
    }

    public interface Zipper<T> {
        T combine(Object[] objects);
    }

    public Interceptor getCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            String result = checkCache(request.url() + "");
            if (cacheMode == CacheMode.MODE_CACHE_ON && result != null && result.split("\n").length == 5) {

                String[] results = result.split("\n");
                ResponseBody responseBody = ResponseBody.create(MediaType.parse(results[3]), results[4]);
                return new okhttp3.Response.Builder().body(responseBody)
                    .protocol(Protocol.get(results[0]))
                    .code(Integer.parseInt(results[1]))
                    .message(results[2])
                    .request(request)
                    .build();
            } else {
                okhttp3.Response response = chain.proceed(request);
                Protocol protocol = response.protocol();
                int code = response.code();
                String message = response.message();

                ResponseBody responseBody = response.body();
                String body = responseBody != null ? responseBody.string() : "";
                String contentType = responseBody == null ? "" : (responseBody.contentType() == null ? "" : responseBody.contentType().toString());

                String encrypted = Tools.md5(request.url() + "");

                File directory = new File(context.getCacheDir() + File.separator + CACHE_DIRECTORY + File.separator);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(directory.getAbsolutePath() + File.separator + encrypted);
                if ((cacheMode == CacheMode.MODE_CACHE_OFF || !file.exists()) && !body.isEmpty()) {
                    try {
                        String output = protocol.toString() + "\n";
                        output += code + "\n";
                        output += message + "\n";
                        output += contentType + "\n";
                        output += body;
                        Tools.saveToFile(output, file, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                responseBody = ResponseBody.create(MediaType.parse(contentType), body);
                return new okhttp3.Response.Builder().body(responseBody)
                    .protocol(protocol)
                    .code(code)
                    .message(message)
                    .request(request)
                    .build();
            }
        };
    }
}
