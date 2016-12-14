import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.*;
// import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import javax.validation.constraints.NotNull;

@RestController
@EnableAutoConfiguration
public class Server {

	HashMap<UUID, String> states = new HashMap<UUID, String>();

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	@NotNull
	private UUID uid(HttpSession session) {
		UUID uuid = (UUID) session.getAttribute("uid");
		if (uuid == null) {
			uuid = UUID.randomUUID();
			session.setAttribute("uid", uuid);
		}
		return uuid;
	}

	@GetMapping("/")
	String front(HttpSession session) {
		return uid(session).toString();
	}

	@RequestMapping("/help")
	String home() {
		return "See documentation for usage";
	}

	@PostMapping(value="/chars")
	public int append(HttpSession session, @ModelAttribute State appendee) {
		final UUID uuid = uid(session);
		final String stateNow = states.getOrDefault(uuid, "") + appendee.toString();	// TODO: not per spec.
		logger.info("POST: uid=" + uuid + " state=" + stateNow);
		states.put(uuid, stateNow);
		return 200;
	}

	@GetMapping("/state")
	public State state(HttpSession session) {
		final UUID uuid = uid(session);
		final String state = states.getOrDefault(uuid, "");
		logger.info("GET: uid=" + uuid + " state=" + state);
		return new State(state);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Server.class, args);
	}

}
