package com.eteks.plugins;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Combination {
	List<Float> combination;
	float sum;
	public Combination(List<Float> combination, float sum) {
		this.combination = combination;
		this.sum = sum;
	}
	@Override
	public String toString() {
		return "sum("+Arrays.toString(combination.toArray())+")="+sum;
	}
	public List<Float> getCombination() {
		return combination;
	}
	public float getSum() {
		return sum;
	}
	public float getDiff()
	{
		sort();
		return combination.get(0) - combination.get(combination.size() - 1);
	}
	public void sort()
	{
		combination.sort(Collections.reverseOrder());
	}

}
