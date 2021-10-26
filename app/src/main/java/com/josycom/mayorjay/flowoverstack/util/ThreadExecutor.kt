package com.josycom.mayorjay.flowoverstack.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor {
    public static ExecutorService mExecutor = Executors.newFixedThreadPool(5);
}
