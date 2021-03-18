package net.kunmc.lab.cryptofthenecrodancer.judger;

/**
 * Judgerが返すもの
 */
public class JudgeResult
{
    /**
     * イベントをキャンセルしなければならない(MISS)
     */
    public boolean isCancel;

    /**
     * PERFECTとか。
     */
    public Judge judgement;

    public JudgeResult(boolean isCancel, Judge judge)
    {
        this.isCancel = isCancel;
        this.judgement = judge;
    }
}
