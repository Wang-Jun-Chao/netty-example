package io.netty.cases.chapter12;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class UserInfoV2 implements Cloneable {

    private int age;

    private Address address;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Object clone() throws CloneNotSupportedException {
        UserInfoV2 cloneInfo = (UserInfoV2) super.clone();
        cloneInfo.setAddress((Address) address.clone());
        return cloneInfo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", address=" + address +
                '}';
    }
}
