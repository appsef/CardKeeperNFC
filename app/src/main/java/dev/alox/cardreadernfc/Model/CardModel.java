package dev.alox.cardreadernfc.Model;

public class CardModel {


    private String cardNumber;
    private String expDate;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    private String cardType;

    public CardModel(){}

    private CardModel(String cardNumber,String expDate,String cardType){
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }



}
