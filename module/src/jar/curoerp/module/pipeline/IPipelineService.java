package jar.curoerp.module.pipeline;

public interface IPipelineService {
	
	public void startServer(int port);
	public void registerApi(Class<?> cls);
	
}
