package com.xl.deploy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with 徐立.
 *
 * @author 徐立
 * @date 2019-04-15
 * @time 22:44
 * To change this template use File | Settings | File Templates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeploymentEntity {
    /**
     * 作者
     */
    private String author;
    /**
     * true任务，false bug
     */
    private boolean flag;
    /**
     * code.txt
     */
    private String codeString;
    /**
     * 发布文档中的内容
     */
    private String docString;
    /**
     * 任务名称
     */
    private String taskName;
}
