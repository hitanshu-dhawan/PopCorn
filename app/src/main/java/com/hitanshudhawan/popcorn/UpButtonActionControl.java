package com.hitanshudhawan.popcorn;

/**
 * Created by soumyajit on 6/10/17.
 */

public class UpButtonActionControl {
    private static String upButtonState = "Movie";

    public static String getUpButtonState(){
        return upButtonState;
    }

    public static void setUpButtonState(String state){
         upButtonState = state;
    }

}
