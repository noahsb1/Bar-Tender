package Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class HashMapToString {
    public static String hashMapToString(HashMap<String, HashMap<String, ArrayList<String>>> hashMap) {
        ArrayList<String> temp1 = SetToArrayList.setToArrayList(hashMap.keySet());
        String rtn = "";
        for(String category : temp1) {
            rtn += category + "{";
            ArrayList<String> temp2 = SetToArrayList.setToArrayList(hashMap.get(category).keySet());
            for(int j = 0; j < temp2.size(); j++) {
                rtn += temp2.get(j) + ":";
                ArrayList<String> temp3 = hashMap.get(category).get(temp2.get(j));
                for(int i = 0; i < temp3.size(); i++) {
                    if(i != temp3.size() - 1) {
                        rtn += temp3.get(i) + ";";
                    } else {
                        rtn += temp3.get(i);
                    }
                }
                if(j != temp2.size() - 1) {
                    rtn += "~~~";
                }
            }
            rtn += "}!";
        }
        return rtn;
    }
}
