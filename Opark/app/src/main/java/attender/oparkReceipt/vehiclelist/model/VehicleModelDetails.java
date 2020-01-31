package attender.oparkReceipt.vehiclelist.model;

/**
 * Created by as187 on 5/24/2017.
 */

public class VehicleModelDetails {

    private String transactionId;
    private String parkingName;
    private String checkInDateTime;
    private String checkOutDateTime;
    private String vehicleNo;
    private String mobileNo;
    private String vehicleType;
    private String agentName;
    private String printReceipt;

    public String getPrintReceipt() {
        return printReceipt;
    }

    public void setPrintReceipt(String printReceipt) {
        this.printReceipt = printReceipt;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    private String parkingType;

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(String checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
