package jar.curoerp.module.pipeline.client;

import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class PipelineClient implements IBootModule {

	private IApplePie _applePie;
	private IPipelineSatteliteService _pipelineService;

	public PipelineClient(IApplePie applePie, IPipelineSatteliteService pipelineService) {
		this._applePie = applePie;
		this._pipelineService = pipelineService;
	}
	
	@Override
	public void boot() {
		//this._applePie.getApple();
		this._pipelineService.connect("localhost", 2876);
	}

}
