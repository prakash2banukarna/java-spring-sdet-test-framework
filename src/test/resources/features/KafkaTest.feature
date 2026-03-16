Feature: Kafka Item-Store Message Produce and Consume
  As an SDET
  I want to produce Item-Store events to a Kafka topic
  And verify those messages are consumed correctly
  So that I can demonstrate end-to-end Kafka messaging in my framework

  Background:
    Given I have a Kafka topic ready to receive messages

#  @KafkaTest @smoke @ignore
#  Scenario: Produce a single Item-Store message and consume it
#    When I produce an Item-Store event with item 789837213, sainId "sd8ewb2" and storeNumber 608
#    Then the Item-Store message should be consumed within 10 seconds
#    And the message key should be "Item-store"
#    And the consumed message should contain item 789837213
#    And the consumed message should contain sainId "sd8ewb2"
#    And the consumed message should contain storeNumber 608

  @KafkaTest @regression
  Scenario: Produce 4 Item-Store messages and verify all are consumed
    When I produce the following Item-Store events:
      | item      | sainId   | storeNumber |
      | 789837213 | sd8ewb2  | 608         |
      | 112233445 | ab1cd2ef | 101         |
      | 998877665 | xy9wz8vt | 305         |
      | 554433221 | gh3ij4kl | 720         |
    Then all 4 Item-Store messages should be consumed within 15 seconds
    And each consumed message should have the key "Item-store"
