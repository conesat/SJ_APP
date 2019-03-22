package com.hg.ui.tools;

public class NumAbbreviation {
    public static String getNumAbbreviation(Long num){
        String rs;
        if (num==null)
            return "0";
        if (num>10000){
            rs=String.format("%.1f", num/10000.0)+"万";
        }else {
            rs=num+"";
        }
        return rs;
    }
}
