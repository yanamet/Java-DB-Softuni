package _05;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity(name = "_05_credit_cards")
@DiscriminatorValue("credit_card")
public class CreditCard extends BillingDetails{

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(name = "expiration_month", nullable = false)
    private LocalDate expirationMonth;

    @Column(name = "expiration_year", nullable = false)
    private LocalDate expirationYear;

    public CreditCard() {}

    public CreditCard(String number, User owner, String cardType, LocalDate expirationMonth, LocalDate expirationYear) {
        super(number, owner);
        this.cardType = cardType;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(LocalDate expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public LocalDate getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(LocalDate expirationYear) {
        this.expirationYear = expirationYear;
    }
}
