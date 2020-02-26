package com.eteks.plugins;

import java.util.ArrayList;
import java.util.List;

class Combinations {
	private ArrayList<Combination> combinations = new ArrayList<Combination>();
    void sum_up_recursive(List<Float> numbers, float target, List<Float> partial) {
       int s = 0;
       for (float x: partial) s += x;
       if (s == target) {
    	   combinations.add(new Combination(partial, target));
           //System.out.println("sum("+Arrays.toString(partial.toArray())+")="+target);
       }
       if (s >= target)
            return;
       for(int i=0;i<numbers.size();i++) {
             List<Float> remaining = new ArrayList<Float>();
             float n = numbers.get(i);
             for (int j=i; j<numbers.size();j++) remaining.add(numbers.get(j));
             List<Float> partial_rec = new ArrayList<Float>(partial);
             partial_rec.add(n);
             sum_up_recursive(remaining,target,partial_rec);
       }
    }
    void sum_up(List<Float> numbers, float target) {
    	sum_up_recursive(numbers,target,new ArrayList<Float>());
    }
    
    public ArrayList<Combination> getCombinations() {
		return combinations;
	}
    
}