package gdr.drools.scouts;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class App {
	StatelessKnowledgeSession session;

	public App() {
		session = getSession();
	}

	void run() {
		System.out.println(findClubType(new Person(Gender.FEMALE, 10, "Gill")).getClubType());
		System.out.println(findClubType(new Person(Gender.MALE, 10, "DEv")).getClubType());
	}

	private Person findClubType(Person person) {
		session.execute(person);
		return person;
	}

	private StatelessKnowledgeSession getSession() {
		DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtableconfiguration.setInputType(DecisionTableInputType.XLS);

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newClassPathResource("gdr/drools/scouts/scouts.xls", getClass()), ResourceType.DTABLE, dtableconfiguration);

		if (kbuilder.hasErrors()) {
			throw new RuntimeException(kbuilder.getErrors().toString());
		}

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatelessKnowledgeSession session = kbase.newStatelessKnowledgeSession();
		//session.addEventListener(new DebugWorkingMemoryEventListener());
		//session.addEventListener(new DebugAgendaEventListener());
		return session;
	}

	public static void main(String[] args) {
		new App().run();
	}
}
