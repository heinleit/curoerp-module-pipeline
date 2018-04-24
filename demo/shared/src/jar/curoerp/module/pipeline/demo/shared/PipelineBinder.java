package jar.curoerp.module.pipeline.demo.shared;

import jar.curoerp.module.pipeline.interfaces.*;

public class PipelineBinder {
	
	//Only for Bootstrapping!
	public PipelineBinder(IPipelineService pipeline) {
		pipeline.registerApi(IAppleModel.class);
	}
	
}
