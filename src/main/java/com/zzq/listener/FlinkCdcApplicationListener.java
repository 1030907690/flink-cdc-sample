package com.zzq.listener;


import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import com.zzq.sink.CustomSink;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Zhou Zhongqing
 * @date: 11/30/2025 8:18 PM
 */
@Component
public class FlinkCdcApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            threadPoolTaskExecutor.execute(() -> {
                startFlinkCdcListener();
            });
        }
    }

    private void startFlinkCdcListener() {
        MySqlSource<String> source = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                .databaseList("test") // set captured database
                .tableList("test.t_user") // set captured table
                .username("root")
                .password("root")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .includeSchemaChanges(true)
                .build();

// 启动一个webUI，指定本地WEB-UI端口号
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

// 检查点间隔时间
// checkpoint的侧重点是“容错”，即FLink作业意外失败并重启之后，能够直接从早先打下的checkpoint恢复运行，且不影响作业逻辑的准确性。
        env.enableCheckpointing(5000);

        DataStreamSink<String> sink = env.fromSource(source, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .addSink(new CustomSink());
        try {
            env.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
