public class Content {

	private String content;

	public Content() {
		content  = "<err>";
	}

	public Content(String ztate) {
		this.content = ztate;
	}

	public String toString() {
		return content;
	}

	public String getContent() {
		return content;
	}
}
