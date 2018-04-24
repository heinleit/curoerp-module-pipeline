package jar.curoerp.module.pipeline;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.curoerp.core.logging.LoggingService;

public class PipelineInvocationHandler implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		log(method.getDeclaringClass().getName() + ":");
		log("\t" + method.getName() + ":");
		
		for (int i = 0; i < args.length; i++) {
			Object object = args[i];
			log("\t\t" + (i+1) + ". " + object);
		}
		
		return null;
	}

	private void log(String str) {
		LoggingService.debug("~~ Proxy ~~ " + str);
	}
}
