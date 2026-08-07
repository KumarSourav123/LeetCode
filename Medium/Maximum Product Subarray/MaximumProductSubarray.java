class Solution {
    public int maxProduct(int[] nums) {
        int totalMax = Integer.MIN_VALUE;
        int prefix = 1;
        int suffix = 1;
        
        for(int i = 0 ; i < nums.length ; i++){
            prefix = prefix * nums[i];
            suffix = suffix * nums[nums.length-1-i];
            if(prefix > totalMax){
                totalMax = prefix;
            }
            if(suffix > totalMax){
                totalMax = suffix;
            }
            if(prefix == 0){
                prefix = 1;
            }
            if(suffix == 0){
                suffix = 1;
            }
        }
        
        return totalMax;
    }
}