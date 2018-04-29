package jar.curoerp.module.pipeline.proxy;

import java.lang.reflect.Method;

public interface IPipelineSenderProxy {
	public Object call(Method method, Object[] args);
	public void setListener(IProxyRequestListener listener);
}
