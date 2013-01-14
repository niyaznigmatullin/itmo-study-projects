package ru.ifmo.niyaz.study.network.task2;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 20.12.12
 * Time: 1:18
 * To change this template use File | Settings | File Templates.
 */
public class User {
    String hostAddress;
    String hostName;
    long timeStamp;
    String userName;
    int type;
    int messagePort;


    public User(String hostAddress, String hostName, long timeStamp, String userName, int type, int messagePort) {
        this.hostAddress = hostAddress;
        this.hostName = hostName;
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.type = type;
        this.messagePort = messagePort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "\t\t" + hostAddress + "\t\t" + hostName + "\t\t" + timeStamp + "\t\t"
                + userName + "\t\t" + type + "\t\t" + messagePort;
    }
}