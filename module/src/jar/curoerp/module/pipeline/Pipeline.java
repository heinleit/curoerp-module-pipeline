package jar.curoerp.module.pipeline;

import java.lang.reflect.Proxy;

import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import jar.curoerp.module.pipeline.interfaces.IPipelineService;

public class Pipeline implements IPipelineService {

	private IDependencyContainer dependencyContainer;
	private PipelineInvocationHandler invocationHandler;

	public Pipeline(IDependencyContainer dependencyContainer, PipelineInvocationHandler invocationHandler) {
		this.dependencyContainer = dependencyContainer;
		this.invocationHandler = invocationHandler;
		// Register 
	}
	
	@Override
	public void startServer(int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerApi(Class<?> cls) {
		Object obj = Proxy.newProxyInstance(cls.getClassLoader(), new Class<?> [] {cls}, this.invocationHandler);
		try {
			this.dependencyContainer.addResolvedDependency(cls, obj);
		} catch (DependencyNotResolvedException e) {
			LoggingService.error(e);
		}
	}
}
