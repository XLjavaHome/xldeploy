package com.xl.deploy.gui;

import com.xl.deploy.service.DeploymentPackageService;
import com.xl.deploy.service.impl.DeploymentPackageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with 徐立
 *
 * @author 徐立
 * @version 1.0 2019-10-25 19:05
 * To change this template use File | Settings | File Templates.
 * @date 2019-10-25
 * @time 19:05
 */
@Configuration
public class AppConfig {
    @Bean
    public DeploymentPackageService getDeploymentPackageService() {
        return new DeploymentPackageServiceImpl();
    }
    
    @Bean
    public DeployForm getDeployForm() {
        return new DeployForm();
    }
}
