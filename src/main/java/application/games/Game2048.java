package application.games;

import java.util.*;
import java.util.List;

public class Game2048 extends javafx.scene.canvas.Canvas {

    private Game2048Cell[] game2048Cells;
    boolean win = false;
    boolean lose = false;
    int score = 0;

    public Game2048Cell[] getCells() {
        return game2048Cells;
    }

    public Game2048() {
        super(330, 390);
        setFocused(true);
        resetGame();
    }

    public Game2048(double width, double height) {
        super(width, height);
        setFocused(true);
        resetGame();
    }


    void resetGame() {
        score = 0;
        win = false;
        lose = false;
        game2048Cells = new Game2048Cell[4 * 4];
        for (int cell = 0; cell < game2048Cells.length; cell++) {
            game2048Cells[cell] = new Game2048Cell();
        }
        addCell();
        addCell();
    }

    private void addCell() {
        List<Game2048Cell> list = availableSpace();
        if(!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Game2048Cell emptyGame2048Cell = list.get(index);
            emptyGame2048Cell.number = Math.random() < 0.9 ? 2 : 4;
        }

    }

    private List<Game2048Cell> availableSpace() {
        List<Game2048Cell> list = new ArrayList<>(16);
        for(Game2048Cell c : game2048Cells)
            if(c.isEmpty())
                list.add(c);
        return list;
    }

    private boolean isFull() {
        return availableSpace().size() == 0;
    }

    private Game2048Cell cellAt(int x, int y) {
        return game2048Cells[x + y * 4];
    }

    protected boolean canMove() {
        if(!isFull()) return true;
        for(int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Game2048Cell game2048Cell = cellAt(x, y);
                if ((x < 3 && game2048Cell.number == cellAt(x + 1, y).number) ||
                        (y < 3) && game2048Cell.number == cellAt(x, y + 1).number) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compare(Game2048Cell[] line1, Game2048Cell[] line2) {
        if(line1 == line2) {
            return true;
        }
        if (line1.length != line2.length) {
            return false;
        }

        for(int i = 0; i < line1.length; i++) {
            if(line1[i].number != line2[i].number) {
                return false;
            }
        }
        return true;
    }

    private Game2048Cell[] rotate(int angle) {
        Game2048Cell[] tiles = new Game2048Cell[4 * 4];
        int offsetX = 3;
        int offsetY = 3;
        if(angle == 90) {
            offsetY = 0;
        } else if(angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                int newX = (x*cos) - (y*sin) + offsetX;
                int newY = (x*sin) + (y*cos) + offsetY;
                tiles[(newX) + (newY) * 4] = cellAt(x, y);
            }
        }
        return tiles;
    }

    private Game2048Cell[] moveLine(Game2048Cell[] oldLine) {
        LinkedList<Game2048Cell> list = new LinkedList<Game2048Cell>();
        for(int i = 0; i < 4; i++) {
            if(!oldLine[i].isEmpty()){
                list.addLast(oldLine[i]);
            }
        }

        if(list.size() == 0) {
            return oldLine;
        } else {
            Game2048Cell[] newLine = new Game2048Cell[4];
            while (list.size() != 4) {
                list.add(new Game2048Cell());
            }
            for(int j = 0; j < 4; j++) {
                newLine[j] = list.removeFirst();
            }
            return newLine;
        }
    }

    private Game2048Cell[] mergeLine(Game2048Cell[] oldLine) {
        LinkedList<Game2048Cell> list = new LinkedList<Game2048Cell>();
        for(int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].number;
            if (i < 3 && oldLine[i].number == oldLine[i+1].number) {
                num *= 2;
                score += num;
                if ( num == 2048) {
                    win = true;
                }
                i++;
            }
            list.add(new Game2048Cell(num));
        }

        if(list.size() == 0) {
            return oldLine;
        } else {
            while (list.size() != 4) {
                list.add(new Game2048Cell());
            }
            return list.toArray(new Game2048Cell[4]);
        }
    }

    private Game2048Cell[] getLine(int index) {
        Game2048Cell[] result = new Game2048Cell[4];
        for(int i = 0; i < 4; i++) {
            result[i] = cellAt(i, index);
        }
        return result;
    }

    private void setLine(int index, Game2048Cell[] re) {
        System.arraycopy(re, 0, game2048Cells, index * 4, 4);
    }

    public void left() {
        boolean needAddCell = false;
        for(int i = 0; i < 4; i++) {
            Game2048Cell[] line = getLine(i);
            Game2048Cell[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            if( !needAddCell && !compare(line, merged)) {
                needAddCell = true;
            }
        }
        if(needAddCell) {
            addCell();
        }
    }

    public void right() {
        game2048Cells = rotate(180);
        left();
        game2048Cells = rotate(180);
    }

    public void up() {
        game2048Cells = rotate(270);
        left();
        game2048Cells = rotate(90);
    }

    public void down() {
        game2048Cells = rotate(90);
        left();
        game2048Cells = rotate(270);
    }
}
