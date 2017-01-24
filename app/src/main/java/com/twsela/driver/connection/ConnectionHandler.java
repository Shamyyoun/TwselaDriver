package com.twsela.driver.connection;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.twsela.driver.utils.Utils;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

public class ConnectionHandler<T> {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    private static final String LOG_TAG = "ConnectionHandler";
    private int timeout = 30 * 1000; // Ion's default timeout is 30 seconds.
    private Context context;
    private String url;
    private Class<?> cls;
    private ConnectionListener<T> listener;
    private String tag = ""; // default value to avoid null pointer exception
    private Map<String, List<String>> headers;
    private Map<String, List<String>> params;
    private Map<String, File> files;
    private Object body;

    private Future<Response<String>> future;
    private long startTime, finishTime;
    private Gson gson;
    private String requestMethod;

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls) {
        init(context, url, cls, null, null);
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener) {
        init(context, url, cls, listener, "");
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, String tag) {
        init(context, url, cls, null, tag);
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, String tag) {
        init(context, url, cls, listener, tag);
    }

    private void init(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, String tag) {
        this.context = context;
        this.url = url;
        this.cls = cls;
        this.listener = listener;
        if (Utils.isNullOrEmpty(tag)) {
            this.tag = url;
        } else {
            this.tag = tag;
        }
        gson = new Gson();
    }


    private void printLogs(int level) {
        String tag;
        if (this.tag != null) {
            tag = "[" + this.tag + "]-[" + requestMethod + "] ";
        } else {
            tag = "[" + url + "]-[" + requestMethod + "]\n";
        }

        switch (level) {
            case 0:
                startTime = System.currentTimeMillis();
                logE(tag + "request started. time=" + Calendar.getInstance().getTime()
                        + "\nUrl: " + url);
                break;
            case 1:
                finishTime = System.currentTimeMillis();
                logE(tag + "request finished and parsing started. time=" + Calendar.getInstance().getTime()
                        + ", Time diff: " + (finishTime - startTime) + " MS");
                break;

        }
    }

    private void logParams() {
        if (params != null) {
            for (String key : params.keySet()) {
                Log.e(LOG_TAG, "Request Parameter: " + key + "=" + params.get(key));
            }
        }
    }

    private void logHeaders() {
        if (headers != null) {
            for (String key : headers.keySet()) {
                Log.e(LOG_TAG, "Request Header: " + key + "=" + headers.get(key));
            }
        }
    }

    /**
     * Execute get request. (requires url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeGet() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            requestMethod = METHOD_GET;
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(context)
                    .load(url)
                    .setTimeout(timeout);

            if (headers != null) {
                logHeaders();
                ionBuilder.addHeaders(headers);
            }

            future = ionBuilder.asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute post request
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executePost() {
        return executeMethod(METHOD_POST);
    }

    /**
     * Execute put request
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executePut() {
        return executeMethod(METHOD_PUT);
    }

    /**
     * Execute delete request
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeDelete() {
        return executeMethod(METHOD_DELETE);
    }

    /**
     * Execute request with passed method, requires at least a url
     * If headers found it will be added. If params found it will be added
     *
     * @return Future object for cancelling the request.
     */
    private Future<Response<String>> executeMethod(String method) {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            requestMethod = method;
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(context)
                    .load(requestMethod, url)
                    .setTimeout(timeout);

            if (headers != null) {
                logHeaders();
                ionBuilder.addHeaders(headers);
            }

            if (params != null) {
                logParams();
                ionBuilder.setBodyParameters(params);
            }

            future = ionBuilder.asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute a multipart post request with text and image parameters.
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeMultiPartPost() {
        if (url == null || (params == null && files == null)) {
            throw new IllegalArgumentException("No url, params or files found.");
        } else {
            requestMethod = METHOD_POST;
            printLogs(0);

            Builders.Any.B ionBuilder =
                    Ion.with(context)
                            .load(requestMethod, url)
                            .setTimeout(timeout);

            if (headers != null) {
                logHeaders();
                ionBuilder.addHeaders(headers);
            }

            if (params != null) {
                logParams();
                ionBuilder.setMultipartParameters(params);
            }

            for (String key : files.keySet()) {
                Log.e(LOG_TAG, "Multipart Parameter: " + key + "=" + files.get(key).getAbsolutePath());
                ionBuilder.setMultipartFile(key, "image/jpeg", files.get(key));
            }

            future = ionBuilder.asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute raw post request with json parameters.
     * (requires at least url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeRawJson() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            requestMethod = "RawJson";
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(context)
                    .load(url)
                    .setTimeout(timeout)
                    .addHeader("content-type", "application/json");

            if (body != null) {
                String bodyJson = gson.toJson(body);
                Log.e(LOG_TAG, "Request Body: " + bodyJson);
                ionBuilder.setJsonPojoBody(body);
            }

            future = ionBuilder.asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Handles the completion of the request if (success or fail) and deserialize the string response using the given Class object and executes the  onFail or onSuccess method.
     *
     * @param e      the exception object (may be null if request failed).
     * @param result the string response.
     */
    @SuppressWarnings("unchecked")
    private void handleOnCompleted(Exception e, Response<String> result) {
        printLogs(1);  // request finished

        // prepare the result code string
        int statusCode = 0;
        if (result != null && result.getHeaders() != null) {
            statusCode = result.getHeaders().code();
        }

        if (e != null) { //on request failure
            Log.e(LOG_TAG, "Error(" + statusCode + "): " + e.getMessage());
            if (!(e instanceof CancellationException))
                if (listener != null) {
                    listener.onFail(e, statusCode, tag);
                }
        } else if (result.getResult() != null) {
            logE("Response(" + statusCode + "): " + result.getResult());

            if (cls == null && listener != null) { //T must be of type: Object or String
                listener.onSuccess((T) result.getResult(), statusCode, tag);
            } else if (listener != null) {
                try {
                    listener.onSuccess((T) new Gson().fromJson(result.getResult(), cls), statusCode, tag);
                } catch (JsonParseException ex) {
                    ex.printStackTrace();
                    listener.onFail(ex, statusCode, tag);
                }
            }
        }
    }

    /**
     * Cancels the request.
     *
     * @param interruptThread true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     * @return false if the task could not be cancelled, typically because it has already completed normally; true otherwise.
     */
    public boolean cancel(boolean interruptThread) {
        return !(future.isCancelled() || future.isDone()) && future.cancel(interruptThread);
    }

    /**
     * method, used to set the time out of the future requests
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * method, used to set the request headers
     *
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            this.headers = null;
        } else {
            this.headers = new HashMap<>();
            for (String key : headers.keySet()) {
                this.headers.put(key, Collections.singletonList(headers.get(key)));
            }
        }
    }

    /**
     * method, used to set the request parameters
     *
     * @param params
     */
    public void setParams(Map<String, String> params) {
        if (params == null) {
            this.params = null;
        } else {
            this.params = new HashMap<>();
            for (String key : params.keySet()) {
                this.params.put(key, Collections.singletonList(params.get(key)));
            }
        }
    }

    /**
     * method, used to set the request files if it is multipart request
     *
     * @param files
     */
    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    /**
     * method, used to set the request body if its is raw request
     *
     * @param body
     */
    public void setBody(Object body) {
        this.body = body;
    }

    /**
     * method, used to log messages
     *
     * @param message
     */
    private void logE(String message) {
        if (!Utils.DEBUGGABLE) {
            return;
        }

        if (Utils.isNullOrEmpty(message)) {
            Log.e(LOG_TAG, "" + message);
            return;
        }

        int maxLogSize = 1000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.e(LOG_TAG, message.substring(start, end));
        }
    }
}
