package io.pivotal.microservices.model;

public class DeclarationEntity {
    private long ID;
    private String EnterpriseInfo;
    private String AttachedInfo;
    private double FreightCost;
    private double InsuranceCost;
    private double OtherCost;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getEnterpriseInfo() {
        return EnterpriseInfo;
    }

    public void setEnterpriseInfo(String enterpriseInfo) {
        EnterpriseInfo = enterpriseInfo;
    }

    public String getAttachedInfo() {
        return AttachedInfo;
    }

    public void setAttachedInfo(String attachedInfo) {
        AttachedInfo = attachedInfo;
    }

    public double getFreightCost() {
        return FreightCost;
    }

    public void setFreightCost(double freightCost) {
        FreightCost = freightCost;
    }

    public double getInsuranceCost() {
        return InsuranceCost;
    }

    public void setInsuranceCost(double insuranceCost) {
        InsuranceCost = insuranceCost;
    }

    public double getOtherCost() {
        return OtherCost;
    }

    public void setOtherCost(double otherCost) {
        OtherCost = otherCost;
    }
}
