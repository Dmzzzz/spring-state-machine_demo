package com.example.stateMachineExample.statemchine.config;

import com.example.stateMachineExample.statemchine.Events;
import com.example.stateMachineExample.statemchine.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Optional;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class MyStateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.BACKLOG)
                .state(States.IN_PROGRESS)
                .state(States.TESTING, deployAction())
                .state(States.DONE);
    }

    private StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void transition(Transition<States, Events> transition) {
                log.info("Move from {} to {}",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log.info("not accepted {}", event);
            }

            private Object ofNullableState(State state) {
                return Optional.ofNullable(state)
                        .map(State::getId)
                        .orElse(null);
            }

        };
    }

    private Action<States, Events> deployAction() {
        return contex -> log.warn("Выкатываемся! в Продакшен");

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions.withExternal()
                .source(States.BACKLOG)
                .target(States.IN_PROGRESS)
                .event(Events.START_FEATURE)
                .and()
                .withExternal()
                .source(States.IN_PROGRESS)
                .target(States.TESTING)
                .event(Events.FINISH_FEATURE)
                .and()
                .withExternal()
                .source(States.TESTING)
                .target(States.DONE)
                .event(Events.QA_TEAM_APPROVE)
                .and()
                .withExternal()
                .source(States.TESTING)
                .target(States.IN_PROGRESS)
                .event(Events.QA_TEAM_REJECT);
    }
}