package com.xl.deloy.service;

import com.xl.deploy.entity.DeploymentEntity;
import java.io.IOException;

/**
 * Created with 徐立.
 *
 * @author 徐立
 * @date 2019-04-13
 * @time 15:40
 * To change this template use File | Settings | File Templates.
 */
public interface DeploymentPackageService {
    /**
     * 创建部署包
     *
     * @param entity
     */
    void createFile(DeploymentEntity entity) throws IOException;
}
