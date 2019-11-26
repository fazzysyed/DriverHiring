package fyp.saira.driverhiring.ModelClasses;

public class PostDriver {

    String departuredatetime, phoneno, fareamount, endpoint, fullname, id, latend, latstart, lngend, lngstart, noofpassenger, offermessage, profileimgurl, regulartrip, roundtrip, startpoint, uid, vehicaltype;


    public PostDriver() {
    }

    public PostDriver(String departuredatetime, String phoneno, String fareamount, String endpoint, String fullname, String id, String latend, String latstart, String lngend, String lngstart, String noofpassenger, String offermessage, String profileimgurl, String regulartrip, String roundtrip, String startpoint, String uid, String vehicaltype) {
        this.departuredatetime = departuredatetime;
        this.phoneno = phoneno;
        this.fareamount = fareamount;
        this.endpoint = endpoint;
        this.fullname = fullname;
        this.id = id;
        this.latend = latend;
        this.latstart = latstart;
        this.lngend = lngend;
        this.lngstart = lngstart;
        this.noofpassenger = noofpassenger;
        this.offermessage = offermessage;
        this.profileimgurl = profileimgurl;
        this.regulartrip = regulartrip;
        this.roundtrip = roundtrip;
        this.startpoint = startpoint;
        this.uid = uid;
        this.vehicaltype = vehicaltype;
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

    public String getFareamount() {
        return fareamount;
    }

    public void setFareamount(String fareamount) {
        this.fareamount = fareamount;
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

    public String getNoofpassenger() {
        return noofpassenger;
    }

    public void setNoofpassenger(String noofpassenger) {
        this.noofpassenger = noofpassenger;
    }

    public String getOffermessage() {
        return offermessage;
    }

    public void setOffermessage(String offermessage) {
        this.offermessage = offermessage;
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

    public String getVehicaltype() {
        return vehicaltype;
    }

    public void setVehicaltype(String vehicaltype) {
        this.vehicaltype = vehicaltype;
    }
}