import java.util.HashMap;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;  
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Server {

	/**
		The specification is that "two different browsers from single computer are two different users"
		so that means using the session id. as a uniqueness identifier.  However, that makes testing
		difficult because curl gives a new session id. to each request.  One way around it is to write our own
		testing tool, or maybe use Grinder, but here we take the simple but flawed approach of allowing the
		IP address of the client to serve as the unique identifier.  Undoubtedly that means that the test is not
		quite the same as the real thing.
	*/
	@Value("${application.uniqueness}")
	private String uniqueness;

	public enum UniqValues { bySession, byIPaddress }

	UniqValues uniqueWhat () {
		return uniqueness == null
			? UniqValues.bySession
			: (uniqueness.equals("IPaddress") ? UniqValues.byIPaddress : UniqValues.bySession);
	}

	/**
		We could have generalised the concept of uniqueness identifier with an abstract class providing
		methods to get and set hashmap values but that's over-engineering this simple problem.
	*/
	HashMap<String, String> states = new HashMap<String, String>();

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	@NotNull
	private String uid(HttpSession session, HttpServletRequest request) {
		String uuid;
		switch (uniqueWhat()) {
		case bySession:
			// from http://docs.oracle.com/javaee/6/api/?javax/servlet/http/HttpSession.html
			uuid = session.getId();
			break;
		case byIPaddress:
			// might not work;  http://stackoverflow.com/questions/22877350/how-to-extract-ip-address-in-spring-mvc-controller-get-call
			uuid = request.getRemoteAddr();
			break;
		default:
			// why do we need this???
			uuid = session.getId();
		}
		return uuid;
	}

	@GetMapping("/")
	String front() {
		return "See documentation for usage";
	}

	@RequestMapping("/help")
	String home() {
		return "See documentation for usage";
	}

	@PostMapping("/chars")
	public ResponseEntity<String> append(HttpSession session, HttpServletRequest request, @Valid @RequestBody State appendee) {
		final String uuid = uid(session, request);
		final String stateNow = states.getOrDefault(uuid, "") + appendee.toString();	// TODO: not per spec.
		logger.info("POST: state=" + stateNow + " from=" + appendee.toString());
		states.put(uuid, stateNow);
		return ResponseEntity.ok().body(stateNow);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ValidationError handleException(MethodArgumentNotValidException exception) {
		logger.warn("running error handler");
		return ValidationErrorBuilder.fromBindingErrors(exception.getBindingResult());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleException(HttpMessageNotReadableException exception) {
		final String cause = exception.getCause().toString();
		logger.warn("input validation error: " + cause);
		return ResponseEntity.badRequest().body(cause);
	}

	@GetMapping("/state")
	public State state(HttpSession session, HttpServletRequest request) {
		final String uuid = uid(session, request);
		final String state = states.getOrDefault(uuid, "");
		logger.info("GET: state=" + state);
		return new State(state);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Server.class, args);
	}

}
