package jar.curoerp.module.pipeline.interfaces;

public interface IPipelineService {
	public void setListener(IPipelineListener listener);
	public void register(Class<?> cls);
}

