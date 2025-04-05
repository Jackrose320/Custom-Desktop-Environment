package hellofx;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ShortcutsData {

    private List<Node> data;

    public <T1, T2, T3> ShortcutsData() {
    }

    class Node {
        private String path;
        private Button b;
    }

    public void addToPane(AnchorPane ap, String path) {
        boolean found = false;
        int i = 0;

        while (i < this.data.size() && !found) {
            if (this.data.get(i).path.equals(path)) {

                ap.getChildren().add(this.data.get(i).b);

                found = true;
            }
            i++;
        }
    }

    public void add(String path, Button b) {
        Node newNode = new Node();
        newNode.path = path;
        newNode.b = b;

        this.data.add(newNode);
    }

    public void remove(String path) {
        boolean found = false;
        int i = 0;

        while (i < this.data.size() && !found) {
            if (this.data.get(i).path.equals(path)) {
                this.data.remove(i);
                found = true;
            }
            i++;
        }
    }

    public void append(ShortcutsData other) {
        this.append(other);
    }

    public int length() {
        return this.data.size();
    }
}
