package fyp.saira.driverhiring.ModelClasses;

public class PostRider {


    String departuredatetime, phoneno, endpoint, fullname, id, latend, latstart, lngend, lngstart, profileimgurl, regulartrip, roundtrip, startpoint, uid;

    public PostRider() {
    }


    public PostRider(String departuredatetime, String phoneno, String endpoint, String fullname, String id, String latend, String latstart, String lngend, String lngstart, String profileimgurl, String regulartrip, String roundtrip, String startpoint, String uid) {
        this.departuredatetime = departuredatetime;
        this.phoneno = phoneno;
        this.endpoint = endpoint;
        this.fullname = fullname;
        this.id = id;
        this.latend = latend;
        this.latstart = latstart;
        this.lngend = lngend;
        this.lngstart = lngstart;
        this.profileimgurl = profileimgurl;
        this.regulartrip = regulartrip;
        this.roundtrip = roundtrip;
        this.startpoint = startpoint;
        this.uid = uid;
    }

    public String getDeparturedatetime() {
        return departuredatetime;
    }

    public void setDeparturedatetime(String departuredatetime) {
        this.departuredatetime = departuredatetime;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatend() {
        return latend;
    }

    public void setLatend(String latend) {
        this.latend = latend;
    }

    public String getLatstart() {
        return latstart;
    }

    public void setLatstart(String latstart) {
        this.latstart = latstart;
    }

    public String getLngend() {
        return lngend;
    }

    public void setLngend(String lngend) {
        this.lngend = lngend;
    }

    public String getLngstart() {
        return lngstart;
    }

    public void setLngstart(String lngstart) {
        this.lngstart = lngstart;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public String getRegulartrip() {
        return regulartrip;
    }

    public void setRegulartrip(String regulartrip) {
        this.regulartrip = regulartrip;
    }

    public String getRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(String roundtrip) {
        this.roundtrip = roundtrip;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}