package jar.curoerp.module.pipeline.client;

import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.PipelineClientConnector;
import jar.curoerp.module.pipeline.PipelineRegistrar;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class PipelineClient implements IBootModule {

	//private IPipelineSatteliteService _pipelineService;
	//private IDependencyContainer _dependencyController;
	private PipelineClientConnector pipelineClient;
	private PipelineRegistrar registrar;
	private IDependencyContainer dependencyContainer;

	public PipelineClient(PipelineRegistrar registrar, PipelineClientConnector pipelineClient, IDependencyContainer dependencyContainer) {
		this.registrar = registrar;
		this.pipelineClient = pipelineClient;
		this.dependencyContainer = dependencyContainer;
	}
	
	@Override
	public void boot() {
		registrar.boot();
		this.pipelineClient.connect("localhost", 2876);
		
		try {
			IApplePie applePie = (IApplePie)this.dependencyContainer.findSingleInstanceOf(IApplePie.class);
			System.out.println(applePie.getApple().kind);
		} catch (DependencyNotResolvedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
