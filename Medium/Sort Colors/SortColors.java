class Solution {
    public void sortColors(int[] nums) {
        int zero = 0;
        int one = 0;
        int two = 0;
        for(int i = 0 ; i < nums.length ; i++){
            if(nums[i] != 0){
                zero=i;
                one=i;
                two=i;
                break;
            }
        }
        while(one < nums.length){
            if(zero < nums.length){

                if(nums[zero] != 0){
                    zero++;
                }else{
                    int temp = nums[two];
                    nums[two] = nums[zero];
                    nums[zero] = temp;
                    zero++;
                    two++;
                }
                
                one = two;
            }else{
                if((nums[two] == 2) && (nums[one] == 1)){
                    int temp = nums[two];
                    nums[two] = nums[one];
                    nums[one] = temp;
                    one++;
                    two++;
                }else{
                    one++;
                    if(nums[two] == 1){
                        two++;
                    }
                }
            }
        }
    }
}