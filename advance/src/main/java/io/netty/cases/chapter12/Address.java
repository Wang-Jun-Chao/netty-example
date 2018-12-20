package io.netty.cases.chapter12;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class Address implements Cloneable {

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                '}';
    }
}
