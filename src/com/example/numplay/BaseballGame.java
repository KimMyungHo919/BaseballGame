package com.example.numplay;


import java.util.*;

public class BaseballGame {
    // 정답숫자. Set
    private Set<Integer> correctNumberSet = new HashSet<>(); // 정답숫자 . Set
    private List<Integer> correctNumberList = new ArrayList<>(); // get() contains() 쓰기위해서 만듦.
    private List<Integer> recordList = new ArrayList<>(); // 기록 보관할 List
    private BaseballGameDisplay countStrikesAndBalls = new BaseballGameDisplay();
    private Scanner scanner = new Scanner(System.in); // 스캐너

    // 랜덤숫자 생성 메서드. 객체 생성될때 정답숫자생성함. Set 으로 저장.
    private Set<Integer> randomNumber(int figure) {
        while (correctNumberSet.size() < figure) {
            Random random = new Random(); // 랜덤 생성
            int digit = random.nextInt(9) + 1; // nextInt(9) 는 0~8 숫자 생성. 그래서 +1 해줌
            correctNumberSet.add(digit); // Set에 추가.
        }
        System.out.println(correctNumberSet); // ⭐️⭐️테스트용⭐️⭐️
        return correctNumberSet; // correctNumber.size() 가 3이되면 return.
    }

    public void showMenu() { // 시작메뉴 보여주기.
        System.out.println("환영합니다! 원하시는 번호를 입력해주세요");
        System.out.println("0. 자릿수로 난이도 조절하기");
        System.out.println("1. 게임 시작하기");
        System.out.println("2. 기록보기");
        System.out.println("3. 게임 종료하기");
        String userChoice = scanner.nextLine();

        switch(userChoice) {
            case "0":
                System.out.println("자리수를 선택해주세요! (3~5자리)");
                int figure = scanner.nextInt();
                scanner.nextLine();
                if (figure==3 || figure==4 || figure==5) {
                    play(figure);
                } else {
                    System.out.println("자릿수를 잘못 입력했습니다! 처음부터 다시 시작합니다.\n");
                    showMenu();
                    return;
                }
                break;
            case "1":
                play(3);
                break;
            case "2":
                record();
                break;
            case "3":
                System.out.println("< 숫자야구게임을 종료합니다. >");
                System.exit(0); // 3입력받을시 프로그램 종료
                break;
            default:
                System.out.println("잘못 입력하셨습니다! 다시 시작합니다.");
                showMenu();
                break;
        }
    }

    public void record() { // 시도횟수를 출력하는 메서드.
        if (recordList.isEmpty()) { // == 0 을 메서드로 수정
            System.out.println("아직 게임을 진행하지 않았습니다! 게임을 진행해주세요!"); // 게임을 하나도 진행하지않았을때 보여주는 문구.
        }
        for (int i=1; i<=recordList.size(); i++) {
            System.out.println(i + "번째 게임 : 시도횟수 - " + recordList.get(i-1));
        }
        System.out.println(); // for문안에 있던 \n 을 밖에 꺼냈음. 게임기록볼때 가독성이 떨어짐.
        showMenu();
    }

    public int play(int figure) {
        correctNumberSet = randomNumber(figure); // 게임플레이 시작시에 랜덤숫자 넣기
        correctNumberList.addAll(correctNumberSet); // 게임플레이 시작시에 랜덤숫자 넣기

        int tryGames = 0;
        // 게임시작
        System.out.println("< 게임을 시작합니다 >");
        System.out.println(figure + "자리 숫자를 입력해주세요 !");

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
            if (strikes == figure) {
                System.out.println("홈런 !");
                System.out.println(tryGames + "번째 시도에 정답을 맞췄어요!\n");
                recordList.add(tryGames); // 정답맞췄을때 기록리스트에 추가하기.
                correctNumberSet.clear(); // 정답 맞췄을때 초기화
                correctNumberList.clear(); // 정답 맞췄을때 초기화
                break;
            }

            // 6. 볼 개수 계산
            int balls = countBall(userInput);

            // 7. 힌트 출력
            countStrikesAndBalls.displayHint(strikes, balls);

        }
        System.out.println("게임이 종료되었어요! 다시 메뉴로 돌아갑니다.\n");
        showMenu();
        // 게임 진행횟수 반환
        return tryGames;
    }

    // 옳바른 값인지 판단하는 메서드.
    protected boolean validateInput(String input) {
        if (!input.matches("[1-9]{3,5}")) { // ⭐️String matches()⭐️
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
            userInputList.add(digit - '0'); // 숫자의 아스키코드. '0' - 48
        }

        for (int i=0; i<input.length(); i++) {
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
            userInputList.add(digit - '0');
        }

        for (int i=0; i<input.length(); i++) {
            if (correctNumberList.contains(userInputList.get(i)) && !userInputList.get(i).equals(correctNumberList.get(i))) {
                balls++;
            }
        }
        return balls;
    }


}
