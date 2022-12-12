package exam.model.dto;

import exam.model.entity.WarrantyType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ImportLaptopDTO {

    //  "macAddress": "B6-34",
    //    "cpuSpeed": 4.9,
    //    "ram": 32,
    //    "storage": 1024,
    //    "description": "Aliquam non mauris. Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet.",
    //    "price": 7443.63,
    //    "warrantyType": "PREMIUM",
    //    "shop": {
    //      "name": "Becker"
    //    }

    @Size(min = 8)
    private String macAddress;

    @Positive
    private float cpuSpeed;

    @Min(8)
    @Max(128)
    private int ram;

    @Min(128)
    @Max(1024)
    private int storage;

    @Size(min = 10)
    private String description;

    @Positive
    private BigDecimal price;

    private String warrantyType;

    private ShopNameDTO shop;

    public ImportLaptopDTO() {}

    public String getMacAddress() {
        return macAddress;
    }

    public float getCpuSpeed() {
        return cpuSpeed;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public ShopNameDTO getShop() {
        return shop;
    }
}
