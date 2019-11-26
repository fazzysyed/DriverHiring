package fyp.saira.driverhiring.ModelClasses;

public class RequestsModel {

    String id, senderid, reciverid, postid, sendername, imgurl, startpoint, endpoint, status;

    public RequestsModel() {
    }

    public RequestsModel(String id, String senderid, String reciverid, String postid, String sendername, String imgurl, String startpoint, String endpoint, String status) {
        this.id = id;
        this.senderid = senderid;
        this.reciverid = reciverid;
        this.postid = postid;
        this.sendername = sendername;
        this.imgurl = imgurl;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReciverid() {
        return reciverid;
    }

    public void setReciverid(String reciverid) {
        this.reciverid = reciverid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}