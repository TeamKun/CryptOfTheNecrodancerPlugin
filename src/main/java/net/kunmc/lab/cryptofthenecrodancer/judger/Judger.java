package net.kunmc.lab.cryptofthenecrodancer.judger;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import org.bukkit.entity.Player;

/**
 * 実際に判定したり、ボスバー・タイトル・アクションバーを管理。
 */
public class Judger
{
    public static JudgeResult onPlayerAction(ActionType type, Player player)
    {

        if (CryptOfTheNecroDancer.game == null || !CryptOfTheNecroDancer.game.isRunning())
            return new JudgeResult(false, null);

        Judge judge = CryptOfTheNecroDancer.game.judge(player);

        showTitleAndActionbar(player, judge);

        return new JudgeResult(judge == Judge.MISS, judge);
    }

    public static void showTitleAndActionbar(Player targetPlayer, Judge judgeResult)
    {
        String textTitle = judgeResult.getDisplay();

        if (judgeResult.isShowActionbar())
        {
            targetPlayer.sendActionBar(textTitle);
            CryptOfTheNecroDancer.titleShower.setMainTitle(targetPlayer, "");
        }

        if (judgeResult.isShowTitlebar())
        {
            targetPlayer.sendActionBar("    ");
            CryptOfTheNecroDancer.titleShower.setMainTitle(targetPlayer, textTitle);
        }
    }
}
