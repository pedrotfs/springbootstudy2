package br.com.pedrotfs.maestro.kafka.services.impl;

import br.com.pedrotfs.maestro.kafka.callback.ProducerCallBack;
import br.com.pedrotfs.maestro.kafka.factory.MaestroKafkaProducerFactory;
import br.com.pedrotfs.maestro.kafka.services.RequestProducerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestProducerServiceImpl implements RequestProducerService {
    private static Logger LOG = LoggerFactory.getLogger(RequestProducerServiceImpl.class);

    @Autowired
    private MaestroKafkaProducerFactory kafkaProducerFactory;

    @Override
    public void produceRequest(String request) {
        LOG.info("producing " + request + " request.");
        final KafkaProducer<String, String> producer = kafkaProducerFactory.createProducer();
        final ProducerCallBack callback = new ProducerCallBack();
        ProducerRecord<String, String> producerRecord = kafkaProducerFactory.createProducerRecord(request);
        producer.send(producerRecord , callback);
        producer.flush();
        producer.close();
    }
}
