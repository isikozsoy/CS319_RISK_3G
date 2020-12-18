import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AllianceRequestPane extends VBox {
    private int requestCount;
    HashMap<Integer, Integer> acceptedRequests;
    List<AllianceRequest> requestsElements;

    public AllianceRequestPane() {
        // Empty constructor
        requestCount = 0;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public List<AllianceRequest> getRequestsElements() {
        return requestsElements;
    }

    public int decreaseRequestCount() {
        requestCount--;
        return requestCount;
    }

    public void addRequest(int source, int target) {
        acceptedRequests.put(source, target);
    }

    public HashMap<Integer, Integer> getAcceptedRequests() {
        return acceptedRequests;
    }

    public void setAllianceRequests(Player curPlayer) {
        requestCount = curPlayer.getAllianceReq().size();
        HashMap<Integer, String> requests = curPlayer.getAllianceReq();
        Iterator iterator = requests.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            String name = (String) mapElement.getValue();
            int id = (int) mapElement.getKey();
            this.getChildren().add(new AllianceRequest(id,  name));
        }
    }

    class AllianceRequest extends HBox {
        private int id;
        private Text playerName;
        Button acceptButton;
        Button ignoreButton;

        AllianceRequest(int id, String name) {
            this.id = id;
            playerName = new Text(name);
            acceptButton = new Button("ACCEPT");
            ignoreButton = new Button("IGNORE");
            requestsElements.add(this);
            this.getChildren().addAll(playerName, acceptButton, ignoreButton);
        }
        public Button getIgnoreButton() {
            return ignoreButton;
        }

        public Button getAcceptButton() {
            return acceptButton;
        }

        public int getElementId() {
            return id;
        }

        public void removeButtons() {
            this.getChildren().removeAll(acceptButton, ignoreButton);
        }
    }
}