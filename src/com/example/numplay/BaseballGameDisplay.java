package com.example.numplay;

public class BaseballGameDisplay {
    public void displayHint(int strike, int ball) {
        if (strike == 0 && ball == 0) {
            System.out.println("아웃!");
        } else {
            System.out.println(strike + "스트라이크 " + ball + "볼 입니다!");
        }
    }
}
