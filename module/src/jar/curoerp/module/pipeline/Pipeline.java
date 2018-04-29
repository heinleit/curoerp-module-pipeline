package jar.curoerp.module.pipeline;

import java.lang.reflect.Proxy;

import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import jar.curoerp.module.pipeline.interfaces.IPipelineListener;
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
	public void register(Class<?> cls) {
		try {
			this.dependencyContainer.findInstancesOf(cls);
			LoggingService.info("instance from " + cls.getSimpleName() + " already exists. jump over registration.");
			return;
		} catch (DependencyNotResolvedException e1) { /* allowed */ }
		
		LoggingService.info("register '" + cls.getSimpleName() + "'");
		
		Object obj = Proxy.newProxyInstance(cls.getClassLoader(), new Class<?> [] {cls}, this.invocationHandler);
		try {
			this.dependencyContainer.addResolvedDependency(cls, obj);
		} catch (DependencyNotResolvedException e) {
			LoggingService.error(e);
		}
	}

	@Override
	public void setListener(IPipelineListener listener) {
		this.invocationHandler.setListener(listener);
	}
}
