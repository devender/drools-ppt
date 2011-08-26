package gdr.drools.stateless;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStateless {
	private StatelessKnowledgeSession ksession;
	private KnowledgeRuntimeLogger logger;

	@Before
	public void before() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("licenseApplication.drl", getClass()), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			System.err.println(kbuilder.getErrors().toString());
			System.exit(-1);
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		ksession = kbase.newStatelessKnowledgeSession();
		// ksession.addEventListener( new DebugWorkingMemoryEventListener() );
		// ksession.addEventListener( new DebugWorkingMemoryEventListener() );
		logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/stateless");
	}

	@After
	public void after() {
		logger.close();
	}

	private void callEngine(Applicant applicant, Application application) {
		ksession.execute(Arrays.asList(new Object[] { application, applicant }));
	}

	@Test
	public void one() {
		Applicant applicant = new Applicant("Mr John Smith", 16, false);
		Application application = new Application(100,"CA");
		callEngine(applicant, application);
		assertThat(application.isValid(), is(false));
	}

	@Test
	public void two() {
		Applicant applicant = new Applicant("Mr John Smith", 18, false);
		Application application = new Application(100,"CA");
		callEngine(applicant, application);
		assertThat(application.isValid(), is(true));
	}
	
	@Test
	public void three() {
		Applicant applicant = new Applicant("Mr John Smith", 18, false);
		Application application = new Application(65,"TX");
		callEngine(applicant, application);
		assertThat(application.isValid(), is(true));
	}

	

}
