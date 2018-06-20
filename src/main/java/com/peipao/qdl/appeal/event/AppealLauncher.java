package com.peipao.qdl.appeal.event;

import com.peipao.qdl.appeal.service.AppealReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.bus.EventBus;
import static reactor.bus.selector.Selectors.$;
/**
 * 方法名称：AppealLauncher
 * 功能描述：关联事件类型与事件处理程序
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2017/11/22 13:38
 * 修订记录：
 */

@Order(1)
@Component
public class AppealLauncher implements CommandLineRunner {
    @Autowired
    private EventBus eventBus;

    @Autowired
    private AppealReceiverService appealReceiverService;

    @Override
    public void run(String... strings) throws Exception {
        eventBus.on($("autoAppealEvent"), appealReceiverService);
    }

}
