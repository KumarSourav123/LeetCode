class Solution {
    public int[] plusOne(int[] digits) {
        int count = 0;
        int last = digits[digits.length-1];
        int[] newDigits = new int[digits.length+1];


        if(last != 9){
            last = last +1;
            digits[digits.length-1] = last;
            return digits;
        }

        for(int i = 0; i<digits.length ; i++){
            if(digits[i] == 9){
                count++; 
            }
        }

        if(count != digits.length){
            for(int i = digits.length-1 ; i >= 0 ; i--){
                if(digits[i] == 9){
                    digits[i] = 0;
                }else{
                    digits[i] = digits[i] + 1;
                    return digits;
                }
            }
        }else{
            newDigits[0] = 1;
            for(int i = 1; i< newDigits.length ; i++){
                newDigits[i] = 0;
            }
            return newDigits;
        }
    return digits;

    }
}