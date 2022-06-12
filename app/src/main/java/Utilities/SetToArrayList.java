package Utilities;

import java.util.ArrayList;
import java.util.Set;

public class SetToArrayList {
    public static ArrayList<String> setToArrayList(Set<String> keySet) {
        Object[] hashMapKeysArray = keySet.toArray();
        ArrayList<String> hashMapKey = new ArrayList<>();
        for (Object str : hashMapKeysArray) {
            hashMapKey.add((String) str);
        }
        return hashMapKey;
    }
}
