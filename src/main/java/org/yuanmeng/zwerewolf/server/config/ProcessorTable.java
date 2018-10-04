package org.yuanmeng.zwerewolf.server.config;

import java.util.HashMap;
import java.util.Map;

import org.yuanmeng.zwerewolf.server.constant.ProtocolConstant;
import org.yuanmeng.zwerewolf.server.controller.BaseController;
import org.yuanmeng.zwerewolf.server.controller.impl.BusinessController;
import org.yuanmeng.zwerewolf.server.controller.impl.GameController;
import org.yuanmeng.zwerewolf.server.controller.impl.MessageController;
import org.yuanmeng.zwerewolf.server.controller.impl.UserController;

public class ProcessorTable {
    private static Map<Short,BaseController> table = new HashMap<>();
    static {
       table.put(ProtocolConstant.SID_USER,new UserController());
       table.put(ProtocolConstant.SID_MSG,new MessageController());
       table.put(ProtocolConstant.SID_BNS,new BusinessController());
       table.put(ProtocolConstant.SID_GAME,new GameController());
    }
    public static BaseController get(short code){
        return table.get(code);
    }
    public static Map<Short,BaseController> getAll(){
    	return table;
    }
}
