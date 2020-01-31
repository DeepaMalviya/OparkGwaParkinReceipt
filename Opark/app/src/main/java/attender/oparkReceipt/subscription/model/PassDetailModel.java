package attender.oparkReceipt.subscription.model;

/**
 * Created by Daffodil on 9/12/2018.
 */

public class PassDetailModel {

    String cardNo, agentId, plan, holderName, passHeading, checkinText, checkoutText, durationText;

    public String getCheckinText() {
        return checkinText;
    }

    public void setCheckinText(String checkinText) {
        this.checkinText = checkinText;
    }

    public String getCheckoutText() {
        return checkoutText;
    }

    public void setCheckoutText(String checkoutText) {
        this.checkoutText = checkoutText;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getPassHeading() {
        return passHeading;
    }

    public void setPassHeading(String passHeading) {
        this.passHeading = passHeading;
    }


}
