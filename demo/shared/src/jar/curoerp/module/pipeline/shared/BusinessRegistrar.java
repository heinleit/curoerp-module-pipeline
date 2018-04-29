package jar.curoerp.module.pipeline.shared;

import jar.curoerp.module.pipeline.interfaces.IPipelineService;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class BusinessRegistrar {
	
	public static void register(IPipelineService registrar) {
		registrar.register(IApplePie.class);
	}

}
