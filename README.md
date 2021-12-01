# Sigmod
------------------
Code for the paper titled ["SCREEN: Stream Data Cleaning under Speed Constraints."](https://dl.acm.org/citation.cfm?doid=2723372.2723730) released in SIGMOD 2015. This paper has been implemented by Rajkumar Chepala (RC21U) and Bhimesh Vittal Kandibedala (BVK21) (grad students currently enrolled at FSU) using Java.

The description of code files are as following:

- `Screen.java`: Algorithm 1 (local), algorithm 2 (update) and algorithm 3 (heuristic) in the paper. The three SCREEN algorithms can be used to repair time series data using constraints known as Speed Constraints.
- `ScreenTest.java`: Class containing methods to run test cases on the algorithms mentioned above.
- `TimePoint.java`: Class for TimePoint indicating a time point.
- `Utility.java`: Class to load data and to calculate RMS value.

Datasets
---------
STOCK public dataset with synthetic errors mentioned in the paper has been gathered from Yahoo. This dataset is used for all the test cases. Another dataset containing fuel information is obtained from Kaggle and the errors have been manually entered by us.

The schema of the data files contain three columns, 

- timestamp: the timestamp of the data
- dirty: the observation
- truth: the truth

Location:

- The STOCK and FUEL dataset can be found in `data/`.

Parameters
-----------
The input and output of **Screen** algorithm 1 (local) is:

Method

```
local(dirtyList, sMax, sMin)
```

Input:

```
ArrayList<TimePoint> dirtyList
double sMax = 6 // maximum speed
double sMin = -6 // minimum speed
```

Output

```
ArrayList<TimePoint> resultList
```
The input and output of **Screen** algorithm 2 (update) is:

Method

```
update(repairedList, sMax, sMin, l, k)
```

Input:

```
ArrayList<TimePoint> repairedList
double sMax = 6 // maximum speed
double sMin = -6 // minimum speed
int l // delayed data point position
int k // first k no.of data points
```

Output

```
ArrayList<TimePoint> resultList
```
The input and output of **Screen** algorithm 3 (heuristic) is:

Method

```
heuristic(repairedList, sMax, sMin, l, k)
```

Input:

```
ArrayList<TimePoint> repairedList
double sMax = 6 // maximum speed
double sMin = -6 // minimum speed
int l // delayed data point position
int k // first k no.of data points
```

Output

```
ArrayList<TimePoint> resultList
```

Note:

- The sMin, sMax and window size for FUEL dataset is -10, 10 and 5 respectively.
