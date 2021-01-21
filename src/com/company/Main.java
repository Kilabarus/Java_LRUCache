package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * Ввод целого числа в диапазоне с выводом, если требуется, сообщения
     *      Могут быть заданы как и 2 границы диапазона, так и одна, также границы диапазона могут быть не заданы вообще
     *
     * @param msg   сообщение, печатающееся перед вводом
     * @param min   нижняя граница диапазона
     * @param max   верхняя граница диапазона
     *
     * @return      целое число в заданном дапазоне
     * @throws IOException
     */
    public static Integer inputInteger(String msg, Integer min, Integer max) throws IOException {
        if ((min != null) && (max != null) && (min > max)) {
            throw new IllegalArgumentException("Параметр min должен быть не больше max");
        }

        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
        String strWithInt;
        int resInt;

        if (msg != null && !msg.isEmpty()) {
            System.out.println(msg);
        }

        while (true) {
            try {
                strWithInt = bR.readLine();
                resInt = Integer.parseInt(strWithInt);
                if ((min != null && resInt < min) || (max != null && resInt > max)) {
                    System.out.println("Введено некорректное число, повторите ввод");
                } else {
                    return resInt;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Не удалось распознать число, повторите ввод");
            }
        }
    }

    /**
     * Реализует ввод строки с выводом перед этим, если требуется, сообщения msg,
     *      а также проверку введенной строки на валидность
     *      (строка должна быть непустая)
     *
     * @param   msg     сообщение, которое выводится пользователю перед считыванием строки
     * @return          введённая пользователем непустая строка, удовлетворяющая критерию allowRepeatSymbols
     * @throws  java.io.IOException
     */
    public static String inputString(String msg)
            throws IOException {
        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
        String resString;

        if (msg != null && !msg.isEmpty()) {
            System.out.println(msg);
        }

        resString = bR.readLine();
        while (resString.isEmpty()) {
            System.out.println("Введена пустая строка, повторите ввод");
            resString = bR.readLine();
        }

        return resString;
    }

    public static void main(String[] args) throws IOException {
        int capacity = inputInteger("Введите вместимость LRU-кеша", 1, null);
        LRUCache<Integer, String> lruCache = new LRUCache<>(capacity);
        System.out.println("Создан LRU-кеш вместимостью " + lruCache.getLimit());

        while (true) {
            System.out.println(
                    "\nВыберите действие:\n" +
                    "1 - Вывести количество элементов в кеше\n" +
                    "2 - Найти элемент в кеше по ключу\n" +
                    "3 - Добавить элемент в кеш\n" +
                    "4 - Вывести содержимое кеша\n" +
                    "0 - Закончить работу с программой"
            );

            switch (inputInteger(null, 0, 4)) {
                case 1 -> System.out.println("Количество элементов в кеше: " + lruCache.getSize());
                case 2 -> {
                    if (lruCache.getSize() == 0) {
                        System.out.println("Кеш пуст");
                    } else {
                        String s = lruCache.get(inputInteger("Введите ключ", null, null));
                        if (s == null) {
                            System.out.println("Элемента с введенным ключом нет в кеше");
                        } else {
                            System.out.println("Значение элемента с введенным ключом: " + s);
                        }
                    }
                }
                case 3 -> {
                    int key = inputInteger("Введите ключ для элемента", null, null);
                    String value = inputString("Введите элемент");

                    lruCache.set(key, value);
                    System.out.println("Элемент был добавлен в кеш");
                }
                case 4 -> {
                    if (lruCache.getSize() == 0) {
                        System.out.println("Кеш пуст");
                    } else {
                        for (Node<Integer, String> node : lruCache) {
                            System.out.println("Ключ: \"" + node.key + "\", значение: \"" + node.value + "\"");
                        }
                    }
                }
                case 0 -> System.exit(0);
            }
        }
    }
}
