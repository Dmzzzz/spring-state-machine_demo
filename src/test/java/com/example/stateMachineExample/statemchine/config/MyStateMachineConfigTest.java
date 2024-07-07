package com.example.stateMachineExample.statemchine.config;

import com.example.stateMachineExample.statemchine.Events;
import com.example.stateMachineExample.statemchine.States;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootTest(classes = MyStateMachineConfig.class)
class MyStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<States, Events> stateMachineFactory;
    private StateMachine<States, Events> stateMachine;

        @BeforeEach
        void init() {
            this.stateMachine = stateMachineFactory.getStateMachine();
        }

    @Test
    void initTest() {
        Assertions.assertThat(stateMachine).isNotNull();
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.BACKLOG);
    }

    @Test
    void greenWay() {
        stateMachine.sendEvent(Events.START_FEATURE);
        stateMachine.sendEvent(Events.FINISH_FEATURE);
        stateMachine.sendEvent(Events.QA_TEAM_APPROVE);

        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.DONE);
    }
}