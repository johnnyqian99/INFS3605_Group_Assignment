package com.example.infs3605_group_assignment;

import java.util.ArrayList;
import java.util.List;

public class Rewards {
    private int cost;
    private int img;

    public Rewards(int cost, int img){
        this.cost = cost;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public int getCost() {
        return cost;
    }

    public static List<Rewards> getCategories() {
        List<Rewards> rewards = new ArrayList<Rewards>();

        rewards.add(new Rewards(100, R.drawable.vids));
        rewards.add(new Rewards(100, R.drawable.donate));
        rewards.add(new Rewards(100, R.drawable.donate));

        return rewards;
    }

}
