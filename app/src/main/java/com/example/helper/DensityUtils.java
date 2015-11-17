package com.example.helper;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author EdisonChang
 */
public class DensityUtils {

	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

}
