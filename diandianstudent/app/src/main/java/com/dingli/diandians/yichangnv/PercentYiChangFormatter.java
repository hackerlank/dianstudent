package com.dingli.diandians.yichangnv;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by dingliyuangong on 2017/3/21.
 */

public class PercentYiChangFormatter implements IValueFormatter {
    protected DecimalFormat mFormat;
    public PercentYiChangFormatter(){
        mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value>4){
            return mFormat.format(value) + " %";
        }else{
            return "";
        }
    }
}
