package ru.ifmo.study.term6.parsing.lab4;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Нияз
 * Date: 17.06.12
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class Grammar {
    final public Set<Terminal> terminals;
    final public Set<NonTerminal> nonTerminals;
    final public Map<NonTerminal, List<Rule>> rules;
    private Set<Terminal> first;
    private Set<Terminal> follow;

    public Grammar(Set<Terminal> terminals, Set<NonTerminal> nonTerminals, Map<NonTerminal, List<Rule>> rules) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
    }

    public boolean isLL1() {
        // TODO implement isLL1
        return false;
    }

}
