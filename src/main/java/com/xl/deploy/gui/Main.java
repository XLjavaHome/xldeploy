package com.xl.deploy.gui;

import com.xl.util.GUIUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created with 徐立.
 *
 * @author 徐立
 * @date 2019-09-11
 * @time 22:43
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        DeployForm dialog = applicationContext.getBean(DeployForm.class);
        dialog.pack();
        GUIUtil.makeCenter(dialog);
        dialog.setVisible(true);
    }
}
