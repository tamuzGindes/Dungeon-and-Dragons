package BusinessLayer.GameManeger;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.GameTiles.Tile;
import BusinessLayer.Objects.Position;
import BusinessLayer.Player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class GameBoard {
    private TileFactory tileFactory = new TileFactory();
    public Tile[][] Board;

    private Player player;
    private List<Enemy> enemies = new LinkedList<>();

    public GameBoard(String path,int p) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println("Error while reading level board");
        }
        int rows = lines.size();
        int column = lines.get(0).length();
        char[][] charBoard = new char[rows][column];
        for (int i = 0; i < rows ; i = i + 1){
            for (int j = 0; j < column; j = j + 1) {
                charBoard[i][j] = lines.get(i).charAt(j);
            }
        }

        Tile[][] tileMatrix = new Tile[rows][column];
        for (int x = 0 ; x < charBoard.length ; x = x + 1){
            for(int y = 0 ; y < charBoard[x].length ; y = y + 1){
                if (charBoard[x][y] == '.' | charBoard[x][y] == '#'){
                    tileMatrix[x][y] = tileFactory.initTile(new Position(x,y),charBoard[x][y]);
                }
                else if(charBoard[x][y] == '@'){
                    player = tileFactory.producePlayer(new Position(x,y),p);
                    tileMatrix[x][y] = tileFactory.producePlayer(new Position(x,y),p);

                }
                else{
                    tileMatrix[x][y] = tileFactory.produceEnemy(charBoard[x][y],new Position(x,y));
                    enemies.add(tileFactory.produceEnemy(charBoard[x][y],new Position(x,y)));
                }
            }

        }
        Board = tileMatrix;

    }


    public Player getPlayer(){
        return player;
    }

    public List<Enemy> getEnemies(){
        return enemies;
    }


    public String toString() {
        String output = "";
        for(int i = 0 ; i < Board.length ; i = i + 1){
            for(int j = 0 ; j < Board[i].length ; j = j + 1){
                output += Board[i][j].getTile();
            }
            output += '\n';
        }
        return output;
    }

    public Tile[][] getBoard(){
        return Board;
    }

}