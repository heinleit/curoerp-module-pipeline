package jar.curoerp.module.pipeline.helper;

public class PipelineTag {
	private int id;
	private PipelineType type;
	private String hash;
	private Class<?> cls;

	public PipelineTag(int id, PipelineType type, String hash, Class<?> cls) {
		this.id = id;
		this.type = type;
		this.hash = hash;
		this.cls = cls;
	}

	public int getId() {
		return id;
	}

	public PipelineType getType() {
		return type;
	}

	public String getHash() {
		return hash;
	}
	
	public Class<?> getCls() {
		return this.cls;
	}

	public boolean match(PipelineTag other) {
		System.out.println("this:" + this);
		System.out.println("other:" + other);
		return this.hash.equalsIgnoreCase(other.hash) 
				&& this.type == other.type
				&& this.id == other.id;
	}

	@Override
	public String toString() {
		return id + ":" + type + ":" + hash;
	}

	public boolean validateContent(String content) {
		return this.hash.equalsIgnoreCase(PipelineHelper.sha256(content));
	}
}
