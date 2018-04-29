package jar.curoerp.module.pipeline.shared;

import jar.curoerp.module.pipeline.PipelineRegistrar;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class BusinessRegistrar {
	
	public BusinessRegistrar(PipelineRegistrar registrar) {
		registrar.register(IApplePie.class);
	}

}
