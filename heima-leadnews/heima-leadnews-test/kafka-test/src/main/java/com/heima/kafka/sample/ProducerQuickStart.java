package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerQuickStart {
    // kafka链接配置
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // kafka链接配置
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        // 创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        // 发送消息
        System.out.println("开始发送消息");
        for(int i=0;i<5;i++){
            ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<>("itcast-topic-input", "key-1", "hello kafka");
            producer.send(kvProducerRecord);
        }
        System.out.println("发送消息结束");
        // 同步发送
//        RecordMetadata recordMetadata = producer.send(kvProducerRecord).get();
//        System.out.println(recordMetadata.offset());

        // 异步发送
//        producer.send(kvProducerRecord, new Callback() {
//
//            @Override
//            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                if(e!=null){
//                    e.printStackTrace();
//                }
//                System.out.println(recordMetadata.offset());
//            }
//        });
        // 关闭消息通道
        producer.close();
    }
}
















































