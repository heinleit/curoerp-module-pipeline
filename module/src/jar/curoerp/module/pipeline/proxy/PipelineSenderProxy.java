package jar.curoerp.module.pipeline.proxy;

import java.lang.reflect.Method;
import jar.curoerp.module.pipeline.helper.PipelineRequest;

public class PipelineSenderProxy implements IPipelineSenderProxy {

	private IProxyRequestListener listener = null;

	public PipelineSenderProxy() {
		// nothing
	}

	@Override
	public Object call(Method method, Object[] args) {
		PipelineRequest request = new PipelineRequest();
		request.cls = method.getDeclaringClass().getName();
		request.method = method.getName();
		request.arguments = args;
		
		return this.listener.sendAndReceive(request);
	}
	
	public void setListener(IProxyRequestListener requestListener) {
		this.listener = requestListener;
	}
}
