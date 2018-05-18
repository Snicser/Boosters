package io.github.jordieh.boosters.framework.booster;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoard {

    private final Player player;
    private final Scoreboard scoreboard;

    public ScoreBoard(Player player, String name) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("score", "dummy");
        objective.setDisplayName(name);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateLine(int line, String s) {
        scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(s).setScore(line);
    }

}
