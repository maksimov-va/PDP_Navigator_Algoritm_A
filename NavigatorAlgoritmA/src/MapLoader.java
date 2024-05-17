/*
  Карта города, загружаемая из txt-файла в навигатор для дальнейшего анализа маршрута
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MapLoader {
    /**
     * Загрузка карты из txt-файла в двумерный масссив символов,
     * производя проверку на корректность размера карты (MxN)
     * @param filePath  путь к txt-файлу, содержащему карту
     * @return          null, если загруженная карта некорректна
     *                  map, если карта пригодна для использования навигатором
     */
    char[][] loadMap(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        int rows = 0, columns = 0, mapSizeChecker;
        String line;
        StringBuilder sb = new StringBuilder();
        List<String> lineList = new ArrayList<>();

        while ((line = br.readLine()) != null && line.length() != 0) {
            rows++;
            mapSizeChecker=line.length();
            if (columns!=0 && mapSizeChecker!=columns)
            {
                System.out.println("Map is invalid. The map size is not equal MxN");
                return null;
            }else {
                columns = mapSizeChecker;
                lineList.add(line);
                sb.append(line);
            }
        }
        br.close();

        if(isMapValid(sb.toString())) {
            return getMap(rows, columns, lineList);
        } else return null;
    }

    /**
     * Перевод листа строк, полученных из файла, в двумерный массив символов
     * @param rows      количество строк заданной карты
     * @param columns   количество столбцов заданной карты
     * @param lineList  лист строк заданной карты, используемый
     *                  для преобразования в массив символов
     * @return          заданная карта, преобразованная
     *                  в двумерный массив символов
     */
    private char[][] getMap(int rows, int columns, List<String> lineList) {
        char[][] map = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            int idx = 0;
            for (int j = 0; j < lineList.get(i).length(); j++) {
                map[i][j] = lineList.get(i).charAt(idx++);
            }
        }
        return map;
    }

    /**
     * Анализ карты на предмет следующих ошибок:
     *  - использование символов, кроме '#', '.', 'X', '@';
     *  - использование боелее одного символа '@' или 'X',
     *    означающих начало маршрута и конец маршрута соответственно
     * @param text  анализируемый c помощью регулярных выражений набор символов (текст),
     *              задающих карту для навигатора
     * @return      true, если карта может использоваться навигатором
     */
    private static boolean isMapValid(String text) {
        final String ENTRANCE_REGEX="^[#.X]*@{1}[#.X]*$";
        final String EXIT_REGEX="^[#.@]*X{1}[#.@]*$";
        if (!(text.matches(ENTRANCE_REGEX) && text.matches(EXIT_REGEX))){
            System.out.println("Map is invalid. Map should contain the following characters:" +
                    " '@' (in single copy), 'X'(in single copy), '#', '.'");
            return false;
        } else return true;
    }

    /**
     * Вывод считанной в двумерный массив карты в консоль
     * для проверки человеком корректности считанных данных
     * @param map   карта, задаваемая для навигатора с помощью набора символов
     */
    void printMap(char[][] map) {
        for (char[] r: map){
            System.out.println(r);
        }
    }
}