package br.com.pedrotfs.maestro.kafka.callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerCallBack implements Callback {

    private static Logger LOG = LoggerFactory.getLogger(ProducerCallBack.class);

    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        LOG.debug("topic: " + recordMetadata.topic() + " - Prt: " + recordMetadata.partition()
                + " - Tms: " + recordMetadata.timestamp() + " - Off: " + recordMetadata.offset());
        if(e != null)
        {
            LOG.error("error sending to kafka!");
            e.printStackTrace();
            return;
        }
        LOG.debug("message sent succesfully.");
    }
}
