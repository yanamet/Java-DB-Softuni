package com.example.modelmapping.entities.games;

import com.example.modelmapping.exceprions.InvalidGameParameters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GameDTO {

    private static final String TITLE_MESSAGE = "Game title should start with an upper case " +
            "letter and should have length over 3 and less than 100 symbols.";

    private String title;
    private String trailer;
    private String imageUrl;
    private float size;
    private BigDecimal price;
    private String description;

    private LocalDate releaseDate;

    public GameDTO(String[] commandLine) {
        this.setTitle(commandLine[1]);
        this.setPrice(BigDecimal.valueOf(Double.parseDouble(commandLine[2])));
        this.setSize(Float.parseFloat(commandLine[3]));
        this.setTrailer(commandLine[4]);
        this.setImageUrl(commandLine[5]);
        this.setDescription(commandLine[6]);
        this.setReleaseDate(LocalDate.parse(commandLine[7], DateTimeFormatter.ofPattern("d-MM-yyyy")));
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(!Character.isUpperCase(title.charAt(0)) || title.length() < 3 || title.length() > 100){
            throw new InvalidGameParameters(TITLE_MESSAGE);
        }
        this.title = title;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {


        if(trailer.length() > 11 && !trailer.contains("www.youtube.com")){
            throw new InvalidGameParameters("The video trailer must be from youtube.");
        }


        if(trailer.length() > 11){
            trailer = trailer.substring(trailer.length() - 11);
        }


        this.trailer = trailer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if(!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")){
            throw new InvalidGameParameters("Thumbnail URL should start with https protocol.");
        }
        this.imageUrl = imageUrl;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        if(size< 0.0){
            throw new InvalidGameParameters("Size must be a positive number.");
        }
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if(price.doubleValue() < 0.0){
            throw new InvalidGameParameters("Price must be a positive number.");
        }
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description.length() < 20){
            throw new InvalidGameParameters("Description must be at least 20 symbols long.");
        }
        this.description = description;
    }
}
