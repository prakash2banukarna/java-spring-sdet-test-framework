package e2e.model;

/**
 * Represents a Kafka message payload for an Item-Store event.
 */
public class ItemStoreEvent {

    private int    item;
    private String sainId;
    private int    storeNumber;

    public ItemStoreEvent() {}

    public ItemStoreEvent(int item, String sainId, int storeNumber) {
        this.item        = item;
        this.sainId      = sainId;
        this.storeNumber = storeNumber;
    }

    public int    getItem()        { return item; }
    public String getSainId()      { return sainId; }
    public int    getStoreNumber() { return storeNumber; }

    public void setItem(int item)              { this.item = item; }
    public void setSainId(String sainId)       { this.sainId = sainId; }
    public void setStoreNumber(int storeNumber){ this.storeNumber = storeNumber; }

    /**
     * Serialises to the exact JSON structure required:
     * {
     *   "item": 789837213,
     *   "sainId": "sd8ewb2",
     *   "storeNumber": 608
     * }
     */
    public String toJson() {
        return "{"
                + "\"item\":"          + item          + ","
                + "\"sainId\":\""      + sainId        + "\","
                + "\"storeNumber\":"   + storeNumber
                + "}";
    }

    @Override
    public String toString() {
        return "ItemStoreEvent{item=" + item
                + ", sainId='" + sainId + '\''
                + ", storeNumber=" + storeNumber + '}';
    }
}
