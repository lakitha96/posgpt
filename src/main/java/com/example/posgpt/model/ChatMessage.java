package com.example.posgpt.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author lakithaprabudh
 */
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Column(length = 1000)
    private String messageContent;
    @Column(length = 5000)
    private String responseContent;
    private LocalDateTime timestamp;
    private String messageType;

    // Related product information
    private Long relatedProductId;
    private String relatedProductName;
    private Double relatedProductPrice;
    private String relatedProductDescription;

    // Related sale information
    private Long relatedSaleId;
    private String relatedSaleStatus;
    private LocalDateTime relatedSaleDateTime;

    private boolean resultedInSale;
    private boolean resultedInReturn;

    public ChatMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(Long relatedProductId) {
        this.relatedProductId = relatedProductId;
    }

    public String getRelatedProductName() {
        return relatedProductName;
    }

    public void setRelatedProductName(String relatedProductName) {
        this.relatedProductName = relatedProductName;
    }

    public Double getRelatedProductPrice() {
        return relatedProductPrice;
    }

    public void setRelatedProductPrice(Double relatedProductPrice) {
        this.relatedProductPrice = relatedProductPrice;
    }

    public String getRelatedProductDescription() {
        return relatedProductDescription;
    }

    public void setRelatedProductDescription(String relatedProductDescription) {
        this.relatedProductDescription = relatedProductDescription;
    }

    public Long getRelatedSaleId() {
        return relatedSaleId;
    }

    public void setRelatedSaleId(Long relatedSaleId) {
        this.relatedSaleId = relatedSaleId;
    }

    public String getRelatedSaleStatus() {
        return relatedSaleStatus;
    }

    public void setRelatedSaleStatus(String relatedSaleStatus) {
        this.relatedSaleStatus = relatedSaleStatus;
    }

    public LocalDateTime getRelatedSaleDateTime() {
        return relatedSaleDateTime;
    }

    public void setRelatedSaleDateTime(LocalDateTime relatedSaleDateTime) {
        this.relatedSaleDateTime = relatedSaleDateTime;
    }

    public boolean isResultedInSale() {
        return resultedInSale;
    }

    public void setResultedInSale(boolean resultedInSale) {
        this.resultedInSale = resultedInSale;
    }

    public boolean isResultedInReturn() {
        return resultedInReturn;
    }

    public void setResultedInReturn(boolean resultedInReturn) {
        this.resultedInReturn = resultedInReturn;
    }
}
