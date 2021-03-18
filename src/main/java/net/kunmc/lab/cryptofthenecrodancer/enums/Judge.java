package net.kunmc.lab.cryptofthenecrodancer.enums;

public enum Judge
{
    PERFECT(25),
    GREAT(100),
    GOOD(250),
    MISS(-1),
    SKIP(-1);

    private final int judgeTime;

    Judge(int judgeTime) {
        this.judgeTime = judgeTime;
    }

    public int getJudgeTime() {
        return judgeTime;
    }
}
