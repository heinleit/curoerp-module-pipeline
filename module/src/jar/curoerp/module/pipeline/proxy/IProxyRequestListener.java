package jar.curoerp.module.pipeline.proxy;

import jar.curoerp.module.pipeline.helper.PipelineRequest;

public interface IProxyRequestListener {

	Object sendAndReceive(PipelineRequest request);

}
