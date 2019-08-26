
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	static double epsVal;
	static int numOfCounters;
	static long[] MASKS = { 0xFFFFFFFF, 0xFFFFFF00, 0xFFFF0000, 0xFF000000, 0x00000000 };
	static int[] levelEps = { 32, 24, 16, 8, 0 };
	static boolean RHHH10 = true;
	static boolean checkTime = false;;
	static double ignoreProb, logIgnoreProb, minuesQuantity;
	static int nrIgnore;
	static FreqCount[] freqCounters = new FreqCount[5];
	static Random rnd = new Random();
	static Random rnd2 = new Random();
	static Entry[] ipEntries;

	public static String backTo(long ip) {
		return null;
	}

	public static long createIP(String ip) {

		Pattern pattern = Pattern.compile("[0-9]+");
		ArrayList<String> stringList = new ArrayList<String>();
		Matcher matcher = pattern.matcher(ip);
		while (matcher.find()) {
			String matcherGroup = matcher.group(0);
			if (!matcherGroup.equals(" ")) {
				stringList.add(matcherGroup.replaceAll("\"", ""));
			}
		}
		String[] stringArray = stringList.toArray(new String[0]);
		int[] regexpsArray = new int[stringArray.length];
		for (int i = 0; i < regexpsArray.length; i++) {
			regexpsArray[i] = Integer.parseInt(stringArray[i]);
		}

		long ipNum = 0;

		for (int i = 0; i < regexpsArray.length; i++) {
			ipNum += (regexpsArray[regexpsArray.length - i - 1] * Math.pow(256, i));
		}
		return ipNum;
	}

	public static long findGenralizerOfDeg(long ip, int deg) {
		return ip & MASKS[deg];
	}

	public static void update(int idx) {
		if (RHHH10) {
			if (nrIgnore != 0) {
				nrIgnore--;
				return;
			}
			//Random rnd = new Random();
			int rndNum = rnd.nextInt(5);
			long prefix = findGenralizerOfDeg(ipEntries[idx].getIp(), rndNum);
			ipEntries[idx].setIp(prefix);
			freqCounters[rndNum].addEntry2(idx);
			//Random rnd2 = new Random();
			int rndNum2 = rnd.nextInt(Integer.MAX_VALUE);
			nrIgnore = (int) (Math.log((float) rndNum2) / logIgnoreProb - minuesQuantity);
		} else {
			//Random rnd = new Random();
			int rndNum = rnd.nextInt(5);
			long prefix = findGenralizerOfDeg(ipEntries[idx].getIp(), rndNum);
			ipEntries[idx].setIp(prefix);
			freqCounters[rndNum].addEntry2(idx);
		}
	}

	public static long generateOTOValue(long ip, int i) {

		return ((long) Math.pow(2, 32) * ip) + i;
	}

	public static ArrayList<HeavyHitter> output(long threshold, long streamLength) {

		double adjustedThreshold = threshold / 5.0;
		if (RHHH10)
			adjustedThreshold = (1.0 - ignoreProb) * threshold / 5.0;
		ArrayList<HeavyHitter> heavyHitters = new ArrayList<HeavyHitter>();
		int i, z = 4;
		long s;
		//int htSize = ((int) (1.0 / epsVal + 1)) | 1;

		HashMap<Long, Long> oneToOneS = new HashMap<Long, Long>();

		for (i = 0; i < 5; i++) {
			for (Map.Entry<Long, Long> hme : freqCounters[i].getHashMap().entrySet()) {
				if (oneToOneS.get(generateOTOValue(hme.getKey(), i)) != null) {
					s = oneToOneS.get(generateOTOValue(hme.getKey(), i));
				} else
					s = 0;

				double currentFreq = hme.getValue() - s + 2 * z * Math.sqrt(2 * hme.getValue());
				if (RHHH10) {
					currentFreq = hme.getValue() - s + 2 * z * Math.sqrt((1.0 - ignoreProb) * 2 * hme.getValue());
				}

				if (currentFreq >= adjustedThreshold) {

					HeavyHitter hh = new HeavyHitter(hme.getKey(), i, 5 * hme.getValue(),
							5 * (hme.getValue() - freqCounters[i].getDeltaHashMap().get(hme.getKey())),
							5 * hme.getValue());
					heavyHitters.add(hh);
					s = hh.getLower();

				}

				if (s != 0 && i + 1 <= numOfCounters) {
					if (oneToOneS.get(generateOTOValue(hme.getKey(), i + 1)) != null) {
						long val = oneToOneS.get(generateOTOValue(hme.getKey(), i + 1));
						oneToOneS.put(generateOTOValue(hme.getKey(), i + 1), s + val);
					} else {
						oneToOneS.put(generateOTOValue(hme.getKey(), i + 1), s);
					}
				}
			}
		}
		return heavyHitters;
	}

	public static void init(double w, double s, double insertProb, int k) {

		if (RHHH10) {
			ignoreProb = 1.0 - insertProb;
			logIgnoreProb = Math.log(ignoreProb);
			minuesQuantity = Math.log(Integer.MAX_VALUE) / logIgnoreProb;
			Random rnd = new Random();
			int rndNum = rnd.nextInt(Integer.MAX_VALUE);
			double x = Math.log((float) rndNum);
			nrIgnore = (int) (x / logIgnoreProb - minuesQuantity);
		}

		epsVal = w;
		for (int i = 0; i < freqCounters.length; i++) {
			if(!checkTime)
				freqCounters[i] = new FreqCount(Math.max(w, Math.pow(2, -levelEps[i])), s);
			else freqCounters[i] = new FreqCount(Math.pow(2, k), s);
		}

	}

	public static void main(String[] args) {

		String fileName = "C:\\Users\\Yanir\\Desktop\\trace";
		String str;

		HashMap<Long, Long> trueCount = new HashMap<Long, Long>();

		for (int k = 20; k <= 25; k++) {
			int numOfPackets = (int)Math.pow(2, k);
			numOfCounters = 1000;
			int threshold = 100000;
			double insertProb = 1.0 / 10;
			int check = 0, sum = 0;

			ipEntries = new Entry[numOfPackets];

			init(1.0 / numOfCounters, threshold / numOfPackets, insertProb,-k);

			try {
				BufferedReader inBuffer = new BufferedReader(new FileReader(fileName));
				for (int i = 0; i < numOfPackets && ((str = inBuffer.readLine()) != null); i += 2) {
					ipEntries[i] = new Entry(createIP(str.substring(0, str.indexOf("\t"))), -1);
					ipEntries[i + 1] = new Entry(createIP(str.substring(str.indexOf("\t"))), -1);

					for (int j = 0; j < 5; j++) {
						if (trueCount.get(ipEntries[i].getIp() & MASKS[j]) != null) {
							check++;
							trueCount.put(ipEntries[i].getIp() & MASKS[j],
									trueCount.get((ipEntries[i].getIp() & MASKS[j])) + 1);
						} else
							trueCount.put(ipEntries[i].getIp() & MASKS[j], (long) 1);

						if (trueCount.get(ipEntries[i + 1].getIp() & MASKS[j]) != null) {
							check++;
							trueCount.put(ipEntries[i + 1].getIp() & MASKS[j],
									trueCount.get((ipEntries[i + 1].getIp() & MASKS[j])) + 1);
						} else
							trueCount.put(ipEntries[i + 1].getIp() & MASKS[j], (long) 1);
					}
				}
			} catch (Exception e) {
				System.out.println("Exception Caught: " + e.toString());
			}
			long start = System.currentTimeMillis();
			for (int i = 0; i < ipEntries.length; i++)
				update(i);
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;

			ArrayList<HeavyHitter> foundHeavyHitters = output(threshold, numOfPackets);

			for (int i = 0; i < foundHeavyHitters.size(); i++) {
				if (trueCount.get(foundHeavyHitters.get(i).getIp()) < foundHeavyHitters.get(i).getLower()
						- numOfPackets / numOfCounters
						|| trueCount.get(foundHeavyHitters.get(i).getIp()) > foundHeavyHitters.get(i).getUpper()
								+ numOfPackets / numOfCounters) {
					int t = 0;
				}
			}

			System.out.println("K= " + -k);
			/*System.out.println("Time In Milliseconds= "+timeElapsed+" Time In Seconds= "+timeElapsed/1000);
			double dTime = (double)timeElapsed/1000;
			double packetsPerSecond =((double)numOfPackets/dTime);
			if(packetsPerSecond>1000000){
				double toPrint1 = (packetsPerSecond/1000000);
				int toPrint2 = (int)((toPrint1-(int)toPrint1)*100);
				System.out.println("Packets Per Second= "+(int)toPrint1+"."+toPrint2+"M");
			}*/
			System.out.println("HHHs: " + foundHeavyHitters.size());
			System.out.println("\n\n");
		}
	}
}
