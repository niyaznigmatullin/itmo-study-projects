package ru.ifmo.niyaz.study.network.task4;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 20.12.12
 * Time: 1:18
 * To change this template use File | Settings | File Templates.
 */
public class User {
    String hostAddress;
    String name;
    String contentID;

    public User(String hostAddress, String name, String contentID) {
        this.hostAddress = hostAddress;
        this.name = name;
        this.contentID = contentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (hostAddress != null ? !hostAddress.equals(user.hostAddress) : user.hostAddress != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostAddress != null ? hostAddress.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\t\t" + hostAddress + "\t\t" + name + "\t\t" + contentID + "\t\t";
    }
}