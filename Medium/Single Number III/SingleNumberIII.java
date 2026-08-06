class Solution {
    public int[] singleNumber(int[] nums) {
        int ans = 0;

        for(int i = 0 ; i < nums.length ; i++){
            ans = ans ^ nums[i];
        }
        
        int rightMost = (ans & ans-1) ^ ans;
        int a = 0;
        int b = 0;

        for(int i = 0 ; i < nums.length ; i++){
            if((nums[i] & rightMost) == 0){
                a = a ^ nums[i];
            }else{
                b = b ^ nums[i];
            }
        }

        return new int[]{a,b};
    }
}
