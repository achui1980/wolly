package com.sail.cot.domain;



/**
 * CotBank entity. @author MyEclipse Persistence Tools
 */

public class CotBank  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String bankName;
     private String bankShortName;
     private String bankAccount;
     private String bankPhone;
     private String bankFax;
     private String bankSwif;
     private String bankTelex;
     private String bankAddress;
     private String bankRemark;
     private Integer currencyId;
     private Integer payTypeid;
     private String bankContact;
     private String bankBeneficiary;
     private String advisingBank;
     private String cableAddress;
     private String beneficiaryAddress;
     private String intermediarySwft;
     private String intermediaryBank;
     private String op;


    // Constructors

    public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


	/** default constructor */
    public CotBank() {
    }

    
    /** full constructor */
    public CotBank(String bankName, String bankShortName, String bankAccount, String bankPhone, String bankFax, String bankSwif, String bankTelex, String bankAddress, String bankRemark, Integer currencyId, Integer payTypeid, String bankContact, String bankBeneficiary, String advisingBank, String cableAddress, String beneficiaryAddress, String intermediarySwft, String intermediaryBank) {
        this.bankName = bankName;
        this.bankShortName = bankShortName;
        this.bankAccount = bankAccount;
        this.bankPhone = bankPhone;
        this.bankFax = bankFax;
        this.bankSwif = bankSwif;
        this.bankTelex = bankTelex;
        this.bankAddress = bankAddress;
        this.bankRemark = bankRemark;
        this.currencyId = currencyId;
        this.payTypeid = payTypeid;
        this.bankContact = bankContact;
        this.bankBeneficiary = bankBeneficiary;
        this.advisingBank = advisingBank;
        this.cableAddress = cableAddress;
        this.beneficiaryAddress = beneficiaryAddress;
        this.intermediarySwft = intermediarySwft;
        this.intermediaryBank = intermediaryBank;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return this.bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortName() {
        return this.bankShortName;
    }
    
    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }
    
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankPhone() {
        return this.bankPhone;
    }
    
    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    public String getBankFax() {
        return this.bankFax;
    }
    
    public void setBankFax(String bankFax) {
        this.bankFax = bankFax;
    }

    public String getBankSwif() {
        return this.bankSwif;
    }
    
    public void setBankSwif(String bankSwif) {
        this.bankSwif = bankSwif;
    }

    public String getBankTelex() {
        return this.bankTelex;
    }
    
    public void setBankTelex(String bankTelex) {
        this.bankTelex = bankTelex;
    }

    public String getBankAddress() {
        return this.bankAddress;
    }
    
    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankRemark() {
        return this.bankRemark;
    }
    
    public void setBankRemark(String bankRemark) {
        this.bankRemark = bankRemark;
    }

    public Integer getCurrencyId() {
        return this.currencyId;
    }
    
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getPayTypeid() {
        return this.payTypeid;
    }
    
    public void setPayTypeid(Integer payTypeid) {
        this.payTypeid = payTypeid;
    }

    public String getBankContact() {
        return this.bankContact;
    }
    
    public void setBankContact(String bankContact) {
        this.bankContact = bankContact;
    }

    public String getBankBeneficiary() {
        return this.bankBeneficiary;
    }
    
    public void setBankBeneficiary(String bankBeneficiary) {
        this.bankBeneficiary = bankBeneficiary;
    }

    public String getAdvisingBank() {
        return this.advisingBank;
    }
    
    public void setAdvisingBank(String advisingBank) {
        this.advisingBank = advisingBank;
    }

    public String getCableAddress() {
        return this.cableAddress;
    }
    
    public void setCableAddress(String cableAddress) {
        this.cableAddress = cableAddress;
    }

    public String getBeneficiaryAddress() {
        return this.beneficiaryAddress;
    }
    
    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getIntermediarySwft() {
        return this.intermediarySwft;
    }
    
    public void setIntermediarySwft(String intermediarySwft) {
        this.intermediarySwft = intermediarySwft;
    }

    public String getIntermediaryBank() {
        return this.intermediaryBank;
    }
    
    public void setIntermediaryBank(String intermediaryBank) {
        this.intermediaryBank = intermediaryBank;
    }
   








}