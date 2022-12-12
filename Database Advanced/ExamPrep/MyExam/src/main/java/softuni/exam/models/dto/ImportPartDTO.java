package softuni.exam.models.dto;

import javax.validation.constraints.*;

public class ImportPartDTO {

    @Size(min = 2, max = 19)
    private String partName;

    @DecimalMin("10")
    @DecimalMax("2000")
    private double price;

    @Positive
    private int quantity;

    public String getPartName() {
        return partName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }


}
