package e2e.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KafkaConsumerService {

    /**
     * Only messages received AFTER reset() is called are stored here.
     * The 'accepting' gate prevents leftover messages from previous
     * scenarios bleeding into the current one.
     */
    private final List<String> receivedMessages = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean accepting = false;

    // ── Listener ─────────────────────────────────────────────────────────────

    @KafkaListener(
            topics  = "${kafka.topic.name}",
            groupId = "${kafka.consumer.group.id}"
    )
    public void consume(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC)     String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int    partition,
            @Header(KafkaHeaders.OFFSET)             long   offset,
            @Header(KafkaHeaders.RECEIVED_KEY)       String key
    ) {
        if (!accepting) {
            System.out.println("[CONSUMER] ⏭ Skipping old message at offset=" + offset);
            return;
        }
        System.out.println("[CONSUMER] 📥 Received | topic=" + topic
                + " | partition=" + partition
                + " | offset="    + offset
                + " | key="       + key
                + " | message="   + message);
        receivedMessages.add(message);
    }

    // ── Test-support helpers ──────────────────────────────────────────────────

    /** Called from @Before — clears list and opens the gate */
    public void reset() {
        receivedMessages.clear();
        accepting = true;
        System.out.println("[CONSUMER] 🔄 Reset — ready for new scenario");
    }

    /** Called from @After — closes gate so stray async messages don't bleed in */
    public void stopAccepting() {
        accepting = false;
    }

    /** Polls every 300ms until expectedCount messages arrive or timeout */
    public List<String> waitForMessages(int expectedCount, long timeoutSeconds)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1_000);
        while (System.currentTimeMillis() < deadline) {
            if (receivedMessages.size() >= expectedCount) {
                return new ArrayList<>(receivedMessages);
            }
            Thread.sleep(300);
        }
        return new ArrayList<>(receivedMessages);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}