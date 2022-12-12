package _05;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "_05_bank_account")
@DiscriminatorValue("bank_account")
public class BankAccount extends BillingDetails{

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "swift_code", nullable = false)
    private String swiftCode;

    public BankAccount(String number, User owner, String bankName, String swiftCode) {
        super(number, owner);
        this.bankName = bankName;
        this.swiftCode = swiftCode;
    }

    public BankAccount() {}

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}

