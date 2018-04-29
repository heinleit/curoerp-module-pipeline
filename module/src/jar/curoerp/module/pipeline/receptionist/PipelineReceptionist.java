package jar.curoerp.module.pipeline.receptionist;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import jar.curoerp.module.pipeline.helper.PipelineRequest;
import jar.curoerp.module.pipeline.processor.IPipelineReceptionist;

public class PipelineReceptionist implements IPipelineReceptionist {

	private IDependencyContainer dependencyContainer;

	public PipelineReceptionist(IDependencyContainer dependencyContainer) {
		this.dependencyContainer = dependencyContainer;
		
	}
	
	@Override
	public void info(PipelineRequest request) {
		this.receive(request);
	}

	@Override
	public Object request(PipelineRequest request) {
		return this.receive(request);
	}

	public Object receive(PipelineRequest request) {
		try {
			Class<?> cls = Class.forName(request.cls);
			Object obj = this.dependencyContainer.findSingleInstanceOf(cls);
			return cls.getDeclaredMethod(request.method, request.arguments == null ? null : Arrays.stream(request.arguments).map(p -> p.getClass()).toArray(c -> new Class<?>[c])).invoke(obj, request.arguments);
		} catch (ClassNotFoundException | DependencyNotResolvedException
				| NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LoggingService.warn(e);
		}
		return null;
	}

}
