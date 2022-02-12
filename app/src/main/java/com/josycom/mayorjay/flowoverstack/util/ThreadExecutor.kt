package com.josycom.mayorjay.flowoverstack.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadExecutor {
    var mExecutor: ExecutorService = Executors.newFixedThreadPool(5)
}