public class HeavyHitter {

    private long ip;
    private int degree;
    private long upper;
    private long lower;
    private double freq;

    public HeavyHitter(long _ip, int _degree, long _upper, long _lower, double _freq){
        this.ip=_ip;
        this.degree=_degree;
        this.upper=_upper;
        this.lower=_lower;
        this.freq=_freq;
    }

    public double getFreq(){
    	return freq;
    }
    
    public int getDegree() {
        return degree;
    }

    public long getIp() {
        return ip;
    }

    public long getLower() {
        return lower;
    }

    public long getUpper() {
        return upper;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public void setLower(long lower) {
        this.lower = lower;
    }

    public void setUpper(long upper) {
        this.upper = upper;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return ip+", ";
    }
}
