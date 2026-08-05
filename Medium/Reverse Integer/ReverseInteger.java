class Solution {
    public int reverse(int x) {
        int num = Math.abs(x);
        int rem = 0;
        int val = 0;

        while(num > 0){
            rem = num % 10;
            if (val > Integer.MAX_VALUE / 10 || 
                (val == Integer.MAX_VALUE / 10 && rem > 7)) {
                return 0;
            }
            if (val < Integer.MIN_VALUE / 10 || 
                (val == Integer.MIN_VALUE / 10 && rem < -8)) {
                return 0;
            }
            val = (val*10) + rem;
            num = num/10;
        }
        if(x > 0){
            return val;
        }if(x < 0){
            return -val;
        }
        return 0;
    }
}