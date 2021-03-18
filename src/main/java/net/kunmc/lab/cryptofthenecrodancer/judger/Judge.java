package net.kunmc.lab.cryptofthenecrodancer.judger;

import org.bukkit.ChatColor;

/**
 * 判定結果
 */
public enum Judge
{
    PERFECT(20,
            ChatColor.GOLD + "✧✦✧パーフェクト✧✦✧",
            false, true),
    GREAT(80, ChatColor.GREEN + "良", true, false),
    GOOD(120, ChatColor.BLUE + "可", true, false),
    MISS(-1,ChatColor.GRAY + "ミス！", false, true),
    BEAT_SKIP(-1, "ビートスキップ", true, false);

    /*
     * ジャッジの当たり判定(ms)
     * 前後に指定するため、判定値が二倍になることに注意
     */
    private final int judgeTime;

    /**
     * 表示
     */
    private final String display;

    /**
     * アクションバーに表示するかどうか
     */
    private final boolean showActionbar;

    /**
     * タイトルに表示するかどうか。
     */
    private final boolean showTitlebar;

    Judge(int judgeTime, ChatColor color, boolean actionBar, boolean title)
    {
        this(judgeTime, color.toString(), actionBar, title);
    }

    /**
     * コンストラクタ。
     * @param judgeTime 判定する時間
     * @param display アクションバーやタイトルに表示する文字列
     * @param actionBar アクションバーに表示するかどうか
     * @param title タイトルに表示するかどうか
     */
    Judge(int judgeTime, String display, boolean actionBar, boolean title)
    {
        this.judgeTime = judgeTime;
        this.display = display;
        this.showActionbar = actionBar;
        this.showTitlebar = title;
    }

    public int getJudgeTime()
    {
        return judgeTime;
    }

    public String getDisplay()
    {
        return display;
    }

    public boolean isShowActionbar()
    {
        return showActionbar;
    }

    public boolean isShowTitlebar()
    {
        return showTitlebar;
    }
}
