package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utility {
  public static String PATH = "data/";

  /**
   * Basic attributes: timestamp, dirty, truth
   *
   * @param filename filename
   * @param index which column besides timestamp should be read
   * @param splitOp to split up the lines
   * @return data in timeseries form
   */
  public ArrayList<TimePoint> readData(String filename,String filePath, int index, String splitOp) throws Exception {
      if (filename == null || filename.isEmpty() || filePath == null || filePath.isEmpty())
          throw  new Exception("Invalid File name or Invalid File Path !!");
      ArrayList<TimePoint> list = new ArrayList<>();

      File datafile = new File(filePath + filename);
      InputStream is = new FileInputStream(datafile);
      try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
          String line;
          while ((line = br.readLine()) != null) {
              String[] dataPoints;
              if (splitOp != null && !splitOp.isEmpty()) {
                  dataPoints = line.split(Pattern.quote(splitOp));
              } else {
                  dataPoints = line.split(Pattern.quote(","));
              }
              TimePoint dataPoint = new TimePoint(Long.parseLong(dataPoints[0]), Double.parseDouble(dataPoints[index]));
              list.add(dataPoint);
          }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return list;
  }


  public double calcRMS(ArrayList<TimePoint> truthSeries, ArrayList<TimePoint> resultSeries) {
	    double cost = 0;
	    double delta;
	    int len = truthSeries.size();

	    for (int index = 0; index < len; index++) {
	      delta = resultSeries.get(index).constraint
	          - truthSeries.get(index).value;

	      cost = cost + delta * delta;
	    }
	    cost = cost / len;

	    return Math.sqrt(cost);
	  }

}
