package attender.oparkReceipt.booking;

/**
 * Created by Daffodil on 6/30/2018.
 */

public class CheckInModel {
    String receiptHeading,parkingAddress,userContactNo,checkInDate,agentId,availableSlots,parkingId,vehicleNo="",parkingRate,additionalParkingRate,onlineUserText,lastLine,
            mode,receiptStaticText,receiptEmail,receiptMobile,receiptWebsite,barcode,responseType,parkingType,companyWebsite,poweredBy,receiptNo,qrCode,agentName="",cardNo,printReceipt;

    public String getLastLine() {
        return lastLine;
    }

    public void setLastLine(String lastLine) {
        this.lastLine = lastLine;
    }

    public String getOnlineUserText() {
        return onlineUserText;
    }

    public void setOnlineUserText(String onlineUserText) {
        this.onlineUserText = onlineUserText;
    }

    public String getPrintReceipt() {
        return printReceipt;
    }

    public void setPrintReceipt(String printReceipt) {
        this.printReceipt = printReceipt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    private boolean isCheckin = false;
    public boolean isCheckin() {
        return isCheckin;
    }

    public void setCheckin(boolean checkin) {
        isCheckin = checkin;
    }


    private static final CheckInModel ourInstance = new CheckInModel();

    public static CheckInModel getInstance() {
        return ourInstance;
    }

    public CheckInModel() {
    }


    public String getReceiptHeading() {
        return receiptHeading;
    }

    public void setReceiptHeading(String receiptHeading)     {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
