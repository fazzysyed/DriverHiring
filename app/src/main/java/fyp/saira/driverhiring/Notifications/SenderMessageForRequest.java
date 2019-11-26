package fyp.saira.driverhiring.Notifications;

import java.util.HashMap;

public class SenderMessageForRequest {
    public String title;
    HashMap<String, String> data;

    public SenderMessageForRequest() {
    }

    public SenderMessageForRequest(HashMap<String, String> data, String title) {
        this.data = data;
        this.title = title;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
