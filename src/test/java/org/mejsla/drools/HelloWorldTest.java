package org.mejsla.drools;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldTest {

    private static final String K_SESSION_NAME = "HelloWorldKS";
    private KieSession kieSession;

    @Before
    public void before() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kieSession = kContainer.newKieSession(K_SESSION_NAME);

        Logger ruleLogger = LoggerFactory.getLogger(K_SESSION_NAME);
        kieSession.setGlobal("log", ruleLogger);
    }

    @Test
    public void shouldSayGoodbye() {
        Message message = new Message();
        message.setMessage("Hello world");
        message.setStatus(MessageStatus.HELLO);

        kieSession.insert(message);
        kieSession.fireAllRules();

        assertThat(message.getMessage()).isEqualTo("Goodbye cruel world");
        assertThat(message.getStatus()).isEqualTo(MessageStatus.GOODBYE);

    }


}
