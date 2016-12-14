 import javax.validation.constraints.Min;
 import javax.validation.constraints.Max;
 import javax.validation.constraints.Size;
 import javax.validation.constraints.NotNull;

 public class Appendee {

	@Min(1)
	@Max(9)
	private long count;

	@NotNull
	@Size(min=1,max=1,message="appendee must be one character")
	private String content;

	public Appendee() {
		content  = "<err>";
	}

	public Appendee(String ztate) {
		this.content = ztate;
	}

	public int getCount() {
		return (int)count;
	}

	public String getContent() {
		return content;
	}
}
