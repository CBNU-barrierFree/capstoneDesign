package com.example.demo.DTO;

import java.util.List;

public class TourApiResponseDTO {
    private List<TourDTO> tours;
    private Header header;
    private Body body;

    public List<TourDTO> getTours() {
        return tours;
    }
    public void setTours(List<TourDTO> tours) {
        this.tours = tours;
    }
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
    public Body getBody() {
        return body;
    }
    public void setBody(Body body) {
        this.body = body;
    }
}
