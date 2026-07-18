import java.util.*;

class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for(int element : nums1){
            if(map.containsKey(element)){
                map.put(element , map.get(element)+1);
            }else{
                map.put(element,1);
            }
        }

        for(int element : nums2){
            if((map.containsKey(element)) && (map.get(element) > 0)){
                list.add(element);
                map.put(element , map.get(element)-1);
            }
        }

        int[] result = new int[list.size()];

        for(int i = 0; i<list.size() ; i++){
            result[i] = list.get(i);
        }

        return result;
    }
}