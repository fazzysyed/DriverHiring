package fyp.saira.driverhiring.ModelClasses;

public class StartRideRequestModel {
    String  rideid,driveruid,drivername,sourcelat,sourcelng,destenationlat,destenationlng;

    public StartRideRequestModel() {
    }

    public StartRideRequestModel(String rideid, String driveruid, String drivername, String sourcelat, String sourcelng, String destenationlat, String destenationlng) {
        this.rideid = rideid;
        this.driveruid = driveruid;
        this.drivername = drivername;
        this.sourcelat = sourcelat;
        this.sourcelng = sourcelng;
        this.destenationlat = destenationlat;
        this.destenationlng = destenationlng;
    }


    public String getRideid() {
        return rideid;
    }

    public void setRideid(String rideid) {
        this.rideid = rideid;
    }

    public String getDriveruid() {
        return driveruid;
    }

    public void setDriveruid(String driveruid) {
        this.driveruid = driveruid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getSourcelat() {
        return sourcelat;
    }

    public void setSourcelat(String sourcelat) {
        this.sourcelat = sourcelat;
    }

    public String getSourcelng() {
        return sourcelng;
    }

    public void setSourcelng(String sourcelng) {
        this.sourcelng = sourcelng;
    }

    public String getDestenationlat() {
        return destenationlat;
    }

    public void setDestenationlat(String destenationlat) {
        this.destenationlat = destenationlat;
    }

    public String getDestenationlng() {
        return destenationlng;
    }

    public void setDestenationlng(String destenationlng) {
        this.destenationlng = destenationlng;
    }
}
