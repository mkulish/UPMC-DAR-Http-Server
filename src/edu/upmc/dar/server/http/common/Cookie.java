package edu.upmc.dar.server.http.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cookie {
    private static final DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z");

    private String name;
    private String value;
    private Date expirationDate;
    private Integer maxAge;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String toString(){
        return name + "=" + value
                + ((expirationDate != null) ? ("; Expires=" + df.format(expirationDate)) : "")
                + ((maxAge != null) ? ("; Max-Age=" + maxAge) : "");
    }
}
