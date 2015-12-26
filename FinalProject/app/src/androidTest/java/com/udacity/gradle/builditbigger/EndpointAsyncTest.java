package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class EndpointAsyncTest extends ApplicationTestCase {

    public EndpointAsyncTest(){
        super(Application.class);
    }

    public void testEdnpointAsyncTask() throws Throwable{
        final CountDownLatch signal = new CountDownLatch(1);

        EndpointAsyncTestTask testTask = new EndpointAsyncTestTask();


        testTask.execute(getContext());

        signal.await(5, TimeUnit.SECONDS);
        assertNotNull(testTask.getResult());
    }

    private class EndpointAsyncTestTask extends EndpointAsyncTask{
        private String result;

        @Override
        protected void onPostExecute(String result){
            this.result = result;
        }

        public  String getResult(){
            return result;
        }
    }
}