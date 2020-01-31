package attender.oparkReceipt.vehiclelist.model;

/**
 * Created by Daffodil on 5/24/2018.
 */

public class ChechInListBean {

    String transactionId;
    String receiptHeading;
    String parkingAddress;
    String userContactNo;
    String checkInDate;
    String agentId;
    String availableSlots;
    String parkingId;
    String vehicleNo;
    String parkingRate;
    String additionalParkingRate;
    String mode;
    String receiptStaticText;
    String receiptEmail;
    String receiptMobile;
    String receiptWebsite;
    String receipt;
    String responseType;
    String parkingType;
    String companyWebsite;
    String poweredBy;
    String vehicleType;
    String receiptType;
    String receiptNo;


    private static final ChechInListBean ourInstance = new ChechInListBean();

    public static ChechInListBean getOurInstance() {
        return ourInstance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptHeading() {
        return receiptHeading;
    }

    public void setReceiptHeading(String receiptHeading) {
        this.receiptHeading = receiptHeading;
    }

    public String getParkingAddress() {
        return parkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }

    public String getUserContactNo() {
        return userContactNo;
    }

    public void setUserContactNo(String userContactNo) {
        this.userContactNo = userContactNo;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getParkingRate() {
        return parkingRate;
    }

    public void setParkingRate(String parkingRate) {
        this.parkingRate = parkingRate;
    }

    public String getAdditionalParkingRate() {
        return additionalParkingRate;
    }

    public void setAdditionalParkingRate(String additionalParkingRate) {
        this.additionalParkingRate = additionalParkingRate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getReceiptStaticText() {
        return receiptStaticText;
    }

    public void setReceiptStaticText(String receiptStaticText) {
        this.receiptStaticText = receiptStaticText;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }

    public String getReceiptMobile() {
        return receiptMobile;
    }

    public void setReceiptMobile(String receiptMobile) {
        this.receiptMobile = receiptMobile;
    }

    public String getReceiptWebsite() {
        return receiptWebsite;
    }

    public void setReceiptWebsite(String receiptWebsite) {
        this.receiptWebsite = receiptWebsite;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getPoweredBy() {
        return poweredBy;
    }

    public void setPoweredBy(String poweredBy) {
        this.poweredBy = poweredBy;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}
