package com.ddcb.utils;

import java.sql.Timestamp;

public class RealCostUtil {
	
	public static int getRealCost(Object cost, Object starttime, Object deadline, Object rebate) {
		int cost_ = Integer.valueOf((String)cost);
		
		if(starttime == null || deadline == null || rebate == null) {
			return cost_; 
		}
		
		long starttime_ = Timestamp.valueOf(starttime.toString()).getTime();
		long deadline_ = Timestamp.valueOf(deadline.toString()).getTime();
		int rebate_ = Integer.valueOf((String)rebate);
		
		long now = System.currentTimeMillis();
		if(now>starttime_ && now<deadline_) {
			cost_ = cost_*rebate_/10;
		}
		return cost_;
	}
}
