package jar.curoerp.module.pipeline.client;

import de.curoerp.core.modularity.dependency.IDependencyContainer;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;
import jar.curoerp.module.pipeline.interfaces.IPipelineService;
import jar.curoerp.module.pipeline.shared.BusinessRegistrar;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class PipelineClient implements IBootModule {

	private IPipelineSatteliteService _pipelineService;
	private IDependencyContainer _dependencyController;

	public PipelineClient(IPipelineService registrar, IPipelineSatteliteService pipelineService, IDependencyContainer dependencyController) {
		BusinessRegistrar.register(registrar);
		
		this._pipelineService = pipelineService;
		this._dependencyController = dependencyController;
	}
	
	@Override
	public void boot() {
		this._pipelineService.connect("localhost", 2876);
		
		try {
			IApplePie applePie = (IApplePie)this._dependencyController.findSingleInstanceOf(IApplePie.class);
			System.out.println(applePie.getApple().kind);
		} catch (DependencyNotResolvedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
