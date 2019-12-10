package br.com.pedrotfs.maestro.kafka.services.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.kafka.factory.MaestroKafkaConsumerFactory;
import br.com.pedrotfs.maestro.kafka.services.RegisterConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;

@Component
public class RegisterConsumerServiceImpl implements RegisterConsumerService {

    @Autowired
    private MaestroKafkaConsumerFactory consumerFactory;

    private KafkaConsumer<String, String> consumer = null;

    private static Logger LOG = LoggerFactory.getLogger(RegisterConsumerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void consumeRegisters() {
        LOG.info("pooling kafka queue.");
        if(consumer == null)
        {
            consumer = consumerFactory.createConsumer();
        }
        ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(10000));
        while(poll.isEmpty())
        {
            poll = consumer.poll(Duration.ofMillis(10000));
        }
        poll.forEach(r -> {
            LOG.info("request pooled. Key: " + r.key() + ", timestamp: " + r.timestamp()
                    + ", offset: " + r.offset() + ", partition: " + r.partition() + ", value: " + r.value());
            buildDraws(r.value());
        });
        consumer.commitSync();
    }

    private void buildDraws(final String json) {
        Document parsedDocument = Document.parse(json);
        if(shouldPersist(parsedDocument.getInteger("_id"))) {
            Draw draw = new Draw();
            draw.set_id(parsedDocument.getInteger("_id").toString());
            Document dateDocument = (Document) parsedDocument.get("date");
            String date = dateDocument.getInteger("day") + "/" + dateDocument.getInteger("month") + "/" + dateDocument.getInteger("year");
            draw.setDate(date);
            draw.setRegisterId(parsedDocument.getString("name").toLowerCase());
            draw.setNumbers((ArrayList<Integer>) parsedDocument.get("numbers"));
            draw.setWinnerCategoriesAmount((ArrayList<Integer>) parsedDocument.get("winnerCategoriesAmount"));
            draw.setWinnerCategoriesDividends((ArrayList<Long>) parsedDocument.get("winnerCategoriesDividends"));
            mongoTemplate.save(draw);
        }
    }

    private boolean shouldPersist(final Integer id)
    {
        return mongoTemplate.getDb().getCollection("draw").find(new Document("_id", id.toString())).first() == null;
    }
}
