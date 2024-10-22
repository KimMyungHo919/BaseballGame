package com.example.numplay;


import java.util.*;

public class BaseballGame {
    // 정답숫자. Set
    private Set<Integer> correctNumberSet = new HashSet<>(); // 정답숫자 . Set
    private List<Integer> correctNumberList = new ArrayList<>(); // get() contains() 쓰기위해서 만듦.
    private List<Integer> recordList = new ArrayList<>(); // 기록 보관할 List
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

    public void showMenu() { // 시작메뉴 보여주기.
        System.out.println("환영합니다! 원하시는 번호를 입력해주세요");
        System.out.println("1. 게임 시작하기");
        System.out.println("2. 기록보기");
        System.out.println("3. 게임 종료하기");
        String userChoice = scanner.nextLine();

        if (userChoice.equals("1")) {
            play();
        } else if (userChoice.equals("2")) {
            record();
        } else if (userChoice.equals("3")) {
            System.out.println("< 숫자야구게임을 종료합니다. >");
            System.exit(0); // 2입력받을시 프로그램 종료.
        } else {
            System.out.println("잘못 입력하셨습니다! 다시 시작합니다.");
            showMenu();
        }
    }

    public void record() { // 시도횟수를 출력하는 메서드.
        if (recordList.size() == 0) {
            System.out.println("아직 게임을 진행하지 않았습니다! 게임을 진행해주세요!"); // 게임을 하나도 진행하지않았을때 보여주는 문구.
        }
        for (int i=1; i<=recordList.size(); i++) {
            System.out.println(i + "번째 게임 : 시도횟수 - " + recordList.get(i-1));
        }
        System.out.println(); // for문안에 있던 \n 을 밖에 꺼냈음. 게임기록볼때 가독성이 떨어짐.
        showMenu();
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
                System.out.println("홈런 !");
                System.out.println(tryGames + "번째 시도에 정답을 맞췄어요!\n");
                recordList.add(tryGames); // 정답맞췄을때 기록리스트에 추가하기.
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
        System.out.println("게임이 종료되었어요! 다시 메뉴로 돌아갑니다.\n");
        showMenu();
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
