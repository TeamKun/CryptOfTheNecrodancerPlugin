package net.kunmc.lab.cryptofthenecrodancer.enums;

public enum Judge
{
    PERFECT(20),
    GREAT(80),
    GOOD(120),
    MISS(-1),
    SKIP(-1);

    /*
     * ジャッジの当たり判定(ms)
     * 前後に指定するため、判定値が二倍になることに注意
     */
    private final int judgeTime;

    Judge(int judgeTime)
    {
        this.judgeTime = judgeTime;
    }

    public int getJudgeTime()
    {
        return judgeTime;
    }
}
