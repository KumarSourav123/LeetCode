class Solution {
    public int maxArea(int[] height) {
        int s = 0;
        int e = height.length-1;
        int area = 0;
        int maximumArea = 0;

        while(s <= e){
            if(height[s] <= height[e]){
                area = height[s] * (e-s);
                s++;
            }else{
                area = height[e] * (e-s);
                e--;
            }

            if(area > maximumArea){
                maximumArea = area;
            }
        }
        return maximumArea;
    }
}