package com.example.josycom.flowoverstack.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor {

    public final ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    public ThreadExecutor(){}
}
