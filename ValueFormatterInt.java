package com.csudh.healthapp.csudhhealthapp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Darshit on 11/30/2017.
 */

public class ValueFormatterInt implements ValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "" + ((int) value);
    }
}
