package gdr.drools.leadrouting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

public class LoadTest {
	private StatelessKnowledgeSession ksession;
	private AtomicInteger atomicInteger = new AtomicInteger(0);

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
	}

	private void callEngine(final Lead lead, final Agent[] agents) {
		List<Command> cmds = new ArrayList<Command>();
		cmds.add(CommandFactory.newInsert(lead));
		for (Agent agent : agents) {
			cmds.add(CommandFactory.newInsert(agent));
		}
		ksession.execute(CommandFactory.newBatchExecution(cmds));
		atomicInteger.incrementAndGet();
	}

	@Test
	public void loadTest() {
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		List<Lead> leads = new ArrayList<Lead>(100000);
		for (int i = 0; i < 100000; i++) {
			leads.add(new Lead(i, Type.COLLEGE, "CA"));
		}

		List<Agent> agents = new ArrayList<Agent>();
		for (int i = 0; i < 1000; i++) {
				agents.add(new Agent(99.9, Type.COLLEGE, i, "agent", "CA"));
		}
		final Agent[] agentsA = agents.toArray(new Agent[agents.size()]);

		System.out.println(agents.size());
		System.out.println(leads.size());

		for (Lead lead : leads) {
			final Lead a = lead;
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					callEngine(a, agentsA);
				}
			});
		}
		long t1 = System.currentTimeMillis();
		executorService.shutdown();

		while (!executorService.isTerminated()) {
			System.out.println(atomicInteger.get());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - t1);
	}
}
