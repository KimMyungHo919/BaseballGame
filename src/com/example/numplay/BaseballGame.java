package com.example.numplay;


import java.util.*;

public class BaseballGame {
    // 변수선언
    private Set<Integer> correctNumberSet = new HashSet<>();
    private List<Integer> correctNumberList = new ArrayList<>();
    private List<Integer> recordList = new ArrayList<>();
    private BaseballGameDisplay gameDisplay = new BaseballGameDisplay();
    private Scanner scanner = new Scanner(System.in);

    // int형 매개변수 figure를 받아서, 그 숫자에 맞는 자리수로 중복되지 않는 랜덤숫자를 만들어 Set에 넣어주는 메서드.
    private Set<Integer> randomNumber(int figure) {
        while (correctNumberSet.size() < figure) {
            Random random = new Random();
            int digit = random.nextInt(9) + 1;
            correctNumberSet.add(digit);
        }
        return correctNumberSet;
    }
    /*
    메뉴를 보여주는 메서드.
    안에서 scanner로 사용자에게 숫자입력을 받고, 그 숫자에 맞게 switch문으로 처리.
     */
    public void showMenu() {
        System.out.println("환영합니다! 원하시는 번호를 입력해주세요");
        System.out.println("0. 자릿수로 난이도 조절하기");
        System.out.println("1. 게임 시작하기");
        System.out.println("2. 기록보기");
        System.out.println("3. 게임 종료하기");
        String userChoice = scanner.nextLine();

        switch (userChoice) {
            case "0":
                System.out.println("자리수를 선택해주세요! (3~5자리)");
                try {
                    int figure = scanner.nextInt();
                    scanner.nextLine();

                    if (figure == 3 || figure == 4 || figure == 5) {
                        playGame(figure);
                    } else {
                        System.out.println("자릿수를 잘못 입력했습니다! 처음부터 다시 시작합니다.\n");
                        showMenu();
                        return;
                    }
                } catch (InputMismatchException error) {
                    System.out.println("숫자를 입력해야합니다! 메뉴로 돌아갑니다.");
                    System.out.println("에러메세지 : " + error.getMessage() + "\n");
                    scanner.nextLine();
                    showMenu();
                }
                break;
            case "1":
                playGame(3);
                break;
            case "2":
                showRecord();
                break;
            case "3":
                System.out.println("< 숫자야구게임을 종료합니다. >");
                scanner.close();
                return;
            default:
                System.out.println("잘못 입력하셨습니다! 다시 시작합니다.");
                showMenu();
                break;
        }
    }

    // 현재까지의 게임 기록을 보여주는 메서드.
    public void showRecord() {
        if (recordList.isEmpty()) {
            System.out.println("아직 게임을 진행하지 않았습니다! 게임을 진행해주세요!");
        }

        for (int i = 1; i <= recordList.size(); i++) {
            System.out.println(i + "번째 게임 : 시도횟수 - " + recordList.get(i - 1));
        }

        System.out.println();
        showMenu();
    }

    // int형 매개변수 figure. 게임진행 메서드.
    public int playGame(int figure) {
        correctNumberSet = randomNumber(figure);
        correctNumberList.addAll(correctNumberSet);
        Collections.shuffle(correctNumberList);

        int tryGames = 0;

        System.out.println("< 게임을 시작합니다 >");
        System.out.println(figure + "자리 숫자를 입력해주세요 !");

        while (true) {
            // 1. 유저에게 입력값을 받음
            String userInput = scanner.nextLine();
            // 2. 올바른 입력값을 받았는지 검증
            if (!validateInput(userInput)) {
                System.out.println("잘못된 입력값입니다. 다시 입력해주세요.");
                continue;
            }
            // 3. 게임 진행횟수 증가
            tryGames++;
            // 4. 스트라이크 개수 계산
            int strikes = countStrike(userInput);
            // 5. 정답여부 확인
            if (strikes == figure) {
                System.out.println("홈런 !");
                System.out.println(tryGames + "번째 시도에 정답을 맞췄어요!\n");

                recordList.add(tryGames);
                correctNumberSet.clear();
                correctNumberList.clear();
                break;
            }
            // 6. 볼 개수 계산
            int balls = countBall(userInput);
            // 7. 힌트 출력
            gameDisplay.displayHint(strikes, balls);
        }

        System.out.println("게임이 종료되었어요! 다시 메뉴로 돌아갑니다.\n");
        showMenu();

        return tryGames;
    }

    // 게임진행시 유저의 입력값이 형식에 맞는지 판단하는 메서드.
    protected boolean validateInput(String input) {
        Set<Character> userInputSet = new HashSet<>();

        if (!input.matches("[1-9]{3,5}")) {
            return false;
        }

        for (char digit : input.toCharArray()) {
            if (!userInputSet.add(digit)) {
                return false;
            }
        }
        return true;
    }

    // 스트라이크 수를 계산하는 메서드
    private int countStrike(String input) {
        int strikes = 0;
        List<Integer> userInputList = new ArrayList<>();

        for (char digit : input.toCharArray()) {
            userInputList.add(digit - '0');
        }

        for (int i = 0; i < input.length(); i++) {
            if (userInputList.get(i).equals(correctNumberList.get(i))) {
                strikes++;
            }
        }
        return strikes;
    }

    // 볼 수를 계산하는 메서드
    private int countBall(String input) {
        int balls = 0;
        List<Integer> userInputList = new ArrayList<>();

        for (char digit : input.toCharArray()) {
            userInputList.add(digit - '0');
        }

        for (int i = 0; i < input.length(); i++) {
            if (correctNumberList.contains(userInputList.get(i)) && !userInputList.get(i).equals(correctNumberList.get(i))) {
                balls++;
            }
        }
        return balls;
    }
}
