public class Entry {

    private long ip;
    private long delta;

    public Entry(long _ip, long _delta){
        this.ip=_ip;
        this.delta=_delta;
    }

    public long getIp() {
        return ip;
    }

    public long getDelta() {
        return delta;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }


}
