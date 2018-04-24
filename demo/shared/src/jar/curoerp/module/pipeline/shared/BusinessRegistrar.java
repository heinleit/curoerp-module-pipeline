package jar.curoerp.module.pipeline.shared;

import jar.curoerp.module.pipeline.interfaces.IPipelineService;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class BusinessRegistrar {
	
	public BusinessRegistrar(IPipelineService pipelineService) {
		
		// Register Interfaces
		pipelineService.registerApi(IApplePie.class);
		
	}

}
