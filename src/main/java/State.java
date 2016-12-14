import java.io.Serializable;

public class State  implements Serializable {

	private String content;

	public State() {
		content  = "<err>";
	}

	public State(String ztate) {
		this.content = ztate;
	}

	public String toString() {
		return content;
	}

	public String getContent() {
		return content;
	}
}
