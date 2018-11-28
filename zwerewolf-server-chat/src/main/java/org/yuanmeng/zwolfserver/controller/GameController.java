package org.yuanmeng.zwolfserver.controller;

import org.springframework.stereotype.Controller;
import org.yuanmeng.zwolfserver.annotation.RequestMapping;

/**
 * 游戏流程控制 controller
 *
 * @author wangtonghe
 * @since 2018/10/5 20:58
 */
@Controller
public class GameController {

    @RequestMapping(value = "/game/ready")
    public void readyGame() {

    }

    @RequestMapping(value = "/game/start")
    public void startGame() {

    }

}
