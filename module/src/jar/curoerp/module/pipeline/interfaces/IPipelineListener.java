package jar.curoerp.module.pipeline.interfaces;

import java.lang.reflect.Method;

public interface IPipelineListener {
	public Object request(Method method, Object[] args);
}
