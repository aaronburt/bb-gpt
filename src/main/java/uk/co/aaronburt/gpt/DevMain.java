package uk.co.aaronburt.gpt;

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
@EnableJpaRepositories(basePackages = {"net.bdavies.babblebot", "uk.co.aaronburt.gpt"})
@EntityScan(basePackages = {"net.bdavies.babblebot", "uk.co.aaronburt.gpt"})
public class DevMain {
    public static void main(String[] args) {
        IApplication app = BabblebotApplication.make(DevMain.class, args);
    }

    @Bean
    CommandLineRunner onBoot(GenericApplicationContext gac, IApplication app) {
        return args -> {
            gac.registerBean(AskGPT.class);
            AskGPT plugin = app.get(AskGPT.class);
            app.getPluginContainer()
                    .addPlugin(
                            plugin,
                            PluginModel
                                    .builder()
                                    .name("ask-gpt")
                                    .pluginType(PluginType.JAVA)
                                    .config("{}")
                                    .namespace("gpt")
                                    .pluginPermissions(EPluginPermission.all())
                                    .build()
                    );
        };
    }
}