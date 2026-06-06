
package thesweetspot.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class ReceiptGroup implements receiptComponent {
    private String groupName;
    private List<receiptComponent> items = new ArrayList<>();

    public ReceiptGroup(String groupName) {
        this.groupName = groupName;
    }

    public void addItem(receiptComponent item) {
        items.add(item);
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("> ").append(groupName).append("\n");
        for (receiptComponent item : items) {
            sb.append(item.display());
        }
        return sb.toString();
    }
}