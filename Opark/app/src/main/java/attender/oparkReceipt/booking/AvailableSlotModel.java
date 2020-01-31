package attender.oparkReceipt.booking;

/**
 * Created by Daffodil on 6/29/2018.
 */

public class AvailableSlotModel {
    String parkingId,parkingType,bookedSlots,availableSlots;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(String bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }
}
