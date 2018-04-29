package jar.curoerp.module.pipeline.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {

	private IPipelineSenderProxy senderProxy;

	public ProxyHandler(IPipelineSenderProxy senderProxy) {
		this.senderProxy = senderProxy;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return senderProxy.call(method, args);
	}
	

}
