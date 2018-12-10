package com.sd.his.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "PAYMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NaturalId
    @Column(name = "PAYMENT_ID")
    private String paymentId;


    @Column(name = "PAYMENT_AMOUNT")
    private  Double paymentAmount;

    @Column(name = "REFUND_TYPE")
    private  String refundType;



    @Column(name = "TRANSACTION_TYPE")
    private  String transactionType;   // Advance / Invoice


    @OneToMany(mappedBy = "payment")
    private List<PatientInvoicePayment> patientInvoicePayment;


    @OneToMany (mappedBy="payment")
    private List<ReceiptPaymentType> ReceiptPaymentType;


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public List<PatientInvoicePayment> getPatientInvoicePayment() {
        return patientInvoicePayment;
    }

    public void setPatientInvoicePayment(List<PatientInvoicePayment> patientInvoicePayment) {
        this.patientInvoicePayment = patientInvoicePayment;
    }

    public List<com.sd.his.model.ReceiptPaymentType> getReceiptPaymentType() {
        return ReceiptPaymentType;
    }

    public void setReceiptPaymentType(List<com.sd.his.model.ReceiptPaymentType> receiptPaymentType) {
        ReceiptPaymentType = receiptPaymentType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
