package jar.curoerp.module.pipeline.demo.tester;

import java.io.File;

import de.curoerp.core.exception.RuntimeTroubleException;
import de.curoerp.core.functionality.config.ConfigService;
import de.curoerp.core.functionality.config.IConfigService;
import de.curoerp.core.functionality.info.CoreInfo;
import de.curoerp.core.logging.Logging;
import de.curoerp.core.logging.LoggingLevel;
import de.curoerp.core.logging.LoggingService;
import de.curoerp.core.modularity.DependencyService;
import de.curoerp.core.modularity.ModuleService;
import de.curoerp.core.modularity.dependency.DependencyContainer;
import de.curoerp.core.modularity.exception.DependencyLimitationException;
import de.curoerp.core.modularity.exception.DependencyNotResolvedException;
import de.curoerp.core.modularity.exception.ModuleVersionStringInvalidException;
import de.curoerp.core.modularity.module.Module;
import de.curoerp.core.modularity.module.ModuleInfo;
import de.curoerp.core.modularity.module.TypeInfo;
import de.curoerp.core.modularity.versioning.VersionInfo;

public class ServerTester {

	public static void main(String[] args) throws DependencyNotResolvedException {
		// logging / console (everything for debugging)
		LoggingService.DefaultLogging = new Logging(LoggingLevel.INFO);
		
		// dependency-container and module-service
		DependencyContainer container = new DependencyContainer();
		DependencyService service = new DependencyService(container);
		CoreInfo cInfo = new CoreInfo("PipelineDemoServer".toLowerCase(), new File("../../test/"));
		container.addResolvedDependency(CoreInfo.class, cInfo);
		ConfigService cfg = new ConfigService(cInfo);
		container.addResolvedDependency(IConfigService.class, cfg);
		
		ModuleService modules = new ModuleService(service, container, cInfo);
		
		// dependencies
		try {
			
			// Module: Pipeline
			ModuleInfo info = new ModuleInfo();
			info.name = "Pipeline";
			info.typeInfos = new TypeInfo[] {
					new TypeInfo("jar.curoerp.module.pipeline.PipelineRegistrar", "jar.curoerp.module.pipeline.IPipelineRegistrar"),
					new TypeInfo("jar.curoerp.module.pipeline.PipelineServerService", ""),
					new TypeInfo("jar.curoerp.module.pipeline.proxy.ProxyHandler", ""),
					new TypeInfo("jar.curoerp.module.pipeline.receptionist.PipelineReceptionist", "jar.curoerp.module.pipeline.receptionist.IPipelineReceptionist"),
					new TypeInfo("jar.curoerp.module.pipeline.proxy.PipelineSenderProxy", "jar.curoerp.module.pipeline.proxy.IPipelineSenderProxy"),
			};
			Module pipeline = new Module(new VersionInfo("1"), info);
			
			// Module: Pipeline Demo Share
			info = new ModuleInfo();
			info.name = "PipelineDemoShared";
			info.typeInfos = new TypeInfo[] {
					new TypeInfo("jar.curoerp.module.pipeline.shared.BusinessRegistrar", "")
			};
			Module pipelineDemoShared = new Module(new VersionInfo("1"), info);
			
			// Module: Pipeline Demo Server
			info = new ModuleInfo();
			info.name = "PipelineDemoServer";
			info.typeInfos = new TypeInfo[] {
					new TypeInfo("jar.curoerp.module.pipeline.demo.server.ApplePie", "jar.curoerp.module.pipeline.shared.business.IApplePie"),
					new TypeInfo("jar.curoerp.module.pipeline.server.ServerBoot", "")
			};
			info.bootClass = "jar.curoerp.module.pipeline.server.ServerBoot";
			Module pipelineDemoServer = new Module(new VersionInfo("1"), info);
			
			modules.setModules(new Module[] {pipeline, pipelineDemoShared, pipelineDemoServer});
			
			// resolve dependencies
			modules.boot();
		} catch (RuntimeTroubleException | DependencyLimitationException | ModuleVersionStringInvalidException e) {
			LoggingService.error(e);
			return;
		}

		LoggingService.info("DlS started!");
		

		LoggingService.info("Jump into Boot-Module");
		
		// finally: boot (module: pipeline)
		try {
			modules.runModule("PipelineDemoServer".toLowerCase());
		} catch (RuntimeTroubleException | DependencyNotResolvedException e) {
			LoggingService.error(e);
			return;
		}
	}

}
