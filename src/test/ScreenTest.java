package test;

import java.util.ArrayList;

import screen.Screen;
import util.TimePoint;
import util.Utility;

public class ScreenTest {
	
	private static final String filePath = "data/";

	public static void main(String[] args) throws Exception {
		localTest();
		localTestOnFuel();
		updateTest();
		heuristicTest();
		combiningUpdate();
		combinedHeuristic();
	}

	//Working of algorithm 1 on STOCKS dataset
	public static void localTest() throws Exception {

		System.out.println("Running algorithm 1: ");

		String inputFileName = "stock10k.data";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName, filePath,1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName, filePath,2, splitOp);

		double rmsDirty = utility.calcRMS(truth, dirty);
		System.out.println("Dirty RMS: " + rmsDirty);

		double sMax = 6;
		double sMin = -6;
		int w = 1;
		Screen screen = new Screen(w);

		long start = System.currentTimeMillis();
		ArrayList<TimePoint> result = screen.local(dirty, sMin, sMax);

		double rms = utility.calcRMS(truth, result);
		System.out.println("Local RMS: " + rms);

		System.out.println("Accuracy of algorithm 1 for every 1000 points cleaned: ");
		int wrong = 0;
		for (int i = 0; i < 10000; i = i + 1000) {
			int t = i + 1000;
			for (int j = i; j < i + 1000; j++) {
				if (truth.get(j).value != result.get(j).constraint) {
					wrong++;
				}
			}
			int per = t - wrong;
			double temp = per * 100;
			double accuracy = temp / t;
			System.out.println(accuracy);
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time in milli seconds: " + (end - start));
	}

	//Working of algorithm 1 on FUEL dataset
	public static void localTestOnFuel() throws Exception {

		System.out.println("Running algorithm 1: ");

		String inputFileName = "fuel.txt";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName, filePath,1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName, filePath,2, splitOp);

		double rmsDirty = utility.calcRMS(truth, dirty);
		System.out.println("Dirty RMS: " + rmsDirty);

		double sMax = 10;
		double sMin = -10;
		int w = 5;
		Screen screen = new Screen(w);

		long start = System.currentTimeMillis();
		ArrayList<TimePoint> result = screen.local(dirty, sMin, sMax);

		double rms = utility.calcRMS(truth, result);
		System.out.println("Local RMS: " + rms);

		System.out.println("Accuracy of algorithm 1 for every 1000 points cleaned: ");
		int wrong = 0;
		for (int i = 0; i < 800; i = i + 100) {
			int t = i + 100;
			for (int j = i; j < i + 100; j++) {
				if (truth.get(j).value != result.get(j).constraint) {
					wrong++;
				}
			}
			int per = t - wrong;
			double temp = per * 100;
			double accuracy = temp / t;
			System.out.println(accuracy);
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time in milli seconds: " + (end - start));
	}

	//Working of algorithm 2 on STOCK dataset with delayed timepoint
	public static void updateTest() throws Exception {

		System.out.println("Running algorithm 2: ");

		String inputFileName = "stock10k.data";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName,filePath, 1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName,filePath, 2, splitOp);

		TimePoint tp = dirty.get(6);
		truth.remove(6);
		ArrayList<TimePoint> list = new ArrayList<TimePoint>();
		for (int i = 0; i < 10; i++) {
			list.add(truth.get(i));
		}

		System.out.println(tp.value + " " + tp.constraint + " " + tp.time);

		// Adding delayed point
		list.add(6, tp);

		double sMax = 6;
		double sMin = -6;
		int w = 1;
		Screen screen = new Screen(w);
		ArrayList<TimePoint> result = screen.update(list, sMin, sMax, 6, 7);
		for (int i = 0; i < 10; i++) {
			System.out.println(
					result.get(i).value + " " + result.get(i).constraint + " " + result.get(i).time);
		}

	}

	//Working of algorithm 3 on STOCK dataset with delayed timepoint
	public static void heuristicTest() throws Exception {

		System.out.println("Running algorithm 3: ");

		String inputFileName = "stock10k.data";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName, filePath,1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName, filePath,2, splitOp);

		TimePoint tp = dirty.get(6);
		truth.remove(6);
		ArrayList<TimePoint> list = new ArrayList<TimePoint>();
		for (int i = 0; i < 10; i++) {
			list.add(truth.get(i));
		}

		System.out.println(tp.value + " " + tp.constraint + " " + tp.time);

		// Adding delayed point
		list.add(6, tp);

		double sMax = 6;
		double sMin = -6;
		int w = 1;
		Screen screen = new Screen(w);
		ArrayList<TimePoint> result = screen.heuristic(list, sMin, sMax, 6, 7);
		for (int i = 0; i < 10; i++) {
			System.out.println(
					result.get(i).value + " " + result.get(i).constraint + " " + result.get(i).time);
		}

	}

	//Algorithm 1 and 2 have been integrated and tested on STOCKS dataset 
	public static void combiningUpdate() throws Exception {

		System.out.println("Running algorithm 1 and algorithm 2: ");

		String inputFileName = "stock10k.data";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName,filePath, 1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName,filePath, 2, splitOp);

		double rmsDirty = utility.calcRMS(truth, dirty);
		System.out.println("Dirty RMS: " + rmsDirty);

		double sMax = 6;
		double sMin = -6;
		int w = 1;
		Screen screen = new Screen(w);

		long start = System.currentTimeMillis();
		ArrayList<TimePoint> temp = new ArrayList<TimePoint>();
		for (int i = 0; i < 12000; i = i + 120) {
			TimePoint tp = new TimePoint(dirty.get(i + 40).time, dirty.get(i + 40).value);
			for (int j = i; j < i + 120; j++) {
				if (j == i + 40) {
					continue;
				}
				temp.add(dirty.get(j));
			}
			ArrayList<TimePoint> result = screen.local(temp, sMin, sMax);
			result.add(i + 40, tp);
			ArrayList<TimePoint> ofoResult = screen.update(dirty, sMin, sMax, i + 40, i + 120);
			if (tp.time >= 11921) {
				double rms = utility.calcRMS(truth, ofoResult);
				System.out.println("Update RMS: " + rms);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time in milli seconds: " + (end - start));
	}

	//Algorithm 1 and 3 have been integrated and tested on STOCKS dataset
	public static void combinedHeuristic() throws Exception {

		System.out.println("Running algorithm 1 and algorithm 3: ");

		String inputFileName = "stock10k.data";

		Utility utility = new Utility();
		String splitOp = ",";

		ArrayList<TimePoint> dirty = utility.readData(inputFileName,filePath, 1, splitOp);
		ArrayList<TimePoint> truth = utility.readData(inputFileName,filePath, 2, splitOp);

		double rmsDirty = utility.calcRMS(truth, dirty);
		System.out.println("Dirty RMS: " + rmsDirty);

		double sMax = 6;
		double sMin = -6;
		int w = 1;
		Screen screen = new Screen(w);

		long start = System.currentTimeMillis();
		ArrayList<TimePoint> temp = new ArrayList<TimePoint>();
		for (int i = 0; i < 12000; i = i + 120) {
			TimePoint tp = new TimePoint(dirty.get(i + 40).time, dirty.get(i + 40).value);
			for (int j = i; j < i + 120; j++) {
				if (j == i + 40) {
					continue;
				}
				temp.add(dirty.get(j));
			}
			ArrayList<TimePoint> result = screen.local(temp, sMin, sMax);
			result.add(i + 40, tp);
			ArrayList<TimePoint> ofoResult = screen.heuristic(dirty, sMin, sMax, i + 40, i + 120);
			if (tp.time >= 11921) {
				double rms = utility.calcRMS(truth, ofoResult);
				System.out.println("Heuristic RMS: " + rms);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time in milli seconds: " + (end - start));
	}

}
