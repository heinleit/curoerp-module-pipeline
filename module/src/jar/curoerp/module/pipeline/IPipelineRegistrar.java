package jar.curoerp.module.pipeline;

public interface IPipelineRegistrar {
	public void register(Class<?> type);
	public void boot();
}
