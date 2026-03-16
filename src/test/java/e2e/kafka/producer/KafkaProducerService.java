package e2e.kafka.producer;

import e2e.config.AppConfiguration;
import e2e.model.ItemStoreEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AppConfiguration appConfiguration;

    /** Fixed Kafka message key for all Item-Store events */
    private static final String MESSAGE_KEY = "Item-store";

    /**
     * Produce an Item-Store event to the configured topic.
     *
     * @param item        item number (int)
     * @param sainId      SAIN identifier (String)
     * @param storeNumber store number (int)
     */
    public void produceItemStoreEvent(int item, String sainId, int storeNumber) {

        ItemStoreEvent event   = new ItemStoreEvent(item, sainId, storeNumber);
        String         payload = event.toJson();
        String         topic   = appConfiguration.getKafkaTopicName();

        System.out.println("[PRODUCER] 📤 Sending | topic=" + topic
                + " | key=" + MESSAGE_KEY
                + " | payload=" + payload);

        CompletableFuture future = kafkaTemplate.send(topic, MESSAGE_KEY, payload);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("[PRODUCER] ✅ Sent | key=" + MESSAGE_KEY
                        + " | item=" + item
                        + " | sainId=" + sainId
                        + " | storeNumber=" + storeNumber);
            } else {
                System.err.println("[PRODUCER] ❌ Failed | " + ((Throwable) ex).getMessage());
            }
        });
    }
}