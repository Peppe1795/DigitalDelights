package Giuseppe.DigitalDelights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ProductRunner implements CommandLineRunner {

	@Autowired
	private RandomInstanceGenerator rndGenerator;

	@Override
	public void run(String... args) throws Exception {
		// rndGenerator.randomProductGenerator(100);

	}

}
