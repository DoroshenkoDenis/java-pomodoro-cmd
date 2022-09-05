package com.doroshenko;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PomodoroTimer {

    /**
     * input: -w (work); 1 (minutes); -b (brake); 1 (minutes); -count (кол-во подходов); 2 (подхода);
     * -m (умножает время следующего подходада на m); 2 (множитель m)
     * output: timer is run
     * N minutes is over
     * output: it is time to rest
     * output: timer has been ended
     * input: --help
     * output: instruction
     */
    public static void main(String[] args) throws InterruptedException {
        runPomodoro();
    }

    //    default settings
    static boolean isTest = false;
    static boolean isHelp = false;
    static int workTime = 1;
    static int breakTime = 1;
    static int count = 1;
    static int multiplier = 1;
    static int sizeBreak = 15;
    static int sizeWork = 30;

    public static void runPomodoro() throws InterruptedException {
        System.out.println("Введите команды и время работы и отдыха через пробел");
        //      получаем массив команд-токенов вводом в консоль: [-w, 1, -b, 1, --help, -count, 2, -m, 2]
        var cmd = new Scanner(System.in).nextLine().split(" ");
        //      привязываем вводимые команды к логике ввода
        for (var i = 0; i < cmd.length; i++) {
            switch (cmd[i]) {
                case "-w" -> workTime = Integer.parseInt(cmd[++i]);
                case "-b" -> breakTime = Integer.parseInt(cmd[++i]);
                case "-count" -> count = Integer.parseInt(cmd[++i]);
                case "-m" -> multiplier = Integer.parseInt(cmd[++i]);
                case "--help" -> {
                    printHelpMsg();
                    isHelp = true;
                }
            }
        }
        if (isHelp) return; // вернёт void, если ызвана команда --help
        runBenchmark(workTime, breakTime, sizeWork, sizeBreak, count, multiplier);
    }

    private static void runBenchmark(int workTime, int breakTime, int sizeWork, int sizeBreak, int count, int multiplier) throws InterruptedException {
        //      benchmark - сколько времени работало приложение
        long startTime = System.currentTimeMillis();
        int m = 1;
        //      здесь работает код timer() c учётом количества итераций и множителя
        System.out.printf("Начинаем работать с %d min, " +
                "отдывахем %d min, кол-во подходов %d, множитель %d\n", workTime, breakTime, count, multiplier);
        for (int i = 1; i <= count; i++) {
            if (m <= multiplier) {
                timer(workTime * m, breakTime, sizeWork, sizeBreak);
                m++;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Running time is: " + (endTime - startTime) / (1000 * 60) + " minutes");
    }

// progress bar for work and break
    public static void timer(int workTime, int breakTime, int sizeWork, int sizeBreak) throws InterruptedException {
        // длина рисунка progress bar
        printProgress("Work Progress::  ", workTime, sizeWork);
        printProgress("Break Progress:: ", breakTime, sizeBreak);
    }

// progress bar
    private static void printProgress(String process, int time, int size) throws InterruptedException {
        int length;
        int rep;
        length = 60 * time / size;
        rep = 60 * time / length;
        int stretch = size / (3 * time);
        for (int i = 1; i <= rep; i++) {
            double x = i;
            x = 1.0 / 3.0 * x;
            x *= 10;
            x = Math.round(x);
            x /= 10;
            double w = time * stretch;
            double percent = (x / w) * 1000;
            x /= stretch;
            x *= 10;
            x = Math.round(x);
            x /= 10;
            percent = Math.round(percent);
            percent /= 10;
            System.out.print(process + percent + "% " + (" ").repeat(5 - (String.valueOf(percent).length())) + "[" + ("#").repeat(i) + ("-").repeat(rep - i) + "]    ( " + x + "min / " + time + "min )" + "\r");
            if (!isTest) {
                TimeUnit.SECONDS.sleep(length);
            }
        }
        System.out.println();
    }

    private static void printHelpMsg() {
        System.out.println("""
                            \nPomodoro -  Time tracker
                            -w <time>: время работы
                            -b <time>: время отдыха
                            -count <count>: количество итераций
                            -m <count>: множитель рабочего времени для следующей итерации
                            --help - помощь
                            """);
    }

}
