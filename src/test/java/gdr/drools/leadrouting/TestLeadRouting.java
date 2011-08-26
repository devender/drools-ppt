package gdr.drools.leadrouting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLeadRouting {
	private StatelessKnowledgeSession ksession;
	private KnowledgeRuntimeLogger logger;

	@Before
	public void before() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("route.drl", getClass()), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			System.err.println(kbuilder.getErrors().toString());
			System.exit(-1);
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		ksession = kbase.newStatelessKnowledgeSession();
		//ksession.addEventListener( new DebugWorkingMemoryEventListener() );
		//ksession.addEventListener( new DebugWorkingMemoryEventListener() );
		logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/stateless");
	}

	private void callEngine(Lead lead, Agent[] agents) {
		List<Command> cmds = new ArrayList<Command>();
		cmds.add(CommandFactory.newInsert(lead));
		for (Agent agent : agents) {
			cmds.add(CommandFactory.newInsert(agent));
		}
		ksession.execute(CommandFactory.newBatchExecution(cmds));
	}

	@After
	public void after() {
		logger.close();
	}

	@Test
	public void highSchoolLead_simplematch() {
		Lead lead = new Lead(0, Type.HIGHSCHOOL);

		Agent universityAgent = new Agent(99.9, Type.UNIVERSITY, 1, "universityAgent");
		
		Agent collegeAgent = new Agent(99.9, Type.COLLEGE, 1, "collegeAgent");
		
		Agent highSchoolAgent_OneFullFilled = new Agent(99.9, Type.HIGHSCHOOL, 1, "highSchoolAgent_OneFullFilled");

		Agent[] agents = new Agent[] { universityAgent, collegeAgent, highSchoolAgent_OneFullFilled };
		
		callEngine(lead, agents);
		
		assertEquals("highSchoolAgent_OneFullFilled", lead.getAgent());
	}

	@Test
	public void highSchoolLead_fullfilment() {
		Lead lead = new Lead(0, Type.HIGHSCHOOL);

		Agent universityAgent = new Agent(99.9, Type.UNIVERSITY, 1, "universityAgent");

		Agent collegeAgent = new Agent(99.9, Type.COLLEGE, 1, "collegeAgent");
		
		Agent highSchoolAgent_OneFullFilled = new Agent(99.9, Type.HIGHSCHOOL, 1, "highSchoolAgent_OneFullFilled");
		
		Agent highSchoolAgent_ZeroFullFilled = new Agent(99.9, Type.HIGHSCHOOL, 0, "highSchoolAgent_ZeroFullFilled");

		Agent[] agents = new Agent[] {universityAgent, collegeAgent, highSchoolAgent_OneFullFilled, highSchoolAgent_ZeroFullFilled};
		
		callEngine(lead, agents);
		assertEquals("highSchoolAgent_ZeroFullFilled", lead.getAgent());
	}
	
	@Test
	public void highSchoolLead_conversion() {
		Lead lead = new Lead(0, Type.HIGHSCHOOL);

		Agent universityAgent = new Agent(99.9, Type.UNIVERSITY, 1, "universityAgent");
		
		Agent collegeAgent = new Agent(99.9, Type.COLLEGE, 1, "collegeAgent");
		
		Agent highSchoolAgent_OneFullFilled = new Agent(99.9, Type.HIGHSCHOOL, 1, "highSchoolAgent_OneFullFilled");
		
		Agent highSchoolAgent_ZeroFullFilled = new Agent(10.9, Type.HIGHSCHOOL, 0, "highSchoolAgent_ZeroFullFilled");

		Agent[] agents = new Agent[] {universityAgent, collegeAgent, highSchoolAgent_OneFullFilled, highSchoolAgent_ZeroFullFilled};
		callEngine(lead, agents);
		
		assertEquals("highSchoolAgent_OneFullFilled", lead.getAgent());
	}
	
	@Test
	public void alwaysProvideAnAgent() {
		Lead lead = new Lead(0, Type.HIGHSCHOOL);

		Agent universityAgent = new Agent(99.9, Type.UNIVERSITY, 1, "universityAgent");
		
		Agent collegeAgent = new Agent(99.9, Type.COLLEGE, 1, "collegeAgent");

		Agent[] agents = new Agent[] {universityAgent, collegeAgent};
		callEngine(lead, agents);
		
		System.out.println(lead.getAgent());
		assertNotNull(lead.getAgent());
	}

}
