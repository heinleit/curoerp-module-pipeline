package jar.curoerp.module.pipeline.shared;

public interface IPipelineReceptionist {
	public void info(PipelineRequest request);
	public Object request(PipelineRequest request);
}
