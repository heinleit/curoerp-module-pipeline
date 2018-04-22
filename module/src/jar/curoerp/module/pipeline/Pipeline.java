package jar.curoerp.module.pipeline;

import de.curoerp.core.modularity.dependency.IDependencyContainer;

public class Pipeline implements IPipelineService {

	private IDependencyContainer dependencyContainer;

	public Pipeline(IDependencyContainer dependencyContainer) {
		this.dependencyContainer = dependencyContainer;
	}
	
	@Override
	public void startServer(int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerApi(Class<?> cls) {
		// TODO Auto-generated method stub
		
		
		
	}
}
