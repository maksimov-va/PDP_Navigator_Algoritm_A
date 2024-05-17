/*
   Напишите программу для навигатора, поиска кратчайшего пути в городе Мачу-Пикчу для двух заданных точек.
   Программное обеспечение навигатора написано на языке Java и в библиотеке реализован интерфейс для поиска
   алгоритма.

   Необходимо написать реализацию данного интерфейса. На вход программе подается карта города, заданная как
   символьный двумерный массив размером MxN (1<=M,N<=10000).

   Обозначения на карте
   # стена
   . дорога
   @ начало маршрута
   X конец маршрута

   Жители города могут перемещаться на соседние клетки по вертикали и по горизонтали. По диагонали перемещаться
   жители не могут. Если построить маршрут невозможно, то функция searchRoute должна возвращать null.
   Результатом работы программы должен быть символьный массив с картой города и построенным маршрутом.
   Маршрут должен быть проложен символом ‘+’.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the path to the map file. It should be txt-file."+
                            "\nYou can skip this step by press Enter to use default path.");
        String filePath = reader.readLine();
        if (filePath.isEmpty()) filePath="src/resources/map.txt";

        if (new File(filePath).exists()&& filePath.endsWith(".txt")) {
            MapLoader mapLoader = new MapLoader();
            char[][] map = mapLoader.loadMap(filePath);
            if (map != null) {
                Navigator navigator = new AStarAlgorithm();
                mapLoader.printMap(map);
                mapLoader.printMap(navigator.searchRoute(map));
            }
        } else System.out.println("File doesn't exist or filePath is incorrect");
    }
}