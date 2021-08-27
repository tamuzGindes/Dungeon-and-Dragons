package Presentation;

import BusinessLayer.GameTiles.Empty;
import BusinessLayer.GameTiles.Tile;
import BusinessLayer.Player.Player;
import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;
import BusinessLayer.GameManeger.GameBoard;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameController {
	private List<Enemy> enemies;
	private Player player;
	private GameBoard board;
	private Scanner reader = new Scanner(System.in);
	private int level_counter = 0;
	private ArrayList<GameBoard> levelsBoards;
	
	MessageCallback messageCallback = (msg) -> {
		System.out.println(msg);
	};
	DeathCallback playerDeathCallback = () -> {
		onPlayerDeath();
	};

	public GameController(String path) {
		messageCallback.send(
				"select player: \n 1.Jon Snow \n 2.The Hound \n 3.Melisandre \n 4.Thoros Of Myr \n 5.Arya Stark  \n 6.Bronn \n 7.Ygritte");

		int Player = reader.nextInt();
		levelsBoards = new ArrayList<>();
		try {
			List<String> levels = Files.list(Paths.get(path)).sorted().map(Path::toString).collect(Collectors.toList());
			for (String level : levels) {
				levelsBoards.add(new GameBoard(level, Player - 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		initializeLevel();
	}

	public void initializeLevel() {
		if (level_counter < levelsBoards.size()) {
			// initialize board
			this.board = levelsBoards.get(level_counter);

			// initialize player
			if (player == null) {
				player = this.board.getPlayer();
				player = player.initialize(player.getPosition(), messageCallback, playerDeathCallback);
				Printer.printMessage("you chose " + player.getName());
			} else {
				// this is not the first level so player is already initialized
				// we just need to fix its position
				player.setPosition(this.board.getPlayer().getPosition());
			}

			// initialize enemies
			enemies = board.getEnemies();
			for (Enemy enemy : enemies) {
				DeathCallback enemyDeathCallBack = () -> {
					onEnemyDeath(enemy);
				};
				enemy.initialize(enemy.getPosition(), messageCallback, enemyDeathCallBack);
			}
		}
	}

	public void PlayLevels() {
		while (level_counter != levelsBoards.size()) {
			if (playLevel()) {
				if (level_counter <= levelsBoards.size())
					level_counter++;

				initializeLevel();
			} else {
				Printer.printMessage("you LOST");
				break;
			}
		}
		if (level_counter == levelsBoards.size()) {
			Printer.printMessage("you won");
		}
	}

	public void onPlayerDeath() {
		player.setTile('X');
		messageCallback.send("you lost!!");
	}

	public void onEnemyDeath(Enemy enemy) {
		Empty empty = new Empty(enemy.getPosition().getX(), enemy.getPosition().getY());
		board.getBoard()[enemy.getPosition().getX()][enemy.getPosition().getY()] = empty;
		this.enemies.remove(enemy);

	}

	public void checkDeads() {
		List<Enemy> toDelete = new ArrayList<>();
		for (Enemy enemy : enemies) {
			if (enemy.getHealthAmount() <= 0) {
				board.getBoard()[enemy.getPosition().getX()][enemy.getPosition().getY()] = new Empty(
						enemy.getPosition().getX(), enemy.getPosition().getY());
				toDelete.add(enemy);
			}
		}
		for (Enemy enemy : toDelete)
			enemies.remove(enemy);
		if (player.getHealthAmount() == 0) {
			player.setTile('X');
		}
	}

	public void Turn(char move) {
		PlayerTurn(move);
		player.gameTick();
		checkDeads();
		if (!enemies.isEmpty()) {
			EnemiesTurn();
		}
		checkDeads();
		if (enemies.isEmpty()) {
			// won = true;
		}
	}

	public void PlayerTurn(char move) {
		switch (move) {
		case 'w':
			board.Board[player.getPosition().getX() - 1][player.getPosition().getY()].accept(player);
			movePlayer(player, board.Board[player.getPosition().getX()][player.getPosition().getY()]);
			break;
		case 's':
			board.Board[player.getPosition().getX() + 1][player.getPosition().getY()].accept(player);
			movePlayer(player, board.Board[player.getPosition().getX()][player.getPosition().getY()]);
			break;
		case 'a':
			board.Board[player.getPosition().getX()][player.getPosition().getY() - 1].accept(player);
			movePlayer(player, board.Board[player.getPosition().getX()][player.getPosition().getY()]);
			break;
		case 'd':
			board.Board[player.getPosition().getX()][player.getPosition().getY() + 1].accept(player);
			movePlayer(player, board.Board[player.getPosition().getX()][player.getPosition().getY()]);
			break;
		case 'q':
			break;
		case 'e':
			player.castAbility(enemies, null);
			break;
		}
	}

	public void movePlayer(Player player, Tile tile) {
		Position playerPosition = player.getPosition();
		Position tilePosition = tile.getPosition();
		board.Board[playerPosition.getX()][playerPosition.getY()] = player;
		board.Board[tilePosition.getX()][tilePosition.getY()] = tile;
	}

	public void boardRefresh(Enemy enemy, Tile tile) {
		Position enemyPosition = enemy.getPosition();
		Position tilePosition = tile.getPosition();
		board.Board[enemyPosition.getX()][enemyPosition.getY()] = enemy;
		board.Board[tilePosition.getX()][tilePosition.getY()] = tile;
	}

	public void EnemiesTurn() {
		try {
			for (Enemy enemy : enemies) {
				messageCallback.send(enemy.describe());
				Position EnemyNewPosition = enemy.enemyTurn(player);
				if (!EnemyNewPosition.Equals(new Position(-1, -1))) {
					board.getBoard()[EnemyNewPosition.getX()][EnemyNewPosition.getY()].accept(enemy);
					boardRefresh(enemy, board.getBoard()[EnemyNewPosition.getX()][EnemyNewPosition.getY()]);
				}
				enemy.gameTick();
			}
		} catch (Exception e) {

		}
	}

	public boolean isDead() {
		return player.getTile() == 'X';
	}

	public boolean playLevel() {
		messageCallback.send("welcome to level number " + level_counter);
		messageCallback.send(
				"press w - to move up \npress s - to move down \npress a - to move left\npress d - to move right\npress e - to cast special ability\npress q - to do nothing");
		while (!levelOver()) {
			messageCallback.send(board.toString());
			messageCallback.send(player.describe());
			char move = reader.next().charAt(0);
			Turn(move);

		}
		return isWon();
	}

	public boolean levelOver() {
		return (enemies.isEmpty() | player.getHealthAmount() <= 0);
	}

	public boolean isWon() {
		return player.getHealthAmount() > 0;
	}

}
