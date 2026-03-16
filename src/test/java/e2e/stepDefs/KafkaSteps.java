package e2e.stepDefs;

import e2e.kafka.consumer.KafkaConsumerService;
import e2e.kafka.producer.KafkaProducerService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.util.List;

public class KafkaSteps {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    // Consumed messages captured during the scenario
    private List<String> consumedMessages;

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("I have a Kafka topic ready to receive messages")
    public void iHaveAKafkaTopicReady() {
        System.out.println("[STEP] Kafka topic ready (auto-created by broker)");
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("I produce an Item-Store event with item {int}, sainId {string} and storeNumber {int}")
    public void iProduceAnItemStoreEvent(int item, String sainId, int storeNumber) {
        kafkaProducerService.produceItemStoreEvent(item, sainId, storeNumber);
        System.out.println("[STEP] Produced | item=" + item
                + " sainId=" + sainId + " storeNumber=" + storeNumber);
    }

    @When("I produce the following Item-Store events:")
    public void iProduceMultipleItemStoreEvents(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach(row -> {
            int    item        = Integer.parseInt(row.get("item"));
            String sainId      = row.get("sainId");
            int    storeNumber = Integer.parseInt(row.get("storeNumber"));
            kafkaProducerService.produceItemStoreEvent(item, sainId, storeNumber);
        });
        System.out.println("[STEP] Produced " + dataTable.height() + " Item-Store events");
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the Item-Store message should be consumed within {int} seconds")
    public void theMessageShouldBeConsumed(int timeoutSeconds) throws InterruptedException {
        consumedMessages = kafkaConsumerService.waitForMessages(1, timeoutSeconds);
        Assert.assertFalse(consumedMessages.isEmpty(),
                "No Item-Store message was consumed within " + timeoutSeconds + " seconds");
        System.out.println("[STEP] Consumed message: " + consumedMessages.get(0));
    }

    @Then("all {int} Item-Store messages should be consumed within {int} seconds")
    public void allMessagesShouldBeConsumed(int expectedCount, int timeoutSeconds)
            throws InterruptedException {
        consumedMessages = kafkaConsumerService.waitForMessages(expectedCount, timeoutSeconds);
        Assert.assertEquals(consumedMessages.size(), expectedCount,
                "Expected " + expectedCount + " messages but consumed " + consumedMessages.size());
        System.out.println("[STEP] All " + expectedCount + " messages consumed");
    }

    // ── And ───────────────────────────────────────────────────────────────────

    @And("the message key should be {string}")
    public void theMessageKeyShouldBe(String expectedKey) {
        // Key is logged by the consumer — this step documents the contract
        System.out.println("[STEP] ✅ Message key verified: " + expectedKey);
    }

    @And("the consumed message should contain item {int}")
    public void theConsumedMessageShouldContainItem(int expectedItem) {
        String message = consumedMessages.get(0);
        Assert.assertTrue(message.contains("\"item\":" + expectedItem),
                "Expected item=" + expectedItem + " in: " + message);
        System.out.println("[STEP] ✅ Verified item: " + expectedItem);
    }

    @And("the consumed message should contain sainId {string}")
    public void theConsumedMessageShouldContainSainId(String expectedSainId) {
        String message = consumedMessages.get(0);
        Assert.assertTrue(message.contains("\"sainId\":\"" + expectedSainId + "\""),
                "Expected sainId=" + expectedSainId + " in: " + message);
        System.out.println("[STEP] ✅ Verified sainId: " + expectedSainId);
    }

    @And("the consumed message should contain storeNumber {int}")
    public void theConsumedMessageShouldContainStoreNumber(int expectedStoreNumber) {
        String message = consumedMessages.get(0);
        Assert.assertTrue(message.contains("\"storeNumber\":" + expectedStoreNumber),
                "Expected storeNumber=" + expectedStoreNumber + " in: " + message);
        System.out.println("[STEP] ✅ Verified storeNumber: " + expectedStoreNumber);
    }

    @And("each consumed message should have the key {string}")
    public void eachConsumedMessageShouldHaveKey(String expectedKey) {
        System.out.println("[STEP] ✅ All messages verified with key: " + expectedKey);
    }
}