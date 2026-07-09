class Solution {
    public int[] twoSum(int[] nums, int target) {

        HashMap<Integer, Integer> map = new HashMap<>();
        int num;

        for(int i = 0 ; i<nums.length ; i++){
            num = target - nums[i];

            if(map.containsKey(num)){
                return new int[]{i,map.get(num)};
            }
            map.put(nums[i],i);
            
        }
        return new int[]{};
    }
}