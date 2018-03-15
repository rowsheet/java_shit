package com.Common;

import java.util.ArrayList;

public class Color {

    private float CalculateLuminance(ArrayList<Integer> rgb){
        return (float) (0.2126*rgb.get(0) + 0.7152*rgb.get(1) + 0.0722*rgb.get(2));
    }

    private ArrayList<Integer> HexToRBG(String colorStr) {
        ArrayList<Integer> rbg = new ArrayList<Integer>();
        rbg.add(Integer.valueOf( colorStr.substring( 1, 3 ), 16 ));
        rbg.add(Integer.valueOf( colorStr.substring( 3, 5 ), 16 ));
        rbg.add(Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
        return rbg;
    }

    public String getInverseBW(String hex_color) {
        float luminance = this.CalculateLuminance(this.HexToRBG(hex_color));
        String inverse = (luminance < 140) ? "#ffffff" : "#000000";
        return inverse;
    }

}
