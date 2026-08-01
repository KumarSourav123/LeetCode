class Solution {
    public boolean isPalindrome(int x) {
        int i = x;
        int rem = 0;
        int value = 0;

        while(i > 0){
            rem = i%10;
            value = (value*10) + rem;
            i = i/10;
        }

        if(value == x){
            return true;
        }
        else{
            return false;
        }
    }
}