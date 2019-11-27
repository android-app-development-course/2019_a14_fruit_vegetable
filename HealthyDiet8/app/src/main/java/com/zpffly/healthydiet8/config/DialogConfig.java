package com.zpffly.healthydiet8.config;

import com.kongzue.dialog.util.DialogSettings;

/**
 * 对话框配置类
 */
public class DialogConfig {

    static {
        // 对话框风格
        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        // 自动弹出对话框
        DialogSettings.autoShowInputKeyboard = true;
        DialogSettings.init();
    }
}
