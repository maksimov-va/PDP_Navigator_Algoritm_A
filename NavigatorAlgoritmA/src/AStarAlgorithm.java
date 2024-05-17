/*
  Алгоритм поиска кратчайшего пути от начальной вершины до выбранной конечной
  с оценкой стоимости перемещения по вертикали и горизонтали (без движения по диагонали,
  т.к. по условия заднное направление движения невозможно для жителей города)
 */

import java.util.PriorityQueue;

public class AStarAlgorithm implements Navigator {

    // стоимость перемещения по вертикали и горизонтали
    private static final int VERTICAL_HORIZONTAL_COST = 1;

    // ячейка, содержащая информацию о стоимости ее посещения,
    // x и y координатах и родительской ячейке
    static class Cell{
        int heuristicCost = 0;
        int finalCost = 0;
        int i, j;
        Cell parent;

        Cell(int i, int j){
            this.i = i;
            this.j = j;
        }
    }

    // набор ячеек, в которых будет производиться оценка стоимости построенного маршрута
    private static Cell [][] grid;

    // набор узлов, которые будут оценены
    private static PriorityQueue<Cell> openNodes;

    // набор узлов, который уже оценен
    private static boolean[][] closedNodes;

    // x и y координаты начала и конца маршрута
    private static int startI, startJ;
    private static int endI, endJ;

    /**
     * Присваивание координат ячейки начала маршрута
     * @param i x-координата ячейки начала маршрута
     * @param j y-координата ячейки начала маршрута
     */
    private static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }

    /**
     * Присваивание координат ячейки конца маршрута
     * @param i x-координата ячейки конца маршрута
     * @param j y-координата ячейки конца маршрута
     */
    private static void setEndCell(int i, int j){
        endI = i;
        endJ = j;
    }

    /**
     * Обнуление значения ячеек, представляющих собой препятствия на карте(стены)
     * @param i x-координата ячейки препятствия на карте (стены)
     * @param j y-координата ячейки препятствия на карте (стены)
     */
    private static void setBlockedCells(int i, int j){
        grid[i][j] = null;
    }

    /**
     * Присваивание координат ячеек, представляющих собой препятствия на карте (стены)
     * @param map           заданная  карта, преобразованная в двумерный массив символов
     * @param blocked       координаты ячеек, представляющих собой препятствия на карте (стены)
     * @param blocksCount   количество препятствий на карте
     * @return              массив координат ячеек, представляющих собой
     *                      препятствия на карте (стены)
     */
    private int[][] getBlockedCells(char[][] map, int[][] blocked, int blocksCount) {
        if (blocksCount!=0) {
            blocked = new int[blocksCount][2];
            int blocks=0;
            while (blocks<blocksCount) {
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j] == '#') {
                            blocked[blocks][0] = i;
                            blocked[blocks][1] = j;
                            blocks++;
                        }
                    }
                }
            }
        }
        return blocked;
    }

    /**
     * Выполнение проверки и обновления данных о стоимости маршрута
     * @param currentNode   текущая ячейка, для которой осуществляется проверка
     * @param targetNode    ячейка, в которую планируется выполнить перемещение
     * @param cost          стоимость перемещения
     */
    private static void checkAndUpdateCost(Cell currentNode, Cell targetNode, int cost){
        if(targetNode == null || closedNodes[targetNode.i][targetNode.j])return;

        int targetNodeFinalCost = targetNode.heuristicCost+cost;
        boolean inOpen = openNodes.contains(targetNode);
        if(!inOpen || targetNodeFinalCost<targetNode.finalCost){
            targetNode.finalCost = targetNodeFinalCost;
            targetNode.parent = currentNode;
            if(!inOpen)openNodes.add(targetNode);
        }
    }

    /**
     * Запуск работы алгоритма для вычисления кратчайшего маршрута
     */
    private static void runAStarAlgorithm(){
        openNodes.add(grid[startI][startJ]);

        Cell currentNode;
        while(true){
            currentNode = openNodes.poll();
            if(currentNode==null) break;
            closedNodes[currentNode.i][currentNode.j]=true;
            if(currentNode.equals(grid[endI][endJ])) return;

            Cell targetNode;
            if(currentNode.i-1>=0){
                targetNode = grid[currentNode.i-1][currentNode.j];
                checkAndUpdateCost(currentNode, targetNode, currentNode.finalCost+VERTICAL_HORIZONTAL_COST);
            }
            if(currentNode.j-1>=0){
                targetNode = grid[currentNode.i][currentNode.j-1];
                checkAndUpdateCost(currentNode, targetNode, currentNode.finalCost+VERTICAL_HORIZONTAL_COST);
            }
            if(currentNode.j+1<grid[0].length){
                targetNode = grid[currentNode.i][currentNode.j+1];
                checkAndUpdateCost(currentNode, targetNode, currentNode.finalCost+VERTICAL_HORIZONTAL_COST);
            }
            if(currentNode.i+1<grid.length){
                targetNode = grid[currentNode.i+1][currentNode.j];
                checkAndUpdateCost(currentNode, targetNode, currentNode.finalCost+VERTICAL_HORIZONTAL_COST);
            }
        }
    }

    /**
     * Инициализация ячеек карты (назначение коррдинат ячеек начала и конца маршрута,
     * назначение координат ячеек с препятствием, назначение и вычисление
     * стоимости посещения свободных ячеек)
     * @param map   карта города
     */
    private void initializeCells(char[][] map) {
        int x=map.length,  y=map[0].length, blocksCount=0;
        int[][] blocked=null;

        grid = new Cell[x][y];
        closedNodes = new boolean[x][y];
        openNodes = new PriorityQueue<>((Object o1, Object o2) -> {
            Cell c1 = (Cell)o1;
            Cell c2 = (Cell)o2;
            return (Integer.compare(c1.finalCost, c2.finalCost));
        });

        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                if (map[i][j]=='@') setStartCell(i,j);
                if (map[i][j]=='X') setEndCell(i,j);
                if (map[i][j]=='#') blocksCount++;
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
            }
        }
        blocked = getBlockedCells(map, blocked, blocksCount);
        grid[startI][startJ].finalCost = 0;
        if (blocked!=null) for (int i = 0; i < blocked.length; ++i) setBlockedCells(blocked[i][0], blocked[i][1]);
    }

    /**
     * Рисование маршрута символом ‘+’.
     * @param map   карта города
     * @return      карта города с построенным маршрутом
     */
    private char[][] drawRoute(char[][] map) {
        if(closedNodes[endI][endJ]){
            Cell current = grid[endI][endJ];
            while(current.parent!=null){
                current = current.parent;
                if (map[current.i][current.j]=='.')  map[current.i][current.j]='+';
            }
            System.out.println();
        }
        else {
            System.out.println("No possible path");
            return new char[][]{{}};
        }
        return map;
    }

    /**
     * Поиск кратчайшего маршрута на карте города между двумя точками
     * @param map   карта города
     * @return      карта города с построенным маршрутом
     */
    public char[][] searchRoute(char[][] map){
        initializeCells(map);
        runAStarAlgorithm();
        return drawRoute(map);
    }
}