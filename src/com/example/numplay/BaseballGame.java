package com.example.numplay;


import java.util.*;

public class BaseballGame {
    // 정답숫자. Set
    private Set<Integer> correctNumberSet = new HashSet<>(); // 정답숫자 . Set
    private List<Integer> correctNumberList = new ArrayList<>(); // get() contains() 쓰기위해서 만듦.
    private Scanner scanner = new Scanner(System.in); // 스캐너

    // 객체 생성시 정답을 만들도록 함
    public BaseballGame() {
        correctNumberSet = randomNumber();
        correctNumberList.addAll(correctNumberSet); // get(), contains() 메서드 쓰려고 만들었음.
    }

    // 랜덤숫자 생성 메서드. 객체 생성될때 정답숫자생성함. Set 으로 저장.
    private Set<Integer> randomNumber() {
        while (correctNumberSet.size() < 3) {
            Random random = new Random(); // 랜덤 생성
            int digit = random.nextInt(9) + 1; // nextInt(9) 는 0~8 숫자 생성. 그래서 +1 해줌
            correctNumberSet.add(digit); // Set에 추가.
        }
        return correctNumberSet; // correctNumber.size() 가 3이되면 return.
    }

    public int play() {
        int tryGames = 0;
        // 게임시작
        System.out.println("< 게임을 시작합니다 >");
        System.out.println("3자리 숫자를 입력해주세요 !");

        while (true) {
            // 1. 유저에게 입력값을 받음
            String userInput = scanner.nextLine();
            // 2. 올바른 입력값을 받았는지 검증 . validateInput
            if (!validateInput(userInput)) { // false 를 리턴받았을경우 다시 실행.
                System.out.println("잘못된 입력값입니다. 다시 입력해주세요.");
                continue;
            } // true 값이 나오면 아래 진행.

            // 3. 게임 진행횟수 증가
            tryGames++;

            // 4. 스트라이크 개수 계산
            int strikes = countStrike(userInput);

            // 5. 정답여부 확인, 만약 정답이면 break 를 이용해 반복문 탈출
            if (strikes == 3) {
                System.out.println("정답입니다!");
                System.out.println(tryGames + "번째 시도에 정답을 맞췄어요!");
                break;
            }

            // 6. 볼 개수 계산
            int balls = countBall(userInput);

            // 7. 힌트 출력
            if (strikes == 0 && balls == 0) {
                System.out.println("아웃!");
            } else {
                System.out.println(strikes + "스트라이크 " + balls + "볼 입니다!");
            }

        }
        // 게임 진행횟수 반환
        return tryGames;
    }

    // 옳바른 값인지 판단하는 메서드.
    protected boolean validateInput(String input) {
        if (input.length() != 3 || !input.matches("[1-9]{3}")) { // ⭐️String matches()⭐️
            return false;
        }
        Set<Character> digitSet = new HashSet<>(); // 중복값 있는지 없는지 판단하기위해.
        for (char digit : input.toCharArray()) { // 문자 하나씩 넣기
            if (!digitSet.add(digit)) { // 중복값 체크? 맞는로직인가.? 안들어가면 false.
                return false;
            }
        }
        return true; // 위에 전부 해당하지않으면 true 리턴.
    }

    // 스트라이크 카운트
    private int countStrike(String input) {
        int strikes = 0;
        List<Integer> userInputList = new ArrayList<>();

        for (char digit : input.toCharArray()) {
            userInputList.add(Character.getNumericValue(digit)); // ⭐️Character.getNumericValue()⭐️
        }

        for (int i=0; i<3; i++) {
            if (userInputList.get(i).equals(correctNumberList.get(i)) ) {
                strikes++;
            }
        }
        return strikes;
    }

    // 볼 카운트 . 스트라이크카운터랑 거의 똑같은로직.
    private int countBall(String input) {
        int balls = 0;
        List<Integer> userInputList = new ArrayList<>();

        for (char digit : input.toCharArray()) {
            userInputList.add(Character.getNumericValue(digit)); // ⭐️Character.getNumericValue()⭐️
        }

        for (int i=0; i<3; i++) {
            if (correctNumberList.contains(userInputList.get(i)) && !userInputList.get(i).equals(correctNumberList.get(i))) {
                balls++;
            }
        }
        return balls;
    }


}