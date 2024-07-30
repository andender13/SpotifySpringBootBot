package com.example.springbootmusictgbot.spotify_api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class RestRequestOperations {

    @Autowired
    WebClient.Builder webClientBuilder;
    private final OkHttpClient client;
    public RestRequestOperations(OkHttpClient client) {
        this.client = client;
    }

//    public void getPhotoByURL(String url, String title) {
//        OkHttpClient client = new OkHttpClient();
//
//        // Create a request
//        Request request = new Request.Builder().url(url).build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                // Get the response body
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {
//                    // Get an input stream to read data from the response body
//                    InputStream inputStream = responseBody.byteStream();
//
//                    // Open a file for writing
//                    String filePath = "src/main/resources/" + title + ".jpg";
//                    try (OutputStream outputStream = new FileOutputStream(filePath)) {
//                        // Read data from input stream and write it to the file
//                        byte[] buffer = new byte[4096];
//                        int bytesRead;
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            outputStream.write(buffer, 0, bytesRead);
//                        }
//                        System.out.println("Image successfully downloaded: " + filePath);
//                    } catch (IOException e) {
//                        System.err.println("Failed to write image file: " + e.getMessage());
//                    } finally {
//                        // Close the input stream
//                        inputStream.close();
//                    }
//                } else {
//                    System.out.println("Failed to get response body");
//                }
//            } else {
//                System.out.println("HTTP error: " + response.code() + " - " + response.message());
//            }
//        } catch (IOException e) {
//            System.err.println("Failed to make HTTP request: " + e.getMessage());
//        }
//    }

    public void getPhotoByURL(String url, String title) {
        OkHttpClient client = new OkHttpClient();

        // Create a request
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Get the response body
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    // Get an input stream to read data from the response body
                    InputStream inputStream = responseBody.byteStream();

                    // Open a file for writing
                    String filePath =  title + ".jpg";
                    try (OutputStream outputStream = new FileOutputStream(filePath)) {
                        // Read data from input stream and write it to the file
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        System.out.println("Image successfully downloaded: " + filePath);
                    } catch (IOException e) {
                        System.err.println("Failed to write image file: " + e.getMessage());
                    } finally {
                        // Close the input stream
                        inputStream.close();
                    }
                } else {
                    System.out.println("Failed to get response body");
                }
            } else {
                System.out.println("HTTP error: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            System.err.println("Failed to make HTTP request: " + e.getMessage());
        }
    }

    public void getMp3ByURL(String url, String title) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    saveToFile(inputStream,  title + ".mp3");
                    System.out.println("MP3 файл успешно скачан");
                } else {
                    System.out.println("Не удалось получить тело ответа");
                }
            } else {
                System.out.println("Ошибка: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(InputStream inputStream, String filePath) throws IOException {
        OutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
}
