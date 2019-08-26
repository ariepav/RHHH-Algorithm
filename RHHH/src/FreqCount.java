import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreqCount {

    private ConcurrentHashMap<Long, Long> freqMap;
    private ConcurrentHashMap<Long, Long> deltaMap;
    private int currentBucket;
    private int size;
    private int bucketSize;
    private double eps;
    private double s;
    private long delta;

    public FreqCount(double _eps, double _s){
        freqMap = new ConcurrentHashMap<Long, Long>();
        deltaMap= new ConcurrentHashMap<Long, Long>();
        currentBucket=1;
        size=0;
        bucketSize=((int)(1/_eps)+1);
        if(bucketSize==1001)
        	bucketSize=1000;
        eps=_eps;
        s=_s;
        delta=0;

    }

    public ConcurrentHashMap<Long, Long> getHashMap() {
        return freqMap;
    }

    public int getCurrentBucket() {
        return currentBucket;
    }

    public ConcurrentHashMap<Long, Long> getDeltaHashMap() {
        return deltaMap;
    }
    
    public int getSize() {
        return size;
    }

    public void setHashMap(ConcurrentHashMap<Long, Long> hashMap) {
        this.freqMap = hashMap;
    }

    public void setCurrentBucket(int currentBucket) {
        this.currentBucket = currentBucket;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addEntry(int idx, int Tsize){
    	
    	size+=1;
        if(freqMap.get(Main.ipEntries[idx].getIp())!=null){
            freqMap.put(Main.ipEntries[idx].getIp(), freqMap.get(Main.ipEntries[idx].getIp())+1);
        }
        else{
        	Main.ipEntries[idx].setDelta(currentBucket-1);
            deltaMap.put(Main.ipEntries[idx].getIp(),Main.ipEntries[idx].getDelta());
            freqMap.put(Main.ipEntries[idx].getIp(),(long)1);
            
        }

        if(size%bucketSize==0){

            for(Map.Entry<Long, Long> hme : freqMap.entrySet()) {
                if(freqMap.get(hme.getKey())+deltaMap.get(hme.getKey()) <= currentBucket) {
                    freqMap.remove(hme.getKey());
                    deltaMap.remove(hme.getKey());
                    
                }
                

            }
            currentBucket++;
        }
    }
    
    
    public void addEntry2(int idx){
    	
    	size+=1;
    	if(freqMap.get(Main.ipEntries[idx].getIp())!=null){
    		freqMap.put(Main.ipEntries[idx].getIp(), freqMap.get(Main.ipEntries[idx].getIp())+1);
    	}
    	else{
    		freqMap.put(Main.ipEntries[idx].getIp(), (long)1);
    		deltaMap.put(Main.ipEntries[idx].getIp(), delta);
    		
    	}
    	
    	if(Math.floor(size*eps)!=delta){
    		delta=(int)Math.floor(size*eps);
    		for(Map.Entry<Long, Long> hme : freqMap.entrySet()) {
                   if(hme.getValue()<delta){
                	   freqMap.remove(hme.getKey());
                	   deltaMap.remove(hme.getKey());
                   }
                	   
     
            }
    	}
    	
    }
    
    
    public void deleteInfreq(){
    	
    	for(Map.Entry<Long, Long> hme : freqMap.entrySet()) {
            if(freqMap.get(hme.getKey()) < (s-eps)*size) {
                freqMap.remove(hme.getKey());
                deltaMap.remove(hme.getKey());
            }

        }
    	
    }
}
