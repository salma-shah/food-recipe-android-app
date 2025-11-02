package com.example.foodbunny.objects;

public class Direction {
    private int position;
    private String instruction;
    public Direction(int position, String instruction) {
        this.position = position;
        this.instruction = instruction;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
