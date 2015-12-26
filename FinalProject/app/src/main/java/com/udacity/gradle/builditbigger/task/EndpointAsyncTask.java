package com.udacity.gradle.builditbigger.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.irahavoi.jokes.backend.jokesApi.JokesApi;
import com.rahavoi.JokeConstants;
import com.rahavoi.irahavoi.androidjokeslib.JokeDisplayActivity;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
    private static JokesApi jokesApi = null;
    private Context context;

    @Override
    protected String doInBackground(Context... params) {
        if(jokesApi == null) {  // Only do this once
            JokesApi.Builder builder = new JokesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappservers
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            jokesApi = builder.build();
        }

        this.context = params[0];

        try {
            return jokesApi.tellJoke().execute().getData();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Intent jokeDisplay = new Intent(context, JokeDisplayActivity.class);
        jokeDisplay.putExtra(JokeConstants.JOKE, result);
        context.startActivity(jokeDisplay);
    }
}