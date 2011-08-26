package gdr.drools.forwardchaining;

import java.io.IOException;
import java.io.InputStreamReader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.junit.Before;
import org.junit.Test;

public class StateTest {
	private StatefulSession session;

	@Test
	public void test() {
		State a = new State("A");
		State b = new State("B");
		State c = new State("C");
		State d = new State("D");
		session.insert(a, true);
		session.insert(b, true);
		session.insert(c, true);
		session.insert(d, true);
		session.fireAllRules();
		session.dispose();
	}

	@Before
	public void before() {

		final PackageBuilder builder = new PackageBuilder();
        try {
            builder.addPackageFromDrl( new InputStreamReader( StateTest.class.getResourceAsStream( "state.drl" ) ) );
        } catch (DroolsParserException e) {
            throw new IllegalArgumentException("Invalid drl", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read drl", e);
        }

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( builder.getPackage() );

        session = ruleBase.newStatefulSession();
	}
}
