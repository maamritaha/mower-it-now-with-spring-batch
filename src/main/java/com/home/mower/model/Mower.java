package com.home.mower.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class Mower {
    private int positionX;
    private int positionY;
    private char orientation;
    private String command;

    public void executeCommand(int width, int height) {
        for (char move : command.toCharArray()) {
            if (move == 'G' || move == 'D') {
                rotate(move);
            } else if (move == 'A') {
                moveForward(width, height);
            }
        }

        // log the final position
        log.info("Final Position: (" + positionX + ", " + positionY + ", " + orientation + ")");
    }

    private void moveForward(int width, int height) {
        int nextX = positionX;
        int nextY = positionY;

        switch (orientation) {
            case 'N':
                nextY++;
                break;
            case 'E':
                nextX++;
                break;
            case 'S':
                nextY--;
                break;
            case 'W':
                nextX--;
                break;
        }

        // Check if the new position is within the lawn
        if (isValidPosition(nextX, nextY, width, height)) {
            positionX = nextX;
            positionY = nextY;
        }
    }

    private void rotate(char direction) {
        if (direction == 'G') {
            rotateLeft();
        } else if (direction == 'D') {
            rotateRight();
        }
    }

    private void rotateRight() {
        switch (orientation) {
            case 'N':
                orientation = 'E';
                break;
            case 'E':
                orientation = 'S';
                break;
            case 'S':
                orientation = 'W';
                break;
            case 'W':
                orientation = 'N';
                break;
        }
    }

    private void rotateLeft() {
        switch (orientation) {
            case 'N':
                orientation = 'W';
                break;
            case 'E':
                orientation = 'N';
                break;
            case 'S':
                orientation = 'E';
                break;
            case 'W':
                orientation = 'S';
                break;
        }
    }

    private boolean isValidPosition(int x, int y, int width, int height) {
        return x >= 0 && x <= width && y >= 0 && y <= height;
    }
}
