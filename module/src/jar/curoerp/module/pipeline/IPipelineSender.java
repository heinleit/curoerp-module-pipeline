package jar.curoerp.module.pipeline;

import jar.curoerp.module.pipeline.helper.PipelineRequest;

public interface IPipelineSender {
	public Object sendAndReceive(PipelineRequest request);
	public void answer(int id, Object obj);
	public int send(Object obj);
}
