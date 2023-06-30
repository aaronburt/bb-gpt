package com.example.exampleplugin;

import lombok.extern.slf4j.Slf4j;
import net.bdavies.babblebot.BabblebotApplication;
import net.bdavies.babblebot.api.IApplication;
import net.bdavies.babblebot.api.config.EPluginPermission;
import net.bdavies.babblebot.api.plugins.PluginType;
import net.bdavies.babblebot.plugins.PluginModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Dev Main class for Development Only will not be used inside the
 * main application when importing the plugin
 *
 * @author me@bdavies (Ben Davies)
 * @since 1.0.0
 */

@Slf4j
@SpringBootApplication
@Import(BabblebotApplication.class)
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"net.bdavies.babblebot", "com.example.exampleplugin"})
@EntityScan(basePackages = {"net.bdavies.babblebot", "com.example.exampleplugin"})
public class DevMain {
    public static void main(String[] args) {
        IApplication app = BabblebotApplication.make(DevMain.class, args);
    }

    @Bean
    CommandLineRunner onBoot(GenericApplicationContext gac, IApplication app) {
        return args -> {
            gac.registerBean(ExamplePlugin.class);
            ExamplePlugin plugin = app.get(ExamplePlugin.class);
            app.getPluginContainer()
                    .addPlugin(
                            plugin,
                            PluginModel
                                    .builder()
                                    .name("example")
                                    .pluginType(PluginType.JAVA)
                                    .config("{}")
                                    .namespace("ep")
                                    .pluginPermissions(EPluginPermission.all())
                                    .build()
                    );
        };
    }
}