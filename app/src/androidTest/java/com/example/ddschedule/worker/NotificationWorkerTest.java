package com.example.ddschedule.worker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestWorkerBuilder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NotificationWorkerTest {
    private Context mContext;
    private Executor mExecutor;

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testSleepWorker() {

        NotificationWorker worker =
                (NotificationWorker) TestWorkerBuilder.from(mContext,
                        NotificationWorker.class,
                        mExecutor)
                        .build();

        ListenableWorker.Result result;
        result = worker.doWork();
        assertThat(result, is(ListenableWorker.Result.success()));
    }
}