import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RelatedEpisode {
    private static HashMap<Integer, ArrayList<Double>> episodeToVector;

    public HashSet<Integer> getRelated(Integer episode) {
        if (!episodeToVector.containsKey(episode)) {
            return new HashSet<Integer>();
        }

        ArrayList<Double> episodeVector = episodeToVector.get(episode);

        HashMap<Integer, Double> distMap = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, ArrayList<Double>> entry : episodeToVector.entrySet()) {
            ArrayList<Double> currentVector = entry.getValue();
            Double dist = new Double(0);
            for (int i = 0; i < 8; i++) {
                dist += Math.pow(Math.pow(episodeVector.get(i), 2) + Math.pow(currentVector.get(i), 2), 0.5);
            }
            distMap.put(entry.getKey(), dist);
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(distMap.entrySet());
        Collections.sort(list, new Comparator<Object>(){
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<Double>) ((Map.Entry<Integer, Double>) (o2)).getValue()).compareTo(((Map.Entry<Integer, Double>) (o1)).getValue());
            }
        });
        System.out.println(list);
        HashSet<Integer> result = new HashSet<Integer>();
        for (int i = 0; i < 4; i ++) {
            result.add(list.get(i).getKey());
        }
        return result;
    }

    public static void main(String[] args) {
        episodeToVector = new HashMap<Integer, ArrayList<Double>>();

        Scanner fileScanner;
        try {
            fileScanner = new Scanner(new File("output/result"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        while (fileScanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(fileScanner.nextLine());
            Integer episodeId = Integer.parseInt(lineScanner.next());
            episodeToVector.put(episodeId, new ArrayList<Double>());
            while (lineScanner.hasNext()) {
                Double val = Double.parseDouble(lineScanner.next());
                episodeToVector.get(episodeId).add(val);
            }
        }

        System.out.println(episodeToVector);

//        HashSet<Integer> r = getRelated(499);
//        System.out.println(r);
    }
}
