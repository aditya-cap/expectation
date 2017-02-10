/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 08-Feb-2017
 */
package com.capillary.expectation.state.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * @author aditya
 *
 */
@JsonInclude(value = Include.NON_EMPTY)
public class StateSpace {

    private @Getter @Setter List<Transition> transitions = new ArrayList<>();
    private Map<State, Set<Event>> stateToPrecedingEventsMap = new HashMap<State, Set<Event>>();

    public void init() {
        //Build the event to state map
        for (Transition transition : transitions) {
            populatePeceedingTransitionsForState(transition.getFinalState());
        }
    }

    private void populatePeceedingTransitionsForState(State finalState) {
        if (stateToPrecedingEventsMap.containsKey(finalState)) {
            //nothing to do, already been processed
            return;
        }
        Set<Event> events = transitions
                .stream()
                .filter((t) -> t.getFinalState().equals(finalState))
                .map((t) -> t.getEvent())
                .collect(Collectors.toSet());
        stateToPrecedingEventsMap.put(finalState, events);
        transitions.stream().filter((t) -> t.getFinalState().equals(finalState)).forEach(
                (t) -> populatePeceedingTransitionsForState(t.getInitialState()));
    }

    public Set<State> getAllStates() {
        Set<State> states = new HashSet<>();
        for (Transition transition : transitions) {
            states.add(transition.getInitialState());
            states.add(transition.getFinalState());
        }
        return states;
    }

    public Set<Event> getAllEvents() {
        return transitions.stream().map((t) -> t.getEvent()).collect(Collectors.toSet());
    }

    public boolean isEventRelevant(String stateEntityName, String stateEntityStatus, String eventEntityName,
            String eventStatus) {
        State state = State.builder().entity(stateEntityName).status(stateEntityStatus).build();
        Event event = Event.builder().entityModified(eventEntityName).modification(eventStatus).build();

        return isEventRelevant(state, event);
    }

    public boolean isEventRelevant(State state, Event event) {
        return stateToPrecedingEventsMap.getOrDefault(state, new HashSet<>()).contains(event);
    }
}
