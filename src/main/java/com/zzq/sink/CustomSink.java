package com.zzq.sink;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author: Zhou Zhongqing
 * @date: 11/30/2025 10:24 PM
 */
public class CustomSink extends RichSinkFunction<String> {

    private static final Logger log = LoggerFactory.getLogger(CustomSink.class);

    @Override
    public void invoke(String json, Context context) throws Exception {
        // OP字段：该字段也有4种取值，分别是C(create)、U(Update)、D(Delete)、Read®。
        // 对于U操作，其数据部分同时包含了Before和After。
        log.info("json: {}", json);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
    }

    @Override
    public void close() throws Exception {
    }
}
