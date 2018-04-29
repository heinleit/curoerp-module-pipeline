package jar.curoerp.module.pipeline.center;

import java.net.Socket;
import java.util.Arrays;

import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import jar.curoerp.module.pipeline.shared.PipelineRequest;
import jar.curoerp.module.pipeline.shared.PipelineSocketBase;

public class PipelineCenterConnectionDealer extends PipelineSocketBase {

	private IDependencyContainer _dependencyContainer;

	public PipelineCenterConnectionDealer(Socket socket, IDependencyContainer dependencyContainer) {
		this._socket = socket;
		this._dependencyContainer = dependencyContainer;
		this.deal();
	}

	public void deal() {
		this.createReader().start();
		this.createWriter().start();
	}

	public void close() {
		this._socket = null;
	}	
	
	@Override
	public void info(PipelineRequest request) {
		try {
			Object instance = this._dependencyContainer.findInstancesOf(request.cls);
			instance.getClass().getDeclaredMethod(request.method, Arrays.stream(request.arguments).map(p -> p.getClass()).toArray(c -> new Class[c]));
			//request.method.invoke(instance, request.arguments);
		} catch (DependencyNotResolvedException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			LoggingService.warn(e);
		}
	}

	@Override
	public Object request(PipelineRequest request) {
		try {
			Object instance = this._dependencyContainer.findInstancesOf(request.cls);
			return instance.getClass().getDeclaredMethod(request.method, Arrays.stream(request.arguments).map(p -> p.getClass()).toArray(c -> new Class[c]));
		} catch (DependencyNotResolvedException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			LoggingService.warn(e);
		}
		return null;
	}

}
