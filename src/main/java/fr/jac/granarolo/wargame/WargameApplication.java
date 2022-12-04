package fr.jac.granarolo.wargame;

import fr.jac.granarolo.wargame.vues.Window;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WargameApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(WargameApplication.class).headless(false).run(args);
		Window window = new Window();
		window.setVisible(true);
	}

}
