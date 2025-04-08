package com.example.demo.DTO;

// DB DTO 객체
public class TourDTO {
    private String title;
    private String issuedDate;
    private String category1;
    private String category2;
    private String category3;
    private String description;
    private String subDescription;
    private String tel;
    private String url;
    private String address;
    private String coordinates;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIssuedDate() {
        return issuedDate;
    }
    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }
    public String getCategory1() {
        return category1;
    }
    public void setCategory1(String category1) {
        this.category1 = category1;
    }
    public String getCategory2() {
        return category2;
    }
    public void setCategory2(String category2) {
        this.category2 = category2;
    }
    public String getCategory3() {
        return category3;
    }
    public void setCategory3(String category3) {
        this.category3 = category3;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSubDescription() {
        return subDescription;
    }
    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
