package screen;

import java.util.ArrayList;
import java.util.Collections;

import util.TimePoint;

public class Screen {

	int w;

	public Screen() {
		w = 0;
	}

	public Screen(int window) {
		w = window;
	}

	// Algorithm 1

	public ArrayList<TimePoint> local(ArrayList<TimePoint> dirtyList, double sMin, double sMax) {
		double min, max;

		for (int k = 1; k < dirtyList.size(); k++) {
			if (k == 0) {
				min = max = dirtyList.get(k).value;
			} else {
				min = dirtyList.get(k - 1).constraint + sMin * (dirtyList.get(k).time - dirtyList.get(k - 1).time);
				max = dirtyList.get(k - 1).constraint + sMax * (dirtyList.get(k).time - dirtyList.get(k - 1).time);
			}
			if (k < dirtyList.size() + w - 1) {
				ArrayList<Double> medianList = new ArrayList<Double>();
				for (int i = k + 1; i < dirtyList.size(); i++) {
					if (dirtyList.get(i).time > dirtyList.get(k).time + w) {
						break;
					}
					medianList.add(dirtyList.get(i).value + sMin * (dirtyList.get(k).time - dirtyList.get(i).time));
					medianList.add(dirtyList.get(i).value + sMax * (dirtyList.get(k).time - dirtyList.get(i).time));
				}
				medianList.add(dirtyList.get(k).value);
				Collections.sort(medianList);
				double mid = medianList.get((medianList.size() / 2));
				if (max < mid) {
					dirtyList.get(k).constraint = max;
				} else if (min > mid) {
					dirtyList.get(k).constraint = min;
				} else {
					dirtyList.get(k).constraint = mid;
				}
			} else {
				dirtyList.get(k).constraint = dirtyList.get(k).value;
			}
		}

		return dirtyList;
	}

	// Algorithm 2

	public ArrayList<TimePoint> update(ArrayList<TimePoint> repairedList, double sMin, double sMax, int l, int k) {
		int i;
		for (i = l - 1; i >= 0; i--) {
			ArrayList<Double> medianList = new ArrayList<Double>();
			if (repairedList.get(i).time < repairedList.get(l).time - w) {
				break;
			}
			double cond = (repairedList.get(i).constraint - repairedList.get(l).value)
					/ (repairedList.get(i).time - repairedList.get(l).time);
			if (cond < sMin || cond > sMax) {
				medianList
						.add(repairedList.get(l).value + sMin * (repairedList.get(i).time - repairedList.get(l).time));
				medianList
						.add(repairedList.get(l).value + sMax * (repairedList.get(i).time - repairedList.get(l).time));
				medianList.add(repairedList.get(i).value);
				Collections.sort(medianList);
				repairedList.get(i).constraint = medianList.get((medianList.size() / 2));
			}
		}
		for (int j = i + 1; j <= k; j++) {
			double min = repairedList.get(j - 1).constraint
					+ sMin * (repairedList.get(j).time - repairedList.get(j - 1).time);
			double max = repairedList.get(j - 1).constraint
					+ sMax * (repairedList.get(j).time - repairedList.get(j - 1).time);
			if (min <= max) {
				double mid = repairedList.get(j).value;
				if (max < mid) {
					repairedList.get(l).constraint = max;
				} else if (min > mid) {
					repairedList.get(l).constraint = min;
				} else {
					repairedList.get(l).constraint = mid;
				}
			}
		}
		return repairedList;
	}

	// Algorithm 3

	public ArrayList<TimePoint> heuristic(ArrayList<TimePoint> repairedList, double sMin, double sMax, int l, int k) {

		double min = Math.max(
				repairedList.get(l - 1).constraint + sMin * (repairedList.get(l).time - repairedList.get(l - 1).time),
				repairedList.get(l + 1).constraint + sMax * (repairedList.get(l).time - repairedList.get(l + 1).time));
		double max = Math.min(
				repairedList.get(l - 1).constraint + sMax * (repairedList.get(l).time - repairedList.get(l - 1).time),
				repairedList.get(l + 1).constraint + sMin * (repairedList.get(l).time - repairedList.get(l + 1).time));

		if (min <= max) {
			double mid = repairedList.get(l).value;
			if (max < mid) {
				repairedList.get(l).constraint = max;
			} else if (min > mid) {
				repairedList.get(l).constraint = min;
			} else {
				repairedList.get(l).constraint = mid;
			}
			return repairedList;
		} else {
			System.out.println("Calling for Algorithm 2.");
			return update(repairedList, sMin, sMax, l, k);
		}
	}

}
