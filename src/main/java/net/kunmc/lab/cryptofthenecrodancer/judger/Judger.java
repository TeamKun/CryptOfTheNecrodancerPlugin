package net.kunmc.lab.cryptofthenecrodancer.judger;

import net.kunmc.lab.cryptofthenecrodancer.CryptOfTheNecroDancer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

/**
 * 実際に判定したり、ボスバー・タイトル・アクションバーを管理。
 */
public class Judger
{
    public static JudgeResult onPlayerAction(ActionType type, Player player)
    {
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
