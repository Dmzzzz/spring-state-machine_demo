package com.example.stateMachineExample;

import com.example.stateMachineExample.statemchine.Events;
import com.example.stateMachineExample.statemchine.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
public class StateMachineExampleApplication implements ApplicationRunner {

	private  StateMachineFactory<States, Events> stateMachineFactory;

	public StateMachineExampleApplication(StateMachineFactory<States, Events> stateMachineFactory) {
		this.stateMachineFactory = stateMachineFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(StateMachineExampleApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		StateMachine<States, Events> stateMachine = this.stateMachineFactory.getStateMachine();
		State<States, Events> state = stateMachine.getState();
		log.info(" id  {} ", state.getId());
		log.info("начало {} ", state.getId().name());
		String str = "";
		stateMachine.sendEvent(Events.START_FEATURE);
		ExtendedState extendedState = stateMachine.getExtendedState();
		log.info("current state : {}", stateMachine.getState().getId().name());
	}
}