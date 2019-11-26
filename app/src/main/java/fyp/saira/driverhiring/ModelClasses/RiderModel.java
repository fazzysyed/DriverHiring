package fyp.saira.driverhiring.ModelClasses;

public class RiderModel {
    String uid;
    String email, status, reported, fullname, cnicno, phoneno, provence, city, designetion, age, online, gender, verified, currentlogitude, currentlatitude, profileimageurl;

    public RiderModel() {
    }

    public RiderModel(String uid, String email, String status, String reported, String fullname,
                      String cnicno, String phoneno, String provence, String city, String designetion,
                      String age, String online, String gender, String verified, String currentlogitude,
                      String currentlatitude, String profileimageurl) {
        this.uid = uid;
        this.email = email;
        this.status = status;
        this.reported = reported;
        this.fullname = fullname;
        this.cnicno = cnicno;
        this.phoneno = phoneno;
        this.provence = provence;
        this.city = city;
        this.designetion = designetion;
        this.age = age;
        this.online = online;
        this.gender = gender;
        this.verified = verified;
        this.currentlogitude = currentlogitude;
        this.currentlatitude = currentlatitude;
        this.profileimageurl = profileimageurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCnicno() {
        return cnicno;
    }

    public void setCnicno(String cnicno) {
        this.cnicno = cnicno;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getProvence() {
        return provence;
    }

    public void setProvence(String provence) {
        this.provence = provence;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesignetion() {
        return designetion;
    }

    public void setDesignetion(String designetion) {
        this.designetion = designetion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getCurrentlogitude() {
        return currentlogitude;
    }

    public void setCurrentlogitude(String currentlogitude) {
        this.currentlogitude = currentlogitude;
    }

    public String getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(String currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public String getProfileimageurl() {

        if (profileimageurl == null) {
            return "default";
        }

        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
    }
}
