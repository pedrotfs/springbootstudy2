package br.com.pedrotfs.maestro.kafka.factory;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
class KafkaFactoryUtils {

    @Value("${kafka.message.compression}")
    private String compression;

    @Value("${kafka.message.batch.size}")
    private String batchSize;

    @Value("${kafka.idempotence}")
    private String idempotence;

    @Value("${kafka.acks.config}")
    private String acksConfig;

    @Value("${kafka.retries.config}")
    private String retries;

    @Value("${kafka.max.flight.request}")
    private String maxInFlightRequests;

    @Value("${kafka.linger.ms.config}")
    private String lingerMs;

    void setSerializerPropertiesProducer(Properties properties) {
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //safety
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        properties.setProperty(ProducerConfig.ACKS_CONFIG, acksConfig);
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, retries);
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);

        //high throughput producer with more cost to cpu and latency
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, compression);
//        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
//        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
    }

    void setSerializerPropertiesConsumer(Properties properties) {
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); //set to false to use commit command
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10000"); //10 em 10
    }
}
