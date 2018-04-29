package jar.curoerp.module.pipeline;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.interfaces.IPipelineListener;

public class PipelineInvocationHandler implements InvocationHandler {

	private IPipelineListener _listener;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		log(method.getDeclaringClass().getName() + ":");
		log("\t" + method.getName() + ":");
		
		if(args!=null)
		for (int i = 0; i < args.length; i++) {
			Object object = args[i];
			log("\t\t" + (i+1) + ". " + object);
		}
		
		return this.send(method, args);
	}

	private void log(String str) {
		LoggingService.debug("~~ Proxy ~~ " + str);
	}
	
	private Object send(Method method, Object[] args) {
		return this._listener == null ? null : this._listener.request(method, args);
	}
	
	public void setListener(IPipelineListener listener) {
		this._listener = listener;
	}
}
