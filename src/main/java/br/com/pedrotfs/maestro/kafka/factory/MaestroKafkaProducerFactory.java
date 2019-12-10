package br.com.pedrotfs.maestro.kafka.factory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MaestroKafkaProducerFactory {

    @Value("${default.bootstrap.server}")
    private String bootstrapServer;

    @Value("${kafka.topic.produce}")
    private String topic;

    @Autowired
    private KafkaFactoryUtils kafkaFactoryUtils;

    private Integer key = 0;

    private final static Integer KEY_FACTOR = 3;

    public KafkaProducer<String, String> createProducer()
    {
        return new KafkaProducer<>(createPropertiesProducer());
    }

    private Properties createPropertiesProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        kafkaFactoryUtils.setSerializerPropertiesProducer(properties);
        return properties;
    }

    public ProducerRecord<String, String> createProducerRecord(final String message)
    {
        return new ProducerRecord<>(topic, Integer.toString((key++) % KEY_FACTOR), message);
    }
}
