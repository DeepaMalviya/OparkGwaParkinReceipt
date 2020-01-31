package attender.oparkReceipt.parkinglist.model;

/**
 * Created by Daffodil on 7/13/2018.
 */

public class ParkingListMoel {
    String parkingID,parkingName,parkingType,checkInDateTime,vehicleNo,count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(String checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getParkingID() {
        return parkingID;
    }

    public void setParkingID(String parkingID) {
        this.parkingID = parkingID;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
}
