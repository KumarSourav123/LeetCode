import java.util.*;

class Solution {
    public int longestConsecutive(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int length = 0;
        for (int num : nums) {
            if (map.containsKey(num)) {
                continue;
            }

            int left = map.getOrDefault(num - 1, 0);
            int right = map.getOrDefault(num + 1, 0);

            int total = left + right + 1;

            map.put(num, total);

            if (left > 0) {
                map.put(num - left, total);
            }

            if (right > 0) {
                map.put(num + right, total);
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue() > length){
                length = entry.getValue();
            }

        }
        return length;
    }
}