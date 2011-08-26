package gdr.drools.stateful;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Before;
import org.junit.Test;

public class TestStateful {
	private StatefulKnowledgeSession ksession;
	private Map<String, Room> name2room;
	private List<String> messages;

	@Before
	public void before() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("fireAlarm.drl", getClass()), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			System.err.println(kbuilder.getErrors().toString());
			System.exit(-1);
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		ksession = kbase.newStatefulKnowledgeSession();
		messages = new ArrayList<String>();
		ksession.setGlobal("myGlobalList", messages);
		String[] names = new String[] { "kitchen", "bedroom", "office", "livingroom" };

		name2room = new HashMap<String, Room>();
		for (String name : names) {
			Room room = new Room(name);
			name2room.put(name, room);
			ksession.insert(room);
			Sprinkler sprinkler = new Sprinkler(room);
			ksession.insert(sprinkler);
		}
	}

	@Test
	public void testOne() {
		ksession.fireAllRules();
		assertEquals(messages.size(), 1);
		assertTrue(messages.contains("Everything is ok"));

		// start fires
		Fire kitchenFire = new Fire(name2room.get("kitchen"));
		Fire officeFire = new Fire(name2room.get("office"));

		FactHandle kitchenFireHandle = ksession.insert(kitchenFire);
		FactHandle officeFireHandle = ksession.insert(officeFire);

		ksession.fireAllRules();
		assertEquals(messages.size(), 4);
		
		assertTrue(messages.contains("Turn on the sprinkler for room office"));
		assertTrue(messages.contains("Raise the alarm"));
		assertTrue(messages.contains("Turn on the sprinkler for room kitchen"));

		// retract
		ksession.retract(kitchenFireHandle);
		ksession.retract(officeFireHandle);
		ksession.fireAllRules();
		assertEquals(messages.size(), 8);
		assertTrue(messages.contains("Cancel the alarm"));
		assertTrue(messages.contains("Turn off the sprinkler for room office"));
		assertTrue(messages.contains("Turn off the sprinkler for room kitchen"));
		assertTrue(messages.contains("Everything is ok"));

	}

}
