package jar.curoerp.module.pipeline;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import jar.curoerp.module.pipeline.proxy.ProxyHandler;

public class PipelineRegistrar implements IPipelineRegistrar {

	private ArrayList<Class<?>> dependencies = new ArrayList<>();
	private IDependencyContainer dependencyContainer;
	private InvocationHandler invocationHandler;

	public PipelineRegistrar(IDependencyContainer dependencyContainer, ProxyHandler proxyHandler) {
		this.dependencyContainer = dependencyContainer; 
		this.invocationHandler = proxyHandler;
	}

	@Override
	public void register(Class<?> type) {
		this.dependencies.add(type);
	}

	@Override
	public void boot() {
		for (Class<?> dependency : dependencies) {
			try {
				dependencyContainer.findInstancesOf(dependency);
				LoggingService.debug("Dependency '" + dependency.getName() + "' found! Don't need to start proxy :)");
				continue;
			} catch (DependencyNotResolvedException e) { /* Everything okay, but we need to bound proxy */ }
			
			Object obj = Proxy.newProxyInstance(dependency.getClassLoader(), new Class<?> [] {dependency}, this.invocationHandler);
			try {
				this.dependencyContainer.addResolvedDependency(dependency, obj);
				LoggingService.debug("Dependency '" + dependency.getName() + "' proxied");
			} catch (DependencyNotResolvedException e) {
				LoggingService.error(e);
				// real error, log this
			}
		}
		this.dependencies.clear();
	}

}
