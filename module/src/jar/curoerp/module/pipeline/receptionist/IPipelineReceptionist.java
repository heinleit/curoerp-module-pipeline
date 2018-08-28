package jar.curoerp.module.pipeline.receptionist;

import jar.curoerp.module.pipeline.helper.PipelineRequest;

public interface IPipelineReceptionist {
	public void info(PipelineRequest request);
	public Object request(PipelineRequest request);
}
