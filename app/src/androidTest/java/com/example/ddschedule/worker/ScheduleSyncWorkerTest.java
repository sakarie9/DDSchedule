package com.example.ddschedule.worker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestListenableWorkerBuilder;
import androidx.work.testing.TestWorkerBuilder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ScheduleSyncWorkerTest {
    private Context mContext;
    private Executor mExecutor;

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testSleepWorker() throws ExecutionException, InterruptedException {

        ScheduleSyncWorker worker = TestListenableWorkerBuilder.from(mContext,
                        ScheduleSyncWorker.class)
                        .build();

        ListenableWorker.Result result = worker.startWork().get();
        assertThat(result, is(ListenableWorker.Result.success()));
    }
}